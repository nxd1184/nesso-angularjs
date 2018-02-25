(function () {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('notification', {
                parent: 'entity',
                url: '/notification',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_TEAM_LEADER', 'ROLE_PROJECT_MANAGER', 'ROLE_ADMIN', 'ROLE_QC'],
                    pageTitle: 'Notification'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/notification/notification.html',
                        controller: 'NotificationController',
                        controllerAs: 'vm'
                    }
                }
            })
        ;
    }

})();
