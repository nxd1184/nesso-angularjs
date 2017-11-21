(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job-team-user-task', {
            parent: 'entity',
            url: '/job-team-user-task?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeamUserTasks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team-user-task/job-team-user-tasks.html',
                    controller: 'JobTeamUserTaskController',
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
        .state('job-team-user-task-detail', {
            parent: 'job-team-user-task',
            url: '/job-team-user-task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JobTeamUserTask'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job-team-user-task/job-team-user-task-detail.html',
                    controller: 'JobTeamUserTaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JobTeamUserTask', function($stateParams, JobTeamUserTask) {
                    return JobTeamUserTask.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'job-team-user-task',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('job-team-user-task-detail.edit', {
            parent: 'job-team-user-task-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task/job-team-user-task-dialog.html',
                    controller: 'JobTeamUserTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeamUserTask', function(JobTeamUserTask) {
                            return JobTeamUserTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team-user-task.new', {
            parent: 'job-team-user-task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task/job-team-user-task-dialog.html',
                    controller: 'JobTeamUserTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                originalFileName: null,
                                originalFilePath: null,
                                status: null,
                                fileName: null,
                                filePath: null,
                                numberOfRework: null,
                                lastCheckInTime: null,
                                qcEdit: null,
                                rework: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('job-team-user-task', null, { reload: 'job-team-user-task' });
                }, function() {
                    $state.go('job-team-user-task');
                });
            }]
        })
        .state('job-team-user-task.edit', {
            parent: 'job-team-user-task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task/job-team-user-task-dialog.html',
                    controller: 'JobTeamUserTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JobTeamUserTask', function(JobTeamUserTask) {
                            return JobTeamUserTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team-user-task', null, { reload: 'job-team-user-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job-team-user-task.delete', {
            parent: 'job-team-user-task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job-team-user-task/job-team-user-task-delete-dialog.html',
                    controller: 'JobTeamUserTaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JobTeamUserTask', function(JobTeamUserTask) {
                            return JobTeamUserTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job-team-user-task', null, { reload: 'job-team-user-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
