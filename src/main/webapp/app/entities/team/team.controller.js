(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$state', 'Team', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$uibModal', 'DTOptionsBuilder', 'DTColumnBuilder', 'moment', '$scope'];

    function TeamController($state, Team, ParseLinks, AlertService, paginationConstants, pagingParams, $uibModal, DTOptionsBuilder, DTColumnBuilder, moment, $scope) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            Team.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.teams = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.createOrUpdateTeam = createOrUpdateTeam;
        function createOrUpdateTeam(team) {
            if(!team) {
                team = {
                    name: null,
                    status: 'ACTIVE',
                    id: null
                };
            }

            $uibModal.open({
                templateUrl: 'app/entities/team/team-dialog.html',
                controller: 'TeamDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return team;
                    }
                }
            }).result.then(function() {
                $state.go('team', null, { reload: 'team' });
            }, function() {
                $state.go('team');
            });
        }


        vm.dtInstance = {};
        vm.searchText = '';
        vm.search = search;

        function search() {
            var resetPaging = true;
            vm.dtInstance.reloadData(searchCallback, resetPaging);
        }

        function searchCallback(data) {

        }

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('jhi-authenticationToken').replace(new RegExp('"', 'g'), '')
                },
                // Either you specify the AjaxDataProp here
                // dataSrc: 'data',
                url: 'api/teams/search',
                data: function(d){
                    var params = {};

                    params.search = vm.searchText;

                    if(d.order && d.order.length) {
                        params.sort = d.columns[d.order[0].column].data + ',' + d.order[0].dir;
                        for(var i = 1; i < d.order.length; i++) {
                            params.sort += d.columns[d.order[i].column].data + ',' + d.order[i].dir;
                        }
                    }
                    params.page = d.start / d.length;
                    params.size = d.length;

                    return params;
                },
                type: 'GET'
            })
            .withDataProp('data')
            .withPaginationType('full_numbers')
            .withOption('bFilter', false)
            .withOption('processing', true) // required
            .withOption('serverSide', true)// required
            .withOption('rowCallback', rowClick);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('name').withTitle('TEAM NAME'),
            DTColumnBuilder.newColumn('leaderName').withTitle('TEAM LEADER'),
            DTColumnBuilder.newColumn('members').withTitle('MEMBER').renderWith(function(members) {
                var totalMember = 0;
                if(members) {
                    totalMember = members.length;
                }
                return totalMember;
            }),
            DTColumnBuilder.newColumn('createdDate').withTitle('CREATE AT').renderWith(function (data) {
                var value = '';
                if(data) {
                    value = moment(data).format('DD/MM/YYYY');
                }
                return value;
            }),
            DTColumnBuilder.newColumn('status').withTitle('STATUS').renderWith(function(data) {
                if(data === 'ACTIVE') {
                    return '<p class="active">Active</p>';
                }else {
                    return '<p class="inactive">Inactive</p>';
                }
            })
        ];

        function rowClick(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            // Unbind first in order to avoid any duplicate handler (see https://github.com/l-lin/angular-datatables/issues/87)
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function() {
                var team = aData;

                $scope.$apply(function() {
                    vm.createOrUpdateTeam(team);
                });
            });
            return nRow;
        }
    }
})();
