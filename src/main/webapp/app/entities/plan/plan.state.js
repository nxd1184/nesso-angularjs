(function () {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('plans-status', {
                parent: 'entity',
                url: '/plans-status',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER'],
                    pageTitle: 'Plan Management'
                },
                params: {
                    filterBy: null,
                    filterValue: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/plan/plans-status.html',
                        controller: 'PlanStatusController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('plans-status.edit', {
                parent: 'plans-status',
                url: '/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$uibModalStack', function ($stateParams, $state, $uibModal, $uibModalStack) {
                    var uibModal = $uibModal.open({
                        templateUrl: 'app/entities/plan/plan-dialog.html',
                        windowClass: 'popupModal modal-scroll',
                        controller: 'PlanDialogController',
                        controllerAs: 'vm'
                    });

                    uibModal.rendered.then(function () {
                        $uibModalStack.getTop().value.modalDomEl.attr('id', 'addJobModal');
                    });

                    uibModal.result.then(function() {
                        $state.go('plans-status', null, { reload: 'plans-status' });
                    }, function() {
                        $state.go('plans-status', null, { reload: 'plans-status' });
                    });
                }]
            })
            .state('plans-status.adjust', {
                parent: 'plans-status',
                url: '/adjust/{userId}/{jobId}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$uibModalStack', function ($stateParams, $state, $uibModal, $uibModalStack) {
                    var uibModal = $uibModal.open({
                        templateUrl: 'app/entities/plan/plan-adjust.html',
                        controller: 'PlanAdjustController',
                        controllerAs: 'vm'
                    });

                    uibModal.rendered.then(function () {
                        $uibModalStack.getTop().value.modalDomEl.attr('id', 'addJobModal');
                    });

                    uibModal.result.then(function() {
                        $state.go('plans-status', null, { reload: 'plans-status' });
                    }, function() {
                        $state.go('plans-status', null, { reload: 'plans-status' });
                    });
                }]
            })
            .state('plans-status.finish', {
                parent: 'plans-status',
                url: '/finish/{jobId}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$uibModalStack', function ($stateParams, $state, $uibModal, $uibModalStack) {
                    var uibModal = $uibModal.open({
                        templateUrl: 'app/entities/plan/plan-finish.html',
                        controller: 'PlanFinishController',
                        controllerAs: 'vm'
                    });

                    uibModal.rendered.then(function () {
                        $uibModalStack.getTop().value.modalDomEl.attr('id', 'addJobModal');
                    });

                    uibModal.result.then(function() {
                        $state.go('plans-status', null, { reload: 'plans-status' });
                    }, function() {
                        $state.go('plans-status', null, { reload: 'plans-status' });
                    });
                }]
            })
            .state('plans-timeline', {
                parent: 'entity',
                url: '/plans-timeline',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER'],
                    pageTitle: 'Plan Management'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/plan/plans-timeline.html',
                        controller: 'PlanTimelineController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('plans-timeline.edit', {
                parent: 'plans-timeline',
                url: '/edit/{id}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$uibModalStack', function ($stateParams, $state, $uibModal, $uibModalStack) {
                    var uibModal = $uibModal.open({
                        templateUrl: 'app/entities/plan/plan-dialog.html',
                        windowClass: 'popupModal modal-scroll',
                        controller: 'PlanDialogController',
                        controllerAs: 'vm'
                    });

                    uibModal.rendered.then(function () {
                        $uibModalStack.getTop().value.modalDomEl.attr('id', 'addJobModal');
                    });

                    uibModal.result.then(function() {
                        $state.go('plans-timeline', null, { reload: 'plans-timeline' });
                    }, function() {
                        $state.go('plans-timeline', null, { reload: 'plans-timeline' });
                    });
                }]
            })
            .state('plans-timeline.adjust', {
                parent: 'plans-timeline',
                url: '/adjust/{userId}/{jobId}',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$uibModalStack', function ($stateParams, $state, $uibModal, $uibModalStack) {
                    var uibModal = $uibModal.open({
                        templateUrl: 'app/entities/plan/plan-adjust.html',
                        controller: 'PlanAdjustController',
                        controllerAs: 'vm'
                    });

                    uibModal.rendered.then(function () {
                        $uibModalStack.getTop().value.modalDomEl.attr('id', 'addJobModal');
                    });

                    uibModal.result.then(function() {
                        $state.go('plans-timeline', null, { reload: 'plans-timeline' });
                    }, function() {
                        $state.go('plans-timeline', null, { reload: 'plans-timeline' });
                    });
                }]
            });
    }

})();
