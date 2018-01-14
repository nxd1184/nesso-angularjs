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
                    authorities: ['ROLE_USER', 'ROLE_TEAM_LEADER', 'ROLE_PROJECT_MANAGER', 'ROLE_ADMIN', 'ROLE_QC'],
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
