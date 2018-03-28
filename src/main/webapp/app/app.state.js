(function() {
    'use strict';

    angular
        .module('nessoApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ]
            }
        })
        .state('login', {
            parent: 'app',
            url: '/login',
            data: {
                authorities: []
            },
            views: {
                'login_page@': {
                    templateUrl: 'app/components/login/login.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                }
            },
        });
    }
})();
