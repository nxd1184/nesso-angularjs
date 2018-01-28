(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'reportService', 'moment'];

    function HomeController ($scope, Principal, LoginService, $state, reportService, moment) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.productivityThisWeek = null;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getProductivityThisWeek();
        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        function getProductivityThisWeek() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getProductivityForThisWeek(params).then(function (result) {
                vm.productivityThisWeek = result;
            });
        }


    }
})();
