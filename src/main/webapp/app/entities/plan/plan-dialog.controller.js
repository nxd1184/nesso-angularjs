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
        .controller('PlanDialogController', PlanDialogController);

    PlanDialogController.$inject = ['$state', '$uibModalInstance', '$stateParams', 'planService', 'taskService', 'Team', 'moment'];

    function PlanDialogController($state, $uibModalInstance, $stateParams, planService, taskService, Team, moment) {

        var vm = this;
        vm.clear = clear;
        vm.job = {};
        vm.tasks = [];
        vm.selectedTasks = [];
        vm.selectedTeams = [];
        vm.teams = [];
        vm.sequenceTask = 1;
        vm.deadline;
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
        vm.onTeamRemoved = onTeamRemoved;

        vm.updatePlan = updatePlan;

        function openCalendar(field) {
            vm.datePickerOpenStatus[field] = true;
        }

        _loadPlanDetail();
        _loadTeams();

        function _loadPlanDetail() {
            planService.getJobPlanDetail($stateParams.id).then(function (result) {
                vm.job = result.job;

                if (!vm.job.jobTeams) {
                    vm.job.jobTeams = [];
                }
                _showCurrentTeams(vm.job.jobTeams);

                if (!vm.job.jobTasks) {
                    vm.job.jobTasks = [];
                }
                if(vm.job.deadline) {
                    vm.deadline = moment(LA.StringUtils.decode(vm.job.deadline)).toDate();
                }

                if (vm.job) {
                    _loadTasks(vm.job.projectId, vm.job.jobTasks);

                }
            });
        }

        function _loadTasks(projectId, currentTasks) {
            taskService.search({
                'projectId': projectId
            }).then(onSuccess);

            function onSuccess(result) {
                vm.tasks = result.data;
                if(currentTasks) {
                    for(var i = 0; i < currentTasks.length; i++) {
                        var jobTaskId = currentTasks[i].id;
                        var taskId = currentTasks[i].taskId;
                        _initStoredJobTask(taskId, jobTaskId);
                    }
                }
            }
        }

        function _initStoredJobTask(taskId, jobTaskId) {
            taskService.search({
                taskId: taskId
            }).then(function(result) {
                if(result.data) {
                    var taskResource = result.data[0];
                    taskResource.jobTaskId = jobTaskId;
                    vm.selectedTasks.push(taskResource);
                }
            })
        }

        function _showCurrentTeams(jobTeams) {
            if(jobTeams) {
                jobTeams.forEach(function(jobTeam) {
                    Team.get({id: jobTeam.teamId}, function(result) {
                       vm.selectedTeams.push(result);
                    });
                });
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
            var index = -1;
            for (var i = 0; i < vm.job.jobTeams.length; i++) {
                if (team.id == vm.job.jobTeams[i].id) {
                    index = i;
                    break;
                }
            }

            var jobTeam = {
                totalFiles: 0,
                jobId: vm.job.id,
                teamId: team.id,
                teamName: team.name,
                projectId: vm.job.projectId,
                jobTeamUsers: [],
                totalProcessingFiles: 0
            };

            if(jobTeam) {

            }

            if (team.members) {
                for (var i = 0; i < team.members.length; i++) {
                    var member = team.members[i];
                    jobTeam.jobTeamUsers.push({
                        userId: member.id,
                        name: member.lastName,
                        capacity: member.capacity,
                        totalFiles: member.capacity
                    });
                    jobTeam.totalFiles += member.capacity;
                }
            }


            if (index == -1) {
                vm.job.jobTeams.push(jobTeam);
            } else {
                vm.job.jobTeams[index] = jobTeam;
            }
        }

        function onTeamRemoved(team) {
            for (var i = 0; i < vm.job.jobTeams.length; i++) {
                if (team.id == vm.job.jobTeams[i].teamId) {
                    vm.job.jobTeams.splice(i, 1);
                    return;
                }
            }
        }

        function updatePlan() {

            var tasks = [];
            if(vm.selectedTasks) {
                for(var i = 0; i < vm.selectedTasks.length; i++) {
                    tasks.push({
                        id: vm.selectedTasks[i].jobTaskId,
                        taskId: vm.selectedTasks[i].id,
                        jobId: vm.job.id
                    });
                }
            }

            var deadline = LA.StringUtils.urlEncode(LA.StringUtils.toIso(vm.deadline));

            var params = {
                jobId: vm.job.id,
                deadline: deadline,
                tasks: tasks,
                teams: vm.job.jobTeams,
                customerRequirements: vm.job.customerRequirements,
                sequenceTask: vm.sequenceTask,
                totalFiles: vm.job.totalFiles
            };

            planService.update(params).then(function(result) {
                clear();
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
