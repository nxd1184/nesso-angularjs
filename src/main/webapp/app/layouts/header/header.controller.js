(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$scope','$state', '$timeout', 'headerService', '$window', 'moment', '$interval', 'Principal'];

    function HeaderController($scope, $state, $timeout, headerService, $window, moment, $interval, Principal) {
        var vm = this;
        vm.notifications = [];
        vm.isNotFreelancer = _isNotFreelancer;

        function  getNotify() {
            headerService.getNotify().then(function (result) {
                console.log(result);
                vm.notifications = [];
                if (result.report == null) {
                    return;
                }

                result.report.forEach(function (item, index) {
                    var notification = {};
                    notification.index = index;
                    notification.fileName = item.fileName;
                    notification.time = moment(item.time).format("YYYY-MM-DD HH:mm:ss")
                    vm.notifications.push(notification);
                });
            });
        }
        $interval(getNotify, 60*1000);

        function _isNotFreelancer() {
            return !Principal.hasAnyAuthority(['ROLE_FREELANCER']);
        }

        getNotify();


        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
    }
})();
