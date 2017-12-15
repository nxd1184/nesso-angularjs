(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'User'];

    function TeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Team, User) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.save = save;


        vm.selectedTeamLeader = {};

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        _loadTeamMembers();

        function _loadTeamMembers() {

            User.query({}, onSuccess);

            function onSuccess(data) {
                vm.users = data;

                if(vm.team.id) {
                    for(var i = 0; i < vm.users.length; i++) {
                        var user = vm.users[i];
                        if(user.id === vm.team.leaderId) {
                            vm.selectedTeamLeader = user;
                            break;
                        }
                    }
                }
            }


        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;

            if(vm.selectedTeamLeader) {
                vm.team.leaderId = vm.selectedTeamLeader.id;
            }

            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        if(!vm.team.status) {
            vm.team.status = 'ACTIVE';
        }
    }
})();
