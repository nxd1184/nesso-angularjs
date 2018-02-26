(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$state', '$scope', 'Principal'];

    function SidebarController ($state, $scope, Principal) {

        $scope.account = null;

        $scope.menus = [
            {
                title: 'Dashboard',
                icon: 'fa fa-home',
                state: 'home',
                url: '#!/',
                roles: ['*']
            }, {
                title: 'Plan',
                icon: 'fa fa-calendar',
                state: 'plans-status',
                url: '#!/plans-status',
                roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
            }, {
                title: 'Report',
                icon: 'fa fa-area-chart',
                state: 'report',
                url: '#!/report',
                roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
            }, {
                title: 'Folder',
                icon: 'fa fa-folder-open',
                state: 'folder',
                url: '#!/folder',
                roles: ['ROLE_TEAM_LEADER','ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
            }, {
                title: 'Setting',
                icon: 'fa fa-cog',
                state: 'user-setting',
                url: '#!/user-setting',
                roles: ['ROLE_ADMIN']
            }, {
                title: 'Check in',
                icon: 'fa fa-tasks',
                state: 'check-in',
                url: '#!/check-in',
                roles: ['*']
            }, {
                title: 'Account',
                icon: 'fa fa-user-circle-o',
                roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN'],
                subMenus: [
                    {
                        title: 'User',
                        icon: 'fa fa-user',
                        state: 'user-management',
                        url: '#!/user-management',
                        roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
                    }, {
                        title: 'Team',
                        icon: 'fa fa-users',
                        state: 'team',
                        url: '#!/team',
                        roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
                    }
                ]
            }, {
                title: 'Project',
                icon: 'fa fa-briefcase',
                roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN'],
                subMenus: [
                    {
                        title: 'Project',
                        icon: 'fa fa-briefcase',
                        state: 'project',
                        url: '#!/project',
                        roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
                    }, {
                        title: 'Task',
                        icon: 'fa fa-file',
                        state: 'task',
                        url: '#!/task',
                        roles: ['ROLE_PROJECT_MANAGER', 'ROLE_ADMIN']
                    }
                ]
            }
        ];


        $scope.currentState = $state.current.name;

        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        $scope.hasPermission = function(roles) {
            if($scope.account) {
                if(roles) {
                    var index = -1;
                    index = roles.indexOf('*');
                    if(index != -1) {
                        return true;
                    }
                    var authorities = $scope.account.authorities;
                    if(authorities) {
                        for(var i = 0; i < roles.length; i++) {
                            var role = roles[i];
                            if(authorities.indexOf(role) != -1) {
                                return true;
                            }
                        }
                    }

                }
            }
            return false;
        }
    }
})();
