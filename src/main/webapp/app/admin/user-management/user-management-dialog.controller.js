(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'Team', 'StringUtils'];

    function UserManagementDialogController ($stateParams, $uibModalInstance, entity, User, Team, StringUtils) {
        var vm = this;


        vm.selectedAuthorities = [];

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.authorities = [
            {
                id: 'ROLE_USER',
                name: 'User'
            }, {
                id: 'ROLE_PROJECT_MANAGER',
                name: 'Project Manager'
            },{
                id: 'ROLE_TEAM_LEADER',
                name: 'Team Leader'
            }, {
                id: 'ROLE_QC',
                name: 'QC'
            }, {
                id: 'ROLE_FREELANCER',
                name: 'Freelancer'
            }
        ];

        if(!vm.user.status) {
            vm.user.status = 'ACTIVE';
        }

        if(vm.user.authorities) {
            for(var i = 0; i < vm.user.authorities.length; i++) {
                var authority = vm.user.authorities[i];
                for(var j = 0; j < vm.authorities.length; j++) {
                    var redefinedAuthority = vm.authorities[j];
                    if(authority === redefinedAuthority.id) {
                        vm.selectedAuthorities.push(redefinedAuthority);
                    }
                }
            }
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;

            if(vm.selectedTeam) {
                vm.user.teamId = vm.selectedTeam.id;
            }


            if(vm.selectedAuthorities) {
                vm.user.authorities = [];

                for(var i = 0; i < vm.selectedAuthorities.length; i++) {
                    vm.user.authorities.push(vm.selectedAuthorities[i].id);
                }
            }


            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                vm.user.langKey = 'en';
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }

        vm.datePickerOpenStatus.startDate = false;

        function openCalendar (field) {
            vm.datePickerOpenStatus[field] = true;
        }

        vm.teams = [];

        vm.selectedTeam = {};

        _getAllTeam();

        function _getAllTeam() {

            Team.getAllTeam(onSuccess, onError);

            function onSuccess(data) {
                vm.teams = data.teams;
                if(vm.user.teamId && vm.teams) {
                    for(var i = 0; i < vm.teams.length; i++) {
                        var team = vm.teams[i];
                        if(team.id === vm.user.teamId) {
                            vm.selectedTeam = team;
                            return;
                        }
                    }
                }
            }

            function onError(error) {

            }

        }

        vm.users = User.query();
        vm.selectedTeamLeader = {};
        vm.teamName = '';
        vm.teamNameError = false;
        vm.addNewTeam = addNewTeam;
        vm.openQuickTeamCreationClass = false;
        vm.createTeamSuccess = false;
        function closeQuickTeamCreation() {
            vm.openQuickTeamCreationClass = false;
        }

        function initQuickTeamCreationVariable() {
            vm.selectedTeamLeader = {};
            vm.teamName = '';
            vm.teamNameError = false;
        }

        function addNewTeam() {
            if(!StringUtils.isNotBlank(vm.teamName)) {
                vm.teamNameError = true;
                return;
            }

            var team = {
                name: vm.teamName,
                status: 'ACTIVE'
            };
            if(vm.selectedTeamLeader.id) {
                team.leaderId = vm.selectedTeamLeader.id;
            }

            Team.save(team, onSuccess, onError);

            function onSuccess(data) {
                _getAllTeam();
                closeQuickTeamCreation();
                initQuickTeamCreationVariable();
                vm.createTeamSuccess = true;
            }

            function onError(error) {

            }
        }
    }
})();
