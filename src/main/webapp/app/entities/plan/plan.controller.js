(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanController', PlanController);

    PlanController.$inject = ['$scope','$state', 'planService', '$timeout', 'projectService', '$window'];

    function PlanController($scope, $state, planService, $timeout, projectService, $window) {

        var vm = this;
        vm.projects = [];
        vm.filters = ['Task Code', 'Project Code'];

        vm.rows = [];

        vm.syncUp = syncUp;

        _getAllPlans();

        function _getAllPlans() {
            vm.rows = [];
            planService.getAllPlans().then(onSuccess, onError);

            function onSuccess(result) {
                vm.projects = result;
                var projectNodes = [];
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
                                    delivery: user.totalDeliveryFiles
                                });
                            }
                        }
                    }

                }

                $timeout(function() {
                    angular.element('.tree').treegrid();
                }, 2);

            }

            function onError(error) {

            }
        }

        function syncUp(projectCode) {
            projectService.syncUp(projectCode).then(onSuccess);

            function onSuccess(result) {
                _getAllPlans();
            }
        }

    }
})();
