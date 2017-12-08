(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['$state', 'Project', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$uibModal', 'DTOptionsBuilder', 'DTColumnBuilder', 'moment', '$scope'];

    function ProjectController($state, Project, ParseLinks, AlertService, paginationConstants, pagingParams, $uibModal, DTOptionsBuilder, DTColumnBuilder, moment, $scope) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.createOrUpdate = createOrUpdate;

        loadAll();

        function loadAll() {
            Project.query({
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
                vm.projects = data;
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
                url: 'api/projects/search',
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
        ;

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('#'),
            DTColumnBuilder.newColumn('code').withTitle('PROJECT CODE'),
            DTColumnBuilder.newColumn('name').withTitle('PROJECT NAME'),
            DTColumnBuilder.newColumn('customer').withTitle('CUSTOMER'),
            DTColumnBuilder.newColumn('type').withTitle('PROJECT TYPE').renderWith(function (data) {
                return vm.types[data];
            }),
            DTColumnBuilder.newColumn('managerName').withTitle('PROJECT LEADER'),
            DTColumnBuilder.newColumn('startDate').withTitle('START DATE').renderWith(function (data) {
                var value = '';
                if (data) {
                    value = moment(data).format('DD/MM/YYYY');
                }
                return value;
            }),
            DTColumnBuilder.newColumn('status').withTitle('STATUS').renderWith(function (data) {
                if (data === 'ACTIVE') {
                    return '<p class="active">Active</p>';
                } else {
                    return '<p class="inactive">Inactive</p>';
                }
            })
        ];

        function rowClick(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            // Unbind first in order to avoid any duplicate handler (see https://github.com/l-lin/angular-datatables/issues/87)
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function () {
                var project = aData;
                if (project['startDate']) {
                    project['startDate'] = moment(project['startDate']).toDate();
                }

                $scope.$apply(function () {
                    vm.createOrUpdate(project);
                });
            });
            return nRow;
        }

        function createOrUpdate(project) {
            $uibModal.open({
                templateUrl: 'app/entities/project/project-dialog.html',
                controller: 'ProjectDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {
                    entity: function () {
                        return project;
                    }
                }
            }).result.then(function() {
                $state.go('project', null, { reload: 'project' });
            }, function() {
                $state.go('project');
            });
        }

        vm.types = {
            'AUTO': 'Auto',
            'MANUAL': 'Manual'
        }


    }
})();
