(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Task', 'Project'];

    function TaskDetailController($scope, $rootScope, $stateParams, previousState, entity, Task, Project) {
        var vm = this;

        vm.task = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:taskUpdate', function(event, result) {
            vm.task = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
