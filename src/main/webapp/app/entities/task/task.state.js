(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('task', {
                parent: 'admin',
                url: '/task?page&sort',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Task'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/task/tasks.html',
                        controller: 'TaskController',
                        controllerAs: 'vm'
                    }
                },            params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    }
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort)
                        };
                    }]
                }        })
            .state('task.new', {
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null, login: null, firstName: null, lastName: null, email: null,
                                    activated: true, langKey: null, createdBy: null, createdDate: null,
                                    lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                                    resetKey: null, authorities: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('task', null, { reload: true });
                    }, function() {
                        $state.go('task');
                    });
                }]
            })
            .state('task.edit', {
                url: '/{login}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/task/task-dialog.html',
                        controller: 'TaskDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Task', function(Task) {
                                return Task.get({login : $stateParams.login}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('task', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('task-detail', {
                parent: 'task',
                url: '/{login}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'nesso'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/task/task-detail.html',
                        controller: 'TaskDetailController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('task.delete', {
                url: '/{login}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/task/task-delete-dialog.html',
                        controller: 'TaskDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Task', function(Task) {
                                return Task.get({login : $stateParams.login}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('task', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }
})();
