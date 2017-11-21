(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('setting-info', {
            parent: 'entity',
            url: '/setting-info?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SettingInfos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/setting-info/setting-infos.html',
                    controller: 'SettingInfoController',
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
        .state('setting-info-detail', {
            parent: 'setting-info',
            url: '/setting-info/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SettingInfo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/setting-info/setting-info-detail.html',
                    controller: 'SettingInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SettingInfo', function($stateParams, SettingInfo) {
                    return SettingInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'setting-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('setting-info-detail.edit', {
            parent: 'setting-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting-info/setting-info-dialog.html',
                    controller: 'SettingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SettingInfo', function(SettingInfo) {
                            return SettingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('setting-info.new', {
            parent: 'setting-info',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting-info/setting-info-dialog.html',
                    controller: 'SettingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                key: null,
                                value: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('setting-info', null, { reload: 'setting-info' });
                }, function() {
                    $state.go('setting-info');
                });
            }]
        })
        .state('setting-info.edit', {
            parent: 'setting-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting-info/setting-info-dialog.html',
                    controller: 'SettingInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SettingInfo', function(SettingInfo) {
                            return SettingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('setting-info', null, { reload: 'setting-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('setting-info.delete', {
            parent: 'setting-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/setting-info/setting-info-delete-dialog.html',
                    controller: 'SettingInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SettingInfo', function(SettingInfo) {
                            return SettingInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('setting-info', null, { reload: 'setting-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
