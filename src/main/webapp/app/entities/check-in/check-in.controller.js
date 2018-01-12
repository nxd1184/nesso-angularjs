(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('CheckInController', CheckInController);

    CheckInController.$inject = ['$scope','$state', '$timeout', 'jobTeamUserTaskService'];

    function CheckInController($scope, $state, $timeout, jobTeamUserTaskService) {

        var vm = this;

        vm.tasks = [];
        vm.checkIn = checkIn;

        _loadUnCheckInTasks();

        function _loadUnCheckInTasks() {
            jobTeamUserTaskService.search({
                statusList: 'TODO,REWORK'
            }).then(onSuccess);

            function onSuccess(result) {
                vm.tasks = result;
            }
        }

        function checkIn(task, index) {
            jobTeamUserTaskService.checkIn({
                id: task.id
            }).then(onSuccess);

            function onSuccess(result) {
                vm.tasks.splice(index, 1);
            }
        }

    }
})();
