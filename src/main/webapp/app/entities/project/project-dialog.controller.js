(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ProjectDialogController', ProjectDialogController);

    ProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'User', 'AlertService'];

    function ProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Project, User, AlertService) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        User.query({}, function(data){
            vm.users = data;
            if (vm.project.managerId ) {
                vm.users.forEach(function (user) {
                    if (user.id === vm.project.managerId) {
                        vm.selectedUser = user;
                    }
                })
            } else {
                vm.selectedUser = null;
            }
        });



        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        if (!vm.project.id) {
            vm.project.status = "ACTIVE";
        }

        function save () {
            vm.isSaving = true;
            vm.project.type = vm.selectedType ? vm.selectedType.key: vm.project.type;
            vm.project.managerId =  vm.selectedUser ? vm.selectedUser.id : null;
            if (vm.project.id !== null) {
                Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
                Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError (error) {
            // AlertService.error(error.data);
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }


        vm.types = [
            {
                key: 'AUTO',
                name: 'Auto'
            },
            {
                key: 'MANUAL',
                name: 'Manual'
            }
        ]
        vm.selectedType = vm.types[1];

        vm.datePickerOpenStatus.startDate = false;

        function openCalendar (field) {
            vm.datePickerOpenStatus[field] = true;
        }
    }
})();
