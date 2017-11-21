(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('SettingInfoDialogController', SettingInfoDialogController);

    SettingInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SettingInfo', 'UserSetting'];

    function SettingInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SettingInfo, UserSetting) {
        var vm = this;

        vm.settingInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usersettings = UserSetting.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.settingInfo.id !== null) {
                SettingInfo.update(vm.settingInfo, onSaveSuccess, onSaveError);
            } else {
                SettingInfo.save(vm.settingInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nessoApp:settingInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
