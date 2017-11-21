(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamDialogController', JobTeamDialogController);

    JobTeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobTeam', 'Job', 'Team', 'Project'];

    function JobTeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobTeam, Job, Team, Project) {
        var vm = this;

        vm.jobTeam = entity;
        vm.clear = clear;
        vm.save = save;
        vm.jobs = Job.query();
        vm.teams = Team.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jobTeam.id !== null) {
                JobTeam.update(vm.jobTeam, onSaveSuccess, onSaveError);
            } else {
                JobTeam.save(vm.jobTeam, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:jobTeamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
