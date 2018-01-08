(function () {
    'use strict';

    /**
     * AngularJS default filter with the following expression:
     * We want to perform a OR.
     */
    angular
        .module('nessoApp').filter('propsFilter', function() {
        return function(items, props) {
            var out = [];

            if (angular.isArray(items)) {
                items.forEach(function(item) {
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
        .controller('PlanDialogController', PlanDialogController);

    PlanDialogController.$inject = ['$state', '$uibModalInstance', '$stateParams', 'planService', 'taskService', 'Team'];

    function PlanDialogController($state, $uibModalInstance, $stateParams, planService, taskService, Team) {

        var vm = this;
        vm.clear = clear;
        vm.job = {};
        vm.tasks = [];
        vm.selectedTasks = [];
        vm.selectedTeams = [];
        vm.teams = [];
        vm.sequenceTask = 1;
        vm.options = {
            orientation: 'horizontal',
            min: 1,
            max: 3,
            step: 1,
            range: 'min'
        };
        vm.showSimulate = showSimulate;
        vm.simulate = false;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.datePickerOpenStatus.deadline = false;

        vm.onTeamSelected = onTeamSelected;

        function openCalendar (field) {
            vm.datePickerOpenStatus[field] = true;
        }

        _loadJobDetail();
        _loadTeams();

        function _loadJobDetail() {
            planService.getJobPlanDetail($stateParams.id).then(function(result) {
                vm.job = result.job;
                vm.selectedTasks = result.job.jobTasks;
                if(!vm.job.jobTeams) {
                    vm.job.jobTeams = [];
                }
                if(vm.job) {
                    _loadTasks(vm.job.projectId);
                }
            });
        }

        function _loadTasks(projectId) {
            taskService.search({
                'projectId': projectId
            }).then(onSuccess, onError);

            function onSuccess(result) {
                vm.tasks = result.data;
            }

            function onError(error) {

            }
         }

        function _loadTeams() {
            Team.query({}, onSuccess, onError);

            function onSuccess(result) {
                vm.teams = result;
            }

            function onError(error) {

            }
        }


        function showSimulate() {
            vm.simulate = !vm.simulate;
        }

        function onTeamSelected(team) {
            var exist = false;
            for(var i = 0; i < vm.job.jobTeams.length; i++) {
                if(team.id == vm.job.jobTeams[i].id) {
                    exist = true;
                }
            }
            if(!exist) {
                var jobTeam = {
                    totalFiles: 0,
                    jobId: vm.job.id,
                    teamId: team.id,
                    teamName: team.name,
                    projectId: vm.job.projectId
                };
                vm.job.jobTeams.push(jobTeam);
            }
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
