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
                state: 'plan'
            }, {
                title: 'Report',
                icon: 'fa fa-area-chart',
                state: 'report'
            }, {
                title: 'Folder',
                icon: 'fa fa-folder-open',
                state: 'folder'
            }, {
                title: 'Setting',
                icon: 'fa fa-cog',
                state: 'setting'
            }, {
                title: 'Check in',
                icon: 'fa fa-tasks',
                state: 'check-in'
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
                        state: 'project'
                    }, {
                        title: 'Task',
                        icon: 'fa fa-file',
                        state: 'task',
                        url: '#!/task-management'
                    }
                ]
            }
        ];


        $scope.currentState = $state.current.name;
    }
})();
