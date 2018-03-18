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

        vm.fromDateMoment;
        vm.toDateMoment;

        vm.weekNumber = 0;
        vm.fromDayOfMonth = 0;
        vm.toDayOfMonth = 0;
        vm.ranges = [];
        vm.onDateSelected = onDateSelected;

        if(!vm.fromDate && !vm.toDate) {
            vm.fromDateMoment = moment().startOf('isoWeek');
            vm.toDateMoment = moment().endOf('isoWeek');
        }else {
            vm.fromDateMoment = LA.StringUtils.parseIsoMoment(vm.fromDate);
            vm.toDateMoment = LA.StringUtils.parseIsoMoment(vm.toDate);
        }

        vm.fromDate = vm.fromDateMoment.toDate();
        vm.fromDayOfMonth = vm.fromDateMoment.format('DD');
        vm.toDate = vm.toDateMoment.toDate();
        vm.toDayOfMonth = vm.toDateMoment.format('DD');
        vm.weekNumber = vm.toDateMoment.isoWeek();

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
                fromDate: LA.StringUtils.toIsoTrimToMinute(vm.fromDate),
                toDate: LA.StringUtils.toIsoTrimToMinute(vm.toDate)
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

                    totalDone: project.totalDone,
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
                        totalDone: job.totalDone,
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
                            totalDone: team.totalDone,
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
                                totalDone: user.totalDone,
                                totalDoneByDays: user.totalDoneByDays
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
                    totalDone: team.totalDone,
                    totalDoneByDays: team.totalDoneByDays
                });

                for(var keyUser in team.users) {
                    var user = team.users[keyUser];
                    var userTreeGrid = treeLevel++;
                    vm.rows.push({
                        id: user.userId,
                        cssClass: 'parent treegrid-' + userTreeGrid + ' treegrid-parent-' + teamTreeLevel,
                        type: 'user',
                        name: user.name,
                        totalDone: user.totalDone,
                        totalDoneByDays: user.totalDoneByDays
                    });

                    for(var keyProject in user.projects) {
                        var project = user.projects[keyProject];
                        var projectTreeGrid = treeLevel++;
                        vm.rows.push({
                            id: project.projectId,
                            cssClass: 'parent treegrid-' + projectTreeGrid + ' treegrid-parent-' + userTreeGrid,
                            type: 'project',
                            name: project.projectName,
                            totalDone: project.totalDone,
                            totalDoneByDays: project.totalDoneByDays
                        });

                        for(var jobKey in project.jobs) {
                            var job = project.jobs[jobKey];

                            var jobTreeGrid = treeLevel++;

                            vm.rows.push({
                                id: job.jobId,
                                cssClass: 'treegrid-' + jobTreeGrid + ' treegrid-parent-' + projectTreeGrid,
                                type: 'job',
                                name: job.jobName,
                                totalDone: job.totalDone,
                                totalDoneByDays: job.totalDoneByDays
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
            var startDate = vm.fromDateMoment;
            var endData = vm.toDateMoment;
            while(startDate.isBefore(vm.toDate)) {
                vm.ranges.push({
                    dayOfWeek: startDate.format('ddd'),
                    dayOfMonth: startDate.format('DD')
                });

                startDate = startDate.add(1,'days');
            }
        }

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        function openCalendar (field) {
            vm.datePickerOpenStatus[field] = true;
        }

        function onDateSelected(args) {
            if(args.closePressed) {
                var selectedDate = moment(args.closeDate);
                vm.fromDate = selectedDate.startOf('isoWeek').toDate();
                vm.toDate = selectedDate.endOf('isoWeek').toDate();

                doFilter();
            }
        }

    }
})();
