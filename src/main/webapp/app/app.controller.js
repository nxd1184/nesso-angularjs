(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', 'Auth', '$state', 'Principal', '$rootScope'];

    function ApplicationController ($scope, Auth, $state, Principal, $rootScope) {

        $rootScope.scriptsLoaded = false;

        $scope.account = null;

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
                }else{
                    $scope.account = account;
                    $scope.account.fullName = $scope.showFullName();
                }
            });
        }

        $scope.$state = $state;

        $scope.$on('$viewContentLoaded', function() {
            if(!$rootScope.scriptsLoaded) {
                Metronic.init(); // init metronic core components
                // Layout.init(); // init layout
                Demo.init(); // init demo features
                Index.init(); // init index page
                ComponentsPickers.init();

                $rootScope.scriptsLoaded = true;
            }
        });

        $scope.$on('accountUpdated', function() {
            getAccount();
        });

        $scope.showFullName = function(){
            var fullName = '';
            if($scope.account) {
                fullName = $scope.account.firstName;
                if(!fullName && $scope.account.lastName) {
                    fullName = $scope.account.lastName;
                }else {
                    if($scope.account.lastName) {
                        fullName += ' ' + $scope.account.lastName;
                    }
                }
            }
            return fullName;
        }
    }
})();
