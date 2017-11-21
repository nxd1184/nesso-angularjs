(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserTaskDetailController', JobTeamUserTaskDetailController);

    JobTeamUserTaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobTeamUserTask', 'JobTeamUser', 'User'];

    function JobTeamUserTaskDetailController($scope, $rootScope, $stateParams, previousState, entity, JobTeamUserTask, JobTeamUser, User) {
        var vm = this;

        vm.jobTeamUserTask = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:jobTeamUserTaskUpdate', function(event, result) {
            vm.jobTeamUserTask = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
