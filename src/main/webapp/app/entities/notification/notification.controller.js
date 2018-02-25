(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('NotificationController', NotificationController);

    NotificationController.$inject = ['$scope','$state', '$timeout', 'headerService', '$window', 'moment'];

    function NotificationController($scope, $state, $timeout, headerService, $window, moment) {
        var vm = this;
        vm.notificationsToday = [];
        vm.notificationsYesterday = [];
        vm.notificationsLastSevenDay = [];

        function  getNotify() {
            headerService.getNotify().then(function (result) {
                console.log(result);
                vm.notificationsToday = [];
                vm.notificationsYesterday = [];
                vm.notificationsLastSevenDay = [];
                result.report.forEach(function (item, index) {
                    var notification = {};
                    var end = moment();
                    var start = moment(item.time);

                    var duration = moment.duration(end.diff(start));
                    var hour = duration.asHours();
                    var min = duration.asMinutes();

                    console.log(duration);

                    notification.index = index;
                    notification.fileName = item.fileName;
                    if(min < 1) {
                        notification.time = "Just now";
                    } else if (hour < 1) {
                        notification.time = min + "mins"
                    } else if (hour <= 24) {
                        notification.time = hour + "hours"
                    } else {
                        notification.time = start.format("YYYY-MM-DD HH:mm:ss")
                    }

                    if (hour <= 24) {
                        vm.notificationsToday.push(notification);
                    }
                    else if (hour <= 24*2){
                        vm.notificationsYesterday.push(notification);
                    } else {
                        vm.notificationsLastSevenDay.push(notification);
                    }

                });
            });
        }

        getNotify();
    }
})();
