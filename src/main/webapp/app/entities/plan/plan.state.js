(function () {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('plan', {
                parent: 'entity',
                url: '/plan',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER'],
                    pageTitle: 'Plan Management'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/plan/plans.html',
                        controller: 'PlanController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('plan.edit', {
                parent: 'plan',
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
                        $state.go('project', null, { reload: 'project' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    }

})();
