(function () {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('check-in', {
                parent: 'entity',
                url: '/check-in',
                data: {
                    authorities: ['ROLE_FREELANCER'],
                    pageTitle: 'Check in Management'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/check-in/check-in.html',
                        controller: 'CheckInController',
                        controllerAs: 'vm'
                    }
                }
            });
    }

})();
