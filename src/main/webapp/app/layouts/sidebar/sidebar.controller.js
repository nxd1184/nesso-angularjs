(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$state', '$scope'];

    function SidebarController ($state, $scope) {
        $scope.menus = [
            {
                title: 'Dashboard',
                icon: 'fa fa-home',
                state: 'home',
                url: '#!/'
            }, {
                title: 'Plan',
                icon: 'fa fa-calendar',
                state: 'plans-status',
                url: '#!/plans-status'
            }, {
                title: 'Report',
                icon: 'fa fa-area-chart',
                state: 'report',
                url: '#!/report'
            }, {
                title: 'Folder',
                icon: 'fa fa-folder-open',
                state: 'folder',
                url: '#!/folder'
            }, {
                title: 'Setting',
                icon: 'fa fa-cog',
                state: 'user-setting',
                url: '#!/user-setting'
            }, {
                title: 'Check in',
                icon: 'fa fa-tasks',
                state: 'check-in',
                url: '#!/check-in'
            }, {
                title: 'Account',
                icon: 'fa fa-user-circle-o',
                subMenus: [
                    {
                        title: 'User',
                        icon: 'fa fa-user',
                        state: 'user-management',
                        url: '#!/user-management'
                    }, {
                        title: 'Team',
                        icon: 'fa fa-users',
                        state: 'team',
                        url: '#!/team'
                    }
                ]
            }, {
                title: 'Project',
                icon: 'fa fa-briefcase',
                subMenus: [
                    {
                        title: 'Project',
                        icon: 'fa fa-briefcase',
                        state: 'project',
                        url: '#!/project'
                    }, {
                        title: 'Task',
                        icon: 'fa fa-file',
                        state: 'task',
                        url: '#!/task'
                    }
                ]
            }
        ];


        $scope.currentState = $state.current.name;
    }
})();
