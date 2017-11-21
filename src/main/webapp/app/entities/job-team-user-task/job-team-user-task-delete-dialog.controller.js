(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserTaskDeleteController',JobTeamUserTaskDeleteController);

    JobTeamUserTaskDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobTeamUserTask'];

    function JobTeamUserTaskDeleteController($uibModalInstance, entity, JobTeamUserTask) {
        var vm = this;

        vm.jobTeamUserTask = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobTeamUserTask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
