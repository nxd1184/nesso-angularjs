(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('UserSettingDeleteController',UserSettingDeleteController);

    UserSettingDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserSetting'];

    function UserSettingDeleteController($uibModalInstance, entity, UserSetting) {
        var vm = this;

        vm.userSetting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserSetting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
