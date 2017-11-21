(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserTaskTrackingDeleteController',JobTeamUserTaskTrackingDeleteController);

    JobTeamUserTaskTrackingDeleteController.$inject = ['$uibModalInstance', 'entity', 'JobTeamUserTaskTracking'];

    function JobTeamUserTaskTrackingDeleteController($uibModalInstance, entity, JobTeamUserTaskTracking) {
        var vm = this;

        vm.jobTeamUserTaskTracking = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JobTeamUserTaskTracking.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
