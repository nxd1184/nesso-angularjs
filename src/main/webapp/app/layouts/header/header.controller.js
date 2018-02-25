(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$scope','$state', '$timeout', 'headerService', '$window', 'moment', '$interval'];

    function HeaderController($scope, $state, $timeout, headerService, $window, moment, $interval) {
        var vm = this;
        vm.notifications = [];

        function  getNotify() {
            headerService.getNotify().then(function (result) {
                console.log(result);
                vm.notifications = [];
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

        getNotify();
    }
})();
