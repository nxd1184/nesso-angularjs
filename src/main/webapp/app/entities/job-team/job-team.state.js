(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-team', {
            parent: 'entity',
            url: '/job-team?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeams'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team/job-teams.html',
                    controller: 'JobTeamController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('job-team-detail', {
            parent: 'job-team',
            url: '/job-team/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeam'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team/job-team-detail.html',
                    controller: 'JobTeamDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JobTeam', function($stateParams, JobTeam) {
                    return JobTeam.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-team',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-team-detail.edit', {
            parent: 'job-team-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team/job-team-dialog.html',
                    controller: 'JobTeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeam', function(JobTeam) {
                            return JobTeam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team.new', {
            parent: 'job-team',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team/job-team-dialog.html',
                    controller: 'JobTeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                totalFiles: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-team', null, { reload: 'job-team' });
                }, function() {
                    $state.go('job-team');
                });
            }]
        })
        .state('job-team.edit', {
            parent: 'job-team',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team/job-team-dialog.html',
                    controller: 'JobTeamDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeam', function(JobTeam) {
                            return JobTeam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team', null, { reload: 'job-team' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team.delete', {
            parent: 'job-team',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team/job-team-delete-dialog.html',
                    controller: 'JobTeamDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobTeam', function(JobTeam) {
                            return JobTeam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team', null, { reload: 'job-team' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
