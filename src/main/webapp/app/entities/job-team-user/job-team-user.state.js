(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-team-user', {
            parent: 'entity',
            url: '/job-team-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeamUsers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team-user/job-team-users.html',
                    controller: 'JobTeamUserController',
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
        .state('job-team-user-detail', {
            parent: 'job-team-user',
            url: '/job-team-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeamUser'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team-user/job-team-user-detail.html',
                    controller: 'JobTeamUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JobTeamUser', function($stateParams, JobTeamUser) {
                    return JobTeamUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-team-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-team-user-detail.edit', {
            parent: 'job-team-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user/job-team-user-dialog.html',
                    controller: 'JobTeamUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeamUser', function(JobTeamUser) {
                            return JobTeamUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team-user.new', {
            parent: 'job-team-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user/job-team-user-dialog.html',
                    controller: 'JobTeamUserDialogController',
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
                    $state.go('job-team-user', null, { reload: 'job-team-user' });
                }, function() {
                    $state.go('job-team-user');
                });
            }]
        })
        .state('job-team-user.edit', {
            parent: 'job-team-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user/job-team-user-dialog.html',
                    controller: 'JobTeamUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeamUser', function(JobTeamUser) {
                            return JobTeamUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team-user', null, { reload: 'job-team-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team-user.delete', {
            parent: 'job-team-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user/job-team-user-delete-dialog.html',
                    controller: 'JobTeamUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobTeamUser', function(JobTeamUser) {
                            return JobTeamUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team-user', null, { reload: 'job-team-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
