(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('folder', {
            parent: 'entity',
            url: '/folder',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'Folder'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/folder/folder.html',
                    controller: 'FolderController',
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
        /*.state('user-setting-detail', {
            parent: 'user-setting',
            url: '/user-setting/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'UserSetting'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-setting/user-setting-detail.html',
                    controller: 'UserSettingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UserSetting', function($stateParams, UserSetting) {
                    return UserSetting.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-setting',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-setting-detail.edit', {
            parent: 'user-setting-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-setting/user-setting-dialog.html',
                    controller: 'UserSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserSetting', function(UserSetting) {
                            return UserSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-setting.new', {
            parent: 'user-setting',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-setting/user-setting-dialog.html',
                    controller: 'UserSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                auto: null,
                                type: null,
                                active: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-setting', null, { reload: 'user-setting' });
                }, function() {
                    $state.go('user-setting');
                });
            }]
        })
        .state('user-setting.edit', {
            parent: 'user-setting',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-setting/user-setting-dialog.html',
                    controller: 'UserSettingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserSetting', function(UserSetting) {
                            return UserSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-setting', null, { reload: 'user-setting' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-setting.delete', {
            parent: 'user-setting',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-setting/user-setting-delete-dialog.html',
                    controller: 'UserSettingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserSetting', function(UserSetting) {
                            return UserSetting.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-setting', null, { reload: 'user-setting' });
                }, function() {
                    $state.go('^');
                });
            }]
        });*/
    }

})();
