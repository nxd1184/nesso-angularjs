(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-team-user-task-tracking', {
            parent: 'entity',
            url: '/job-team-user-task-tracking?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeamUserTaskTrackings'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team-user-task-tracking/job-team-user-task-trackings.html',
                    controller: 'JobTeamUserTaskTrackingController',
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
        .state('job-team-user-task-tracking-detail', {
            parent: 'job-team-user-task-tracking',
            url: '/job-team-user-task-tracking/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeamUserTaskTracking'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team-user-task-tracking/job-team-user-task-tracking-detail.html',
                    controller: 'JobTeamUserTaskTrackingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JobTeamUserTaskTracking', function($stateParams, JobTeamUserTaskTracking) {
                    return JobTeamUserTaskTracking.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-team-user-task-tracking',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-team-user-task-tracking-detail.edit', {
            parent: 'job-team-user-task-tracking-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task-tracking/job-team-user-task-tracking-dialog.html',
                    controller: 'JobTeamUserTaskTrackingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeamUserTaskTracking', function(JobTeamUserTaskTracking) {
                            return JobTeamUserTaskTracking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team-user-task-tracking.new', {
            parent: 'job-team-user-task-tracking',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task-tracking/job-team-user-task-tracking-dialog.html',
                    controller: 'JobTeamUserTaskTrackingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-team-user-task-tracking', null, { reload: 'job-team-user-task-tracking' });
                }, function() {
                    $state.go('job-team-user-task-tracking');
                });
            }]
        })
        .state('job-team-user-task-tracking.edit', {
            parent: 'job-team-user-task-tracking',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task-tracking/job-team-user-task-tracking-dialog.html',
                    controller: 'JobTeamUserTaskTrackingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeamUserTaskTracking', function(JobTeamUserTaskTracking) {
                            return JobTeamUserTaskTracking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team-user-task-tracking', null, { reload: 'job-team-user-task-tracking' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team-user-task-tracking.delete', {
            parent: 'job-team-user-task-tracking',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task-tracking/job-team-user-task-tracking-delete-dialog.html',
                    controller: 'JobTeamUserTaskTrackingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobTeamUserTaskTracking', function(JobTeamUserTaskTracking) {
                            return JobTeamUserTaskTracking.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team-user-task-tracking', null, { reload: 'job-team-user-task-tracking' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
