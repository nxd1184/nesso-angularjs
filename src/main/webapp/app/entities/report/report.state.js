(function () {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('report', {
                parent: 'entity',
                url: '/report',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_PROJECT_MANAGER', 'ROLE_TEAM_LEADER'],
                    pageTitle: 'Report'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/report/report.html',
                        controller: 'ReportController',
                        controllerAs: 'vm'
                    }
                }
            })
            ;
    }

})();
