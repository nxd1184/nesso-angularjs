(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('CheckInController', CheckInController);

    CheckInController.$inject = ['$scope','$state', '$timeout', 'jobTeamUserTaskService', 'moment', 'AlertService'];

    function CheckInController($scope, $state, $timeout, jobTeamUserTaskService, moment, AlertService) {

        var vm = this;

        vm.tasks = [];
        vm.checkIn = checkIn;
        vm.checkAll = checkAll;
        vm.showLastCheckInTime = showLastCheckInTime;

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

        function showLastCheckInTime(isoString) {
            if(!isoString) {
                return '';
            }
            return moment(isoString,'YYYY-MM-DD HH:mm:ss:SSS ZZ').format('DD/MM/YYYY hh:mm A');
        }

        function checkAll() {
            jobTeamUserTaskService.checkAll().then(onSuccess);

            function onSuccess(result) {
                AlertService.info(result.ids.length + ' was moved to TO_CHECK');
                _loadUnCheckInTasks();
                // $state.reload();
            }
        }

    }
})();
