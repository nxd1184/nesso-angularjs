(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobDialogController', JobDialogController);

    JobDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Job', 'Project'];

    function JobDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Job, Project) {
        var vm = this;

        vm.job = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.job.id !== null) {
                Job.update(vm.job, onSaveSuccess, onSaveError);
            } else {
                Job.save(vm.job, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:jobUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.deadline = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
