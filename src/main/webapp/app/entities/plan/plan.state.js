(function() {
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
                    authorities: ['ROLE_USER'],
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
            .state('plan.new', {
                parent: 'plan',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/plan/plan-dialog.html',
                        controller: 'PlanDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {

                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('plan', {}, { reload: 'plan' });
                    }, function() {
                        $state.go('plan');
                    });
                }]
            });
    }

})();
