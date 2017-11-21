(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', 'Auth', '$state', 'Principal'];

    function ApplicationController ($scope, Auth, $state, Principal) {

        $scope.settings = {
            layout: {}
        };

        $scope.settings.layout.loginPage = true;
        $scope.settings.layout.defaultPage = false;

        $scope.$on('showLoginPage', function(events, isShow) {
            _onLoginPage(isShow);
        });

        function _onLoginPage(isLoginPage) {

            $scope.settings.layout.loginPage = isLoginPage;
            $scope.settings.layout.defaultPage = !isLoginPage;
        }

        $scope.logout = function() {
            Auth.logout();
            $state.go('login');
        };

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                _onLoginPage(!account);
                if(!account) {
                    $state.go('login');
                }
            });
        }

        $scope.$state = $state;
    }
})();
