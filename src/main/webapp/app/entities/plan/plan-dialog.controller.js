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

    PlanDialogController.$inject = ['$state', '$uibModalInstance', '$stateParams', 'planService', 'taskService', 'teamService', 'moment', 'AlertService'];

    function PlanDialogController($state, $uibModalInstance, $stateParams, planService, taskService, teamService, moment, AlertService) {


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

        vm.types = ['OFFICIAL','OT','DELEGATION'];

        vm.showSimulate = showSimulate;
        vm.simulate = false;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        vm.datePickerOpenStatus.deadline = false;

        vm.onTeamSelected = onTeamSelected;
        vm.onTeamRemoved = onTeamRemoved;

        vm.updatePlan = updatePlan;

        vm.onTaskSelected = onTaskSelected;
        vm.onTaskRemoved = onTaskRemoved;

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
                    vm.totalCredit += taskResource.taskCredit;
                }
            })
        }

        function _showCurrentTeams(jobTeams) {
            if(jobTeams) {
                for(var i = 0; i < jobTeams.length; i++) {
                    var jobTeam = jobTeams[i];
                    teamService.search({
                        teamId: jobTeam.teamId
                    }).then(function(result) {
                        if(result.data && result.data.length) {
                            vm.selectedTeams.push(result.data[0]);
                        }
                    });
                }
            }
        }

        function _loadTeams() {
            teamService.search({}).then(onSuccess, onError);

            function onSuccess(result) {
                vm.teams = result.data;
            }

            function onError(error) {

            }
        }


        function showSimulate() {
            vm.simulate = !vm.simulate;
        }

        function onTeamSelected(team) {

            if(vm.job.started) {
                AlertService.error('Can not add new team when job is started');
                vm.selectedTeams.pop();
                return;
            }

            var exist = false;
            var index = -1;
            for (var i = 0; i < vm.job.jobTeams.length; i++) {
                if (team.id == vm.job.jobTeams[i].id) {
                    index = i;
                    exist = true;
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


            if (team.members) {
                for (var i = 0; i < team.members.length; i++) {
                    var member = team.members[i];
                    jobTeam.jobTeamUsers.push({
                        userId: member.id,
                        name: member.lastName,
                        capacity: member.capacity,
                        authorities: member.authorities,
                        totalFiles: 0
                    });
                }
            }


            if (index == -1) {
                vm.job.jobTeams.push(jobTeam);
            } else {
                vm.job.jobTeams[index] = jobTeam;
            }

            _buildUserAssignments();
        }

        function onTeamRemoved(team) {
            if(vm.job.started) {
                AlertService.error('Can not remove team when job is started');
                vm.selectedTeams.push(team);
                return;
            }
            for (var i = 0; i < vm.job.jobTeams.length; i++) {
                if (team.id == vm.job.jobTeams[i].teamId) {
                    vm.job.jobTeams.splice(i, 1);
                    break;
                }
            }
            _buildUserAssignments();
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
                totalFiles: vm.job.totalFiles,
                type: vm.job.type
            };

            planService.update(params).then(function(result) {
                clear();
            });
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function onTaskSelected(task){

            if(vm.job.started) {
                AlertService.error('Can not add new task when job is started');
                vm.selectedTasks.pop();
                return;
            }

            _buildUserAssignments();
        }

        function onTaskRemoved(task) {

            if(vm.job.started) {
                AlertService.error('Can not remove task when job is started');
                vm.selectedTasks.push(task);
                return;
            }

            _buildUserAssignments();
        }

        function _buildUserAssignments() {
            var totalFilesOfJob = vm.job.totalFiles;
            var total = totalFilesOfJob;
            var totalCredit = 0;
            for(var i = 0; i < vm.selectedTasks.length; i++) {
                totalCredit += vm.selectedTasks[i].taskCredit;
            }

            if(totalCredit <= 0) {
                totalCredit = 1;
            }

            total *= totalCredit;

            for(var i = 0; i < vm.job.jobTeams.length; i++) {
                var selectedTeam = vm.job.jobTeams[i];
                var totalFilesForTeam = 0;
                for(var j = 0; j < selectedTeam.jobTeamUsers.length; j++) {
                    var member = selectedTeam.jobTeamUsers[j];
                    var capacity = member.capacity;

                    var totalFilesForUser = 0;

                    if(capacity > total) {
                        totalFilesForUser = totalFilesOfJob;
                        total = 0;
                    }else {
                        totalFilesForUser = Math.ceil(capacity * totalFilesOfJob / total);
                        total -= capacity;
                    }

                    // if(totalFilesOfJob > 0 && capacity && capacity > 0) {
                    //     totalFilesForUser = Math.ceil(total / capacity);
                    //
                    //     if(totalFilesForUser > totalFilesOfJob) {
                    //         totalFilesForUser = totalFilesOfJob;
                    //     }
                    //
                    //     // total -= totalFilesForUser;
                    //     totalFilesOfJob -= totalFilesForUser;
                    // }

                    member.totalFiles = totalFilesForUser;

                    totalFilesForTeam += totalFilesForUser;

                    totalFilesOfJob -= totalFilesForUser;

                }

                selectedTeam.totalFiles = totalFilesForTeam;
            }
        }

        vm.hasRoleUser = function(roles) {
            if(roles) {
                return roles.indexOf('ROLE_USER') != -1;
            }
            return false;
        }

    }
})();
