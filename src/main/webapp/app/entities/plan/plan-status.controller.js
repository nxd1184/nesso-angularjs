(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanStatusController', PlanStatusController);

    PlanStatusController.$inject = ['$scope','$state', 'planService', '$timeout', 'projectService', '$window'];

    function PlanStatusController($scope, $state, planService, $timeout, projectService, $window) {

        var vm = this;
        vm.projects = [];
        vm.teams = [];
        vm.filters = ['Task Code', 'Project Code'];

        vm.projectView = true;
        vm.userView = false;
        vm.changeViewBaseOn = changeViewBaseOn;
        vm.currentView = 'PROJECT';

        vm.rows = [];

        vm.syncUp = syncUp;

        _getAllPlans();

        function _getAllPlans() {
            vm.rows = [];
            planService.getAllPlans(vm.currentView).then(onSuccess, onError);

            function onSuccess(result) {


                if(vm.currentView === 'PROJECT') {
                    vm.projects = result.projects;
                    _buildPlanTreeDataForProjectView();
                }else if(vm.currentView === 'USER') {
                    vm.teams = result.teams;
                    _buildPlanTreeDataForUserView();
                }


                $timeout(function() {
                    angular.element('.tree').treegrid();
                }, 2);

            }

            function onError(error) {

            }
        }

        function _buildPlanTreeDataForProjectView() {
            var treeLevel = 1;
            for(var i = 0; i < vm.projects.length; i++) {
                var project = vm.projects[i];
                var projectTreeLevel = treeLevel++;
                vm.rows.push({
                    id: project.id,
                    cssClass: 'parent treegrid-' + projectTreeLevel,
                    type: 'project',
                    name: project.name,
                    code: project.code,
                    total: project.totalFiles,
                    toDo: project.totalToDoFiles,
                    toCheck: project.totalToCheckFiles,
                    done: project.totalDoneFiles,
                    delivery: project.totalDeliveryFiles
                });



                for(var j = 0; j < project.jobs.length; j++) {
                    var job = project.jobs[j];
                    var jobTreeGrid = treeLevel++;
                    vm.rows.push({
                        id: job.id,
                        cssClass: 'parent treegrid-' + jobTreeGrid + ' treegrid-parent-' + projectTreeLevel,
                        type: 'job',
                        name: job.name,
                        total: job.totalFiles,
                        toDo: job.totalToDoFiles,
                        toCheck: job.totalToCheckFiles,
                        done: job.totalDoneFiles,
                        delivery: job.totalDeliveryFiles
                    });

                    for(var k = 0; k < job.jobTeams.length; k++) {
                        var team = job.jobTeams[k];
                        var teamTreeGrid = treeLevel++;
                        vm.rows.push({
                            id: team.id,
                            cssClass: 'parent treegrid-' + teamTreeGrid + ' treegrid-parent-' + jobTreeGrid,
                            type: 'team',
                            name: team.teamName,
                            total: team.totalFiles,
                            toDo: team.totalToDoFiles,
                            toCheck: team.totalToCheckFiles,
                            done: team.totalDoneFiles,
                            delivery: team.totalDeliveryFiles
                        });



                        for(var h = 0; h < team.jobTeamUsers.length; h++) {
                            var user = team.jobTeamUsers[h];

                            var userTreeGrid = treeLevel++;

                            vm.rows.push({
                                id: user.id,
                                cssClass: 'treegrid-' + userTreeGrid + ' treegrid-parent-' + teamTreeGrid,
                                type: 'user',
                                name: user.name,
                                total: user.totalFiles,
                                toDo: user.totalToDoFiles,
                                toCheck: user.totalToCheckFiles,
                                done: user.totalDoneFiles,
                                delivery: user.totalDeliveryFiles,
                                jobId: job.id
                            });
                        }
                    }
                }

            }
        }

        function _buildPlanTreeDataForUserView() {
            var treeLevel = 1;
            for(var keyTeam in vm.teams) {
                var team = vm.teams[keyTeam];
                var teamTreeLevel = treeLevel++;
                vm.rows.push({
                    id: team.teamId,
                    cssClass: 'parent treegrid-' + teamTreeLevel,
                    type: 'team',
                    name: team.teamName,
                    total: team.totalFiles,
                    toDo: team.totalToDo,
                    toCheck: team.totalToCheck,
                    done: team.totalDone,
                    delivery: team.totalDelivery
                });

                for(var keyUser in team.users) {
                    var user = team.users[keyUser];
                    var userTreeGrid = treeLevel++;
                    vm.rows.push({
                        id: user.userId,
                        cssClass: 'parent treegrid-' + userTreeGrid + ' treegrid-parent-' + teamTreeLevel,
                        type: 'user',
                        name: user.name,
                        total: user.totalFiles,
                        toDo: user.totalToDo,
                        toCheck: user.totalToCheck,
                        done: user.totalDone,
                        delivery: user.totalDelivery
                    });

                    for(var keyProject in user.projects) {
                        var project = user.projects[keyProject];
                        var projectTreeGrid = treeLevel++;
                        vm.rows.push({
                            id: project.projectId,
                            cssClass: 'parent treegrid-' + projectTreeGrid + ' treegrid-parent-' + userTreeGrid,
                            type: 'project',
                            name: project.projectName,
                            total: project.totalFiles,
                            toDo: project.totalToDo,
                            toCheck: project.totalToCheck,
                            done: project.totalDone,
                            delivery: project.totalDelivery
                        });

                        for(var jobKey in project.jobs) {
                            var job = project.jobs[jobKey];

                            var jobTreeGrid = treeLevel++;

                            vm.rows.push({
                                id: job.jobId,
                                cssClass: 'treegrid-' + jobTreeGrid + ' treegrid-parent-' + projectTreeGrid,
                                type: 'job',
                                name: job.jobName,
                                total: user.totalFiles,
                                toDo: user.totalToDo,
                                toCheck: user.totalToCheck,
                                done: user.totalDone,
                                delivery: user.totalDelivery
                            });
                        }
                    }
                }
            }
        }

        function syncUp(projectCode) {
            projectService.syncUp(projectCode).then(onSuccess);

            function onSuccess(result) {
                _getAllPlans();
            }
        }

        function changeViewBaseOn(view) {
            if(view === 'PROJECT') {
                vm.projectView = true;
                vm.userView = false;
            }else if(view === 'USER') {
                vm.userView = true;
                vm.projectView = false;
            }
            vm.currentView = view;
            _getAllPlans();
        }

    }
})();
