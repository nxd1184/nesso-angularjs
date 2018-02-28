(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanTimelineController', PlanTimelineController);

    PlanTimelineController.$inject = ['$scope','$state', 'planService', '$timeout', 'projectService', '$window', '$stateParams'];

    function PlanTimelineController($scope, $state, planService, $timeout, projectService, $window, $stateParams) {

        var vm = this;
        vm.projects = [];
        vm.teams = [];
        vm.filters = ['Task Code', 'Project Code'];
        vm.filterBy = $stateParams.filterBy;
        vm.filterValue = $stateParams.filterValue;
        vm.fromDate = $stateParams.fromDate;
        vm.toDate = $stateParams.toDate;
        vm.weekNumber = 0;
        vm.fromDayOfMonth = 0;
        vm.toDayOfMonth = 0;
        vm.ranges = [];

        if(!vm.fromDate || vm.toDate) {
            vm.fromDate = moment().startOf('isoWeek').toDate();
            vm.fromDayOfMonth = moment().startOf('isoWeek').format('DD');
            vm.toDate = moment().endOf('isoWeek').toDate();
            vm.toDayOfMonth = moment().endOf('isoWeek').format('DD');
            vm.weekNumber = moment().isoWeek();
        }

        vm.projectView = true;
        vm.userView = false;
        vm.changeViewBaseOn = changeViewBaseOn;
        vm.currentView = $stateParams.view;

        vm.doFilter = doFilter;

        vm.rows = [];

        vm.syncUp = syncUp;

        function doFilter() {
            var query = {
                filterBy: LA.StringUtils.trimToEmpty(vm.filterBy),
                filterValue: LA.StringUtils.trimToEmpty(vm.filterValue),
                view: vm.currentView,
                fromDate: vm.fromDate,
                toDate: vm.toDate
            };
            $state.go($state.current, query, { reload: 'plans-timeline' });
        }

        _buildWeekRange();

        _getAllPlans();

        function _getAllPlans() {
            vm.rows = [];

            var params = {
                taskCode: vm.filterBy === 'Task Code' ? vm.filterValue : '',
                projectCode: vm.filterBy === 'Project Code' ? vm.filterValue : '',
                type: 'TIMELINE',
                toDate: vm.toDate,
                fromDate: vm.fromDate
            };

            planService.getAllPlans(vm.currentView, params).then(onSuccess, onError);

            function onSuccess(result) {
                if(vm.currentView === 'PROJECT') {
                    vm.projects = result.timelineProjects;
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
            for(var keyProject in vm.projects) {
                var project = vm.projects[keyProject];
                var projectTreeLevel = treeLevel++;
                vm.rows.push({
                    id: project.id,
                    cssClass: 'parent treegrid-' + projectTreeLevel,
                    type: 'project',
                    name: project.projectName,
                    total: project.totalFiles,

                    done: project.totalDoneFiles,
                    totalDoneByDays: project.totalDoneByDays
                });



                for(var keyJob in project.jobs) {
                    var job = project.jobs[keyJob];
                    var jobTreeGrid = treeLevel++;
                    vm.rows.push({
                        id: job.id,
                        cssClass: 'parent treegrid-' + jobTreeGrid + ' treegrid-parent-' + projectTreeLevel,
                        type: 'job',
                        name: job.jobName,
                        total: job.totalFiles,
                        done: job.totalDoneFiles,
                        totalDoneByDays: job.totalDoneByDays
                    });

                    for(var keyTeam in job.teams) {
                        var team = job.teams[keyTeam];
                        var teamTreeGrid = treeLevel++;
                        vm.rows.push({
                            id: team.id,
                            cssClass: 'parent treegrid-' + teamTreeGrid + ' treegrid-parent-' + jobTreeGrid,
                            type: 'team',
                            name: team.teamName,
                            total: team.totalFiles,
                            done: team.totalDoneFiles,
                            totalDoneByDays: team.totalDoneByDays
                        });



                        for(var userKey in team.users) {
                            var user = team.users[userKey];

                            var userTreeGrid = treeLevel++;

                            vm.rows.push({
                                id: user.id,
                                cssClass: 'treegrid-' + userTreeGrid + ' treegrid-parent-' + teamTreeGrid,
                                type: 'user',
                                name: user.name,
                                total: user.totalFiles,

                                done: user.totalDoneFiles,
                                totalDoneByDays: user.totalDoneByDays,
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
                                total: job.totalFiles,
                                toDo: job.totalToDo,
                                toCheck: job.totalToCheck,
                                done: job.totalDone,
                                delivery: job.totalDelivery
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

        function _buildWeekRange() {
            var startDate = moment().startOf('isoWeek');
            var endData = moment().endOf('isoWeek');
            while(startDate.isBefore(vm.toDate)) {
                vm.ranges.push({
                    dayOfWeek: startDate.format('ddd'),
                    dayOfMonth: startDate.format('DD')
                });

                startDate = startDate.add(1,'days');
            }
        }

    }
})();
