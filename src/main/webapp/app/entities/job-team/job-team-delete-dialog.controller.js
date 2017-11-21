(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamDeleteController',JobTeamDeleteController);

    JobTeamDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobTeam'];

    function JobTeamDeleteController($uibModalInstance, entity, JobTeam) {
        var vm = this;

        vm.jobTeam = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobTeam.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
