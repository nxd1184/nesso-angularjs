(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanController', PlanController);

    PlanController.$inject = ['$state'];

    function PlanController($state) {

        var vm = this;

        vm.filters = ['Task Code','Project Code'];

    }
})();
