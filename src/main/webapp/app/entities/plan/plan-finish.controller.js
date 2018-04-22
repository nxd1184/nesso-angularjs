(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanFinishController', PlanFinishController);

    PlanFinishController.$inject = ['$state', '$uibModalInstance', '$stateParams', 'planService', 'AlertService', 'Job'];

    function PlanFinishController($state, $uibModalInstance, $stateParams, planService, AlertService, Job) {
        var vm = this;
        var jobId = $stateParams.jobId;
        vm.clear = clear;
        vm.finish = finish;
        vm.job = null;

        Job.get({id : jobId}, function success(result) {
            vm.job = result;
        });

        function finish() {
            var data = {
                jobId: vm.job.id
            };

            planService.finish(data).then(function(result) {
                if(result.totalUnDoneTasks == 0) {
                    $uibModalInstance.close(result);
                }else {
                    AlertService.error(result.totalUnDoneTasks + " have not been done");
                }

            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
