(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('JobTeamUserDetailController', JobTeamUserDetailController);

    JobTeamUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JobTeamUser', 'JobTeam', 'User'];

    function JobTeamUserDetailController($scope, $rootScope, $stateParams, previousState, entity, JobTeamUser, JobTeam, User) {
        var vm = this;

        vm.jobTeamUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:jobTeamUserUpdate', function(event, result) {
            vm.jobTeamUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
