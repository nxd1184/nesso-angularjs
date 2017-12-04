(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ProfileController', ProfileController);

    ProfileController.$inject = ['Principal', 'Auth', 'AlertService', '$scope'];

    function ProfileController (Principal, Auth, AlertService, $scope) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                startDate: account.startDate,
                team: account.teamName,
                status: account.status,
                authorities: account.authorities
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                    AlertService.success("Success! You have changed your basic info.");
                    $scope.$emit('accountUpdated');
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }

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
            },{
                id: 'ROLE_ADMIN',
                name: 'Administrator'
            }
        ];

        function showAuthorities(account) {
            var roles = '';
            if(account && account.authorities) {

                for(var i = 0; i < account.authorities.length; i++) {

                    for(var j = 0; j < vm.authorities.length; j++) {
                        var role = vm.authorities[j];
                        if(role.id === account.authorities[i]) {
                            roles += vm.authorities[j].name;
                            if(i < account.authorities.length - 1) {
                                roles += ', ';
                            }
                        }
                    }
                }
            }
            return roles;
        }

        vm.showAuthorities = showAuthorities;

        vm.currentTab = 1;

        vm.setTab = setTab;

        function setTab(index) {
            vm.currentTab = index;
        }


        vm.changePassword = changePassword;

        function changePassword(currentPassword, newPassword, confirmPassword) {

            if(!currentPassword) {
                AlertService.error("Current password is required");
                return;
            }

            if(!newPassword) {
                AlertService.error("New password is required");
                return;
            }

            if(!confirmPassword) {
                AlertService.error("Confirm password is required");
                return;
            }

            if(newPassword !== confirmPassword) {
                AlertService.error("Confirm password does not match");
                return;
            }

            Auth.changePassword(currentPassword,newPassword,confirmPassword).then(function() {
                AlertService.success("Success! Your password is updated");
            }, function(error) {
                AlertService.error(error.data);
            });

        }
    }
})();
