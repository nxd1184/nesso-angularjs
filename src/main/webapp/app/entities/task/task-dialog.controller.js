(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TaskDialogController',TaskDialogController);

    TaskDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'Task', 'Project', 'StringUtils'];

    function TaskDialogController ($stateParams, $uibModalInstance, entity, Task, Project, StringUtils) {
        var vm = this;


        vm.selectedAuthority = {};

        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.task = entity;

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
            }
        ];

        if(!vm.task.status) {
            vm.task.status = 'ACTIVE';
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

            if(vm.selectedProject) {
                vm.task.projectId = vm.selectedProject.id;
            }

            if (vm.task.id !== null) {
                Task.update(vm.task, onSaveSuccess, onSaveError);
            } else {
                vm.task.langKey = 'en';
                Task.save(vm.task, onSaveSuccess, onSaveError);
            }
        }

        vm.datePickerOpenStatus.startDate = false;

        function openCalendar (field) {
            vm.datePickerOpenStatus[field] = true;
        }

        vm.projects = [];

        vm.selectedProject = {};

        _getAllProject();

        function _getAllProject() {
            Project.query({
                sort: ['id,desc']
            }, onSuccess, onError);

            function onSuccess(data) {
                vm.projects = data;
                if(vm.task.projectId && vm.projects) {
                    for(var i = 0; i < vm.projects.length; i++) {
                        var project = vm.projects[i];
                        if(project.id === vm.task.projectId) {
                            vm.selectedProject = project;
                            return;
                        }
                    }
                }
            }

            function onError(error) {

            }

        }

        vm.tasks = Task.query();
        vm.openQuickTeamCreationClass = false;
    }
})();
