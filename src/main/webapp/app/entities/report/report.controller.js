(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope','$state', '$timeout', 'reportService', '$window'];

    function ReportController($scope, $state, $timeout, reportService, $window) {
        var vm = this;
        vm.productionBonusProject = [];
        vm.qualityReport = [];
        vm.deliveryQualityReport = [];
        vm.freelancerReport = [];
        vm.checkInReport = [];
        vm.projectAndMemberReport = [];

        vm.getProductionBonusReport = _getProductionBonusReport;
        vm.getQualityReport = _getQualityReport;
        vm.getDeliveryQualityReport = _getDeliveryQualityReport;
        vm.getFreelancerReport = _getFreelancerReport;
        vm.getCheckInReport = _getCheckInReport;
        vm.getProjectAndMemberReport = _getProjectAndMemberReport;

        function _getProductionBonusReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getProductionBonus(params).then(function (result) {
                console.log(result);
                vm.productionBonusProject = [];
                var data_group_by_user = {};
                result.report.forEach(function (item, index) {
                    var employee = {'projects': {}, 'totalCredit': 0,  'totalVolumn': 0};
                    if (data_group_by_user[item.userId] != null) {
                        employee = data_group_by_user[item.userId];
                    }
                    employee.name = item.employee;
                    employee.id = item.userId;

                    var project = {'totalCredit': 0,  'totalVolumn': 0, 'jobs': {}};
                    if (employee.projects[item.projectId] != null) {
                        project = employee.projects[item.projectId];
                    }
                    project.name = item.projectName;
                    project.id = item.projectId;

                    var job = {'volumn': 0, 'credit': 0, 'totalCredit': 0};
                    if (project.jobs[item.jobId] != null) {
                        job = project.jobs[item.jobId];
                    }
                    job.name = item.jobName;
                    job.id = item.jobId;
                    job.volumn += item.volumn;
                    job.credit += item.credit;
                    job.totalCredit += item.totalCredit;

                    project.jobs[item.jobId] = job;
                    project.totalCredit += job.totalCredit;
                    project.totalVolumn += job.volumn;

                    employee.totalCredit += job.totalCredit;
                    employee.totalVolumn += job.volumn;
                    employee.projects[item.projectId] = project;
                    data_group_by_user[item.userId] = employee;
                });
                console.log("data_group_by_user", data_group_by_user);
                var idx_user = 1;
                for (var user_id in data_group_by_user) {
                    var list_rows = [];
                    var user = data_group_by_user[user_id];
                    console.log("user", user)
                    var user_row = {'employee': user.name, 'project' : '', 'job': '', 'volumn': user.totalVolumn, 'credit': '', 'totalCredit': user.totalCredit};
                    user_row.idx_user = idx_user++;
                    list_rows.push(user_row);

                    for (var projectId in user.projects) {
                        var project = user.projects[projectId];
                        var project_row = {'employee': '', 'project' : project.name, 'job': '', 'volumn': project.totalVolumn, 'credit': '', 'totalCredit': project.totalCredit};
                        list_rows.push(project_row);

                        for (var jobId in project.jobs) {
                            var job = project.jobs[jobId];
                            var job_row = {'employee': '', 'project' : '', 'job': job.name, 'volumn': job.volumn, 'credit': job.credit, 'totalCredit': job.totalCredit};
                            list_rows.push(job_row);
                        }
                    }
                    user.list_rows = list_rows;
                    vm.productionBonusProject.push(user);
                }
                console.log("vm.productionBonusProject", vm.productionBonusProject);
            });
        }
        function _getQualityReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getQualityReport(params).then(function (result) {
                console.log(result);
                vm.qualityReport = result;
            });
        }

        function _getDeliveryQualityReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getDeliveryQualityReport(params).then(function (result) {
                console.log(result);
                vm.deliveryQualityReport = result;
            });
        }

        function _getFreelancerReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getProductionBonus(params).then(function (result) {
                console.log(result);
                vm.freelancerReport = result;
            });
        }

        function _getCheckInReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getCheckInReport(params).then(function (result) {
                console.log(result);
                vm.checkInReport = result;
            });
        }

        function _getProjectAndMemberReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getProjectAndMemberReport(params).then(function (result) {
                console.log(result);
                vm.projectAndMemberReport = result;
            });
        }


        _getProductionBonusReport();
    }
})();
