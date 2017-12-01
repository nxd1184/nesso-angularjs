(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('profile', {
            parent: 'account',
            url: '/profile',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Profile'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/profile/profile.html',
                    controller: 'ProfileController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
