(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamDetailController', JobTeamDetailController);

    JobTeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobTeam', 'Job', 'Team', 'Project'];

    function JobTeamDetailController($scope, $rootScope, $stateParams, previousState, entity, JobTeam, Job, Team, Project) {
        var vm = this;

        vm.jobTeam = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:jobTeamUpdate', function(event, result) {
            vm.jobTeam = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
