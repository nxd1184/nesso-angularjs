(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('UserSettingDetailController', UserSettingDetailController);

    UserSettingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserSetting', 'User'];

    function UserSettingDetailController($scope, $rootScope, $stateParams, previousState, entity, UserSetting, User) {
        var vm = this;

        vm.userSetting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:userSettingUpdate', function(event, result) {
            vm.userSetting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
