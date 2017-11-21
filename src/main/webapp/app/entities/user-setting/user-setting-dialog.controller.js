(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('UserSettingDialogController', UserSettingDialogController);

    UserSettingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserSetting', 'User'];

    function UserSettingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserSetting, User) {
        var vm = this;

        vm.userSetting = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userSetting.id !== null) {
                UserSetting.update(vm.userSetting, onSaveSuccess, onSaveError);
            } else {
                UserSetting.save(vm.userSetting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:userSettingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
