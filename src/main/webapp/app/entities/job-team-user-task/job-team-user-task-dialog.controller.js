(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserTaskDialogController', JobTeamUserTaskDialogController);

    JobTeamUserTaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobTeamUserTask', 'JobTeamUser', 'User'];

    function JobTeamUserTaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobTeamUserTask, JobTeamUser, User) {
        var vm = this;

        vm.jobTeamUserTask = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.jobteamusers = JobTeamUser.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jobTeamUserTask.id !== null) {
                JobTeamUserTask.update(vm.jobTeamUserTask, onSaveSuccess, onSaveError);
            } else {
                JobTeamUserTask.save(vm.jobTeamUserTask, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:jobTeamUserTaskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.lastCheckInTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
