(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanDialogController', PlanDialogController);

    PlanDialogController.$inject = ['$state', 'entity', '$uibModalInstance'];

    function PlanDialogController($state, entity, $uibModalInstance) {

        var vm = this;

        console.log(entity);

        vm.clear = clear;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
