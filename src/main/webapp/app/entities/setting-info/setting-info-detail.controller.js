(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('SettingInfoDetailController', SettingInfoDetailController);

    SettingInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SettingInfo', 'UserSetting'];

    function SettingInfoDetailController($scope, $rootScope, $stateParams, previousState, entity, SettingInfo, UserSetting) {
        var vm = this;

        vm.settingInfo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('nessoApp:settingInfoUpdate', function(event, result) {
            vm.settingInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
