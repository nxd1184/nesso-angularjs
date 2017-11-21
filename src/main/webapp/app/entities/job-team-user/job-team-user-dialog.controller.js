(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserDialogController', JobTeamUserDialogController);

    JobTeamUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JobTeamUser', 'JobTeam', 'User'];

    function JobTeamUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JobTeamUser, JobTeam, User) {
        var vm = this;

        vm.jobTeamUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.jobteams = JobTeam.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.jobTeamUser.id !== null) {
                JobTeamUser.update(vm.jobTeamUser, onSaveSuccess, onSaveError);
            } else {
                JobTeamUser.save(vm.jobTeamUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:jobTeamUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
