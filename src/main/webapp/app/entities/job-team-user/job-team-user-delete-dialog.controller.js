(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserDeleteController',JobTeamUserDeleteController);

    JobTeamUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobTeamUser'];

    function JobTeamUserDeleteController($uibModalInstance, entity, JobTeamUser) {
        var vm = this;

        vm.jobTeamUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobTeamUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
