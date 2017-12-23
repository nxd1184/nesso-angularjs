(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'User', 'teamService'];

    function TeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Team, User, teamService) {
        var vm = this;

        vm.team = entity;
        vm.workingShifts = ['Day','Night','Freelance'];
        vm.selectedShift = null;
        vm.selectedMember = {};
        vm.clear = clear;
        vm.save = save;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.selectedTeamLeader = {};

        vm.edit = edit;
        vm.changeStatus = changeStatus;
        vm.remove = remove;
        vm.showChangeStatusAction = showChangeStatusAction;

        vm.status = 'ACTIVE';
        vm.startDate = null;
        vm.endDate = null;
        vm.onSelectMember = onSelectMember;
        vm.updateMember = updateMember;
        vm.updateTeam = updateTeam;

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

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (field) {
            vm.datePickerOpenStatus[field] = true;
        }

        function edit(member) {
            vm.selectedMember = member;
            onSelectMember(member);
        }

        function changeStatus(member) {
            if(member.status === 'ACTIVE') {
                member.status = 'INACTIVE';
            }else {
                member.status = 'ACTIVE';
            }
        }

        function remove(index) {
            vm.team.members.splice(index, 1);
        }

        function showChangeStatusAction(member) {
            if(member.status === 'ACTIVE') {
                return 'Disable';
            }
            return 'Enable';
        }

        function onSelectMember(member) {
            vm.status = member.status;
            if(member.startDate) {
                vm.startDate = moment(member.startDate).toDate();
            }

            if(member.endDate) {
                vm.endDate = moment(member.endDate).toDate();
            }
        }

        function updateMember() {
            if(vm.selectedMember && vm.selectedMember.id) {

                vm.selectedMember.shit = vm.selectedShift;
                vm.selectedMember.startDate = vm.startDate;
                vm.selectedMember.endDate = vm.endDate;
                vm.selectedMember.status = vm.status;

                var memberExists = false;
                for(var i = 0; i < vm.team.members.length; i++) {
                    if(vm.team.members[i].id === vm.selectedMember.id) {
                        memberExists = true;
                        vm.team.members[i] = vm.selectedMember;
                        break;
                    }
                }

                if(!memberExists) {
                    vm.team.members.push(vm.selectedMember);
                }

                vm.selectedMember = null;
                vm.selectedShift = null;
                vm.status = 'ACTIVE';
                vm.startDate = null;
                vm.endDate = null;
            }
        }

        function updateTeam() {
            teamService.update(vm.team).then(onSuccess,onError);

            function onSuccess(result) {
                onSaveSuccess(result);
            }

            function onError(error) {

            }
        }
    }
})();
