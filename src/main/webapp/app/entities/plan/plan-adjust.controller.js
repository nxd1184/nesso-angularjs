(function () {
    'use strict';

    /**
     * AngularJS default filter with the following expression:
     * We want to perform a OR.
     */
    angular
        .module('nessoApp').filter('propsFilter', function () {
        return function (items, props) {
            var out = [];

            if (angular.isArray(items)) {
                items.forEach(function (item) {
                    var itemMatches = false;

                    var keys = Object.keys(props);
                    for (var i = 0; i < keys.length; i++) {
                        var prop = keys[i];
                        var text = props[prop].toLowerCase();
                        if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                            itemMatches = true;
                            break;
                        }
                    }

                    if (itemMatches) {
                        out.push(item);
                    }
                });
            } else {
                // Let the output be the input untouched
                out = items;
            }

            return out;
        };
    });

    angular
        .module('nessoApp')
        .controller('PlanAdjustController', PlanAdjustController);

    PlanAdjustController.$inject = ['$state', '$uibModalInstance', '$stateParams', 'planService', 'teamService', 'moment', 'AlertService', 'userService'];

    function PlanAdjustController($state, $uibModalInstance, $stateParams, planService, teamService, moment, AlertService, userService) {
        var vm = this;
        var jobTeamUserId = $stateParams.userId;
        var jobId = $stateParams.jobId;
        vm.clear = clear;
        vm.users = [];
        vm.job = {};
        vm.jobTeamUser;
        vm.searchUsers = searchUsers;
        vm.selectedUser = {};
        vm.onSelectUser = onSelectUser;
        vm.totalFilesAdjustment = 0;
        vm.save = save;

        _getUserJobDetail();
        function _getUserJobDetail() {
            planService.getUserJobDetail({
                jobTeamUserId: jobTeamUserId,
                jobId: jobId
            }).then(function(result) {
                vm.job = result.job;
                vm.jobTeamUser = result.jobTeamUser;
            });
        }

        _loadUsers();

        function _loadUsers() {
            searchUsers();
        }

        function searchUsers(searchTerm) {
            userService.search({
                searchTerm: searchTerm
            }).then(function(result) {
                vm.users = result.data;
            });
        }

        function onSelectUser() {
            if(vm.selectedUser.id === vm.jobTeamUser.userId) {
                AlertService.error('Can not assign to owner');
                vm.selectedUser = {};
                return;
            }
        }

        function save() {
            var data = {
                jobId: vm.job.id,
                jobTeamUserId: vm.jobTeamUser.id,
                toUserId: vm.selectedUser.id,
                totalFilesAdjustment: vm.totalFilesAdjustment
            }

            planService.adjustFiles(data).then(function(result) {
                $uibModalInstance.close(result);
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
