(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobDetailController', JobDetailController);

    JobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Job', 'Project'];

    function JobDetailController($scope, $rootScope, $stateParams, previousState, entity, Job, Project) {
        var vm = this;

        vm.job = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:jobUpdate', function(event, result) {
            vm.job = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
