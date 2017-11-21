(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserTaskTrackingDialogController', JobTeamUserTaskTrackingDialogController);

    JobTeamUserTaskTrackingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobTeamUserTaskTracking', 'User'];

    function JobTeamUserTaskTrackingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobTeamUserTaskTracking, User) {
        var vm = this;

        vm.jobTeamUserTaskTracking = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jobTeamUserTaskTracking.id !== null) {
                JobTeamUserTaskTracking.update(vm.jobTeamUserTaskTracking, onSaveSuccess, onSaveError);
            } else {
                JobTeamUserTaskTracking.save(vm.jobTeamUserTaskTracking, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:jobTeamUserTaskTrackingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
