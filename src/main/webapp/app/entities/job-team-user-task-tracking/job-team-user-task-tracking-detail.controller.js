(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserTaskTrackingDetailController', JobTeamUserTaskTrackingDetailController);

    JobTeamUserTaskTrackingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobTeamUserTaskTracking', 'User'];

    function JobTeamUserTaskTrackingDetailController($scope, $rootScope, $stateParams, previousState, entity, JobTeamUserTaskTracking, User) {
        var vm = this;

        vm.jobTeamUserTaskTracking = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:jobTeamUserTaskTrackingUpdate', function(event, result) {
            vm.jobTeamUserTaskTracking = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
