(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TaskDeleteController', TaskDeleteController);

    TaskDeleteController.$inject = ['$uibModalInstance', 'entity', 'Task'];

    function TaskDeleteController ($uibModalInstance, entity, Task) {
        var vm = this;

        vm.task = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (login) {
            Task.delete({login: login},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
