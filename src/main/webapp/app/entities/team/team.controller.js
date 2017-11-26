(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$state', 'Team', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', '$uibModal'];

    function TeamController($state, Team, ParseLinks, AlertService, paginationConstants, pagingParams, $uibModal) {

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
    }
})();
