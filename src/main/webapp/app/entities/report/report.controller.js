(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope','$state', '$timeout', 'reportService', '$window', 'moment'];

    function ReportController($scope, $state, $timeout, reportService, $window, moment) {
        var vm = this;
        vm.productionBonusProject = [];
        vm.qualityReport = [];
        vm.deliveryQualityReport = [];
        vm.freelancerReport = [];
        vm.checkInReportByDate = [];
        vm.checkInReportByUser = [];
        vm.projectAndMemberReport = [];
        vm.currentScreen = 1;
        vm.getProductionBonusReport = _getProductionBonusReport;
        vm.getQualityReport = _getQualityReport;
        vm.getDeliveryQualityReport = _getDeliveryQualityReport;
        vm.getFreelancerReport = _getFreelancerReport;
        vm.getCheckInReport = _getCheckInReport;
        vm.getProjectAndMemberReport = _getProjectAndMemberReport;

        function _getProductionBonusReport() {
            vm.currentScreen = 1;
            var params = _getParamsDatePicker();
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
            vm.currentScreen = 2;
            var params = _getParamsDatePicker();
            reportService.getQualityReport(params).then(function (result) {
                console.log(result);
                vm.qualityReport = result.report;
            });
        }

        function _getDeliveryQualityReport() {
            vm.currentScreen = 3;
            var params = _getParamsDatePicker();
            reportService.getDeliveryQualityReport(params).then(function (result) {
                console.log(result);
                vm.deliveryQualityReport = [];
                var data_group_by_user = {};
                result.report.forEach(function (item, index) {
                    var employee = {'projects': {}, 'totalDone': 0,  'totalVolumn': 0, 'totalError': 0};
                    if (data_group_by_user[item.userId] != null) {
                        employee = data_group_by_user[item.userId];
                    }
                    employee.name = item.employee;
                    employee.id = item.userId;

                    var project = {'totalDone': 0,  'totalVolumn': 0, 'jobs': {}};
                    if (employee.projects[item.projectId] != null) {
                        project = employee.projects[item.projectId];
                    }
                    project.name = item.projectName;
                    project.id = item.projectId;

                    var job = {'volumn': 0, 'error': 0, 'done': 0};
                    if (project.jobs[item.jobId] != null) {
                        job = project.jobs[item.jobId];
                    }
                    job.name = item.jobName;
                    job.id = item.jobId;
                    job.done += item.done;
                    job.volumn += item.volumn;
                    job.error += item.error;
                    job.returnDate = (job.returnDate == null)? "" : moment(item.returnDate).format("DD/MM/YYYY");
                    job.receivedDate = moment(item.receivedDate).format("DD/MM/YYYY");

                    project.jobs[item.jobId] = job;
                    project.totalDone += job.done;
                    project.totalVolumn += job.volumn;

                    employee.totalDone += job.done;
                    employee.totalVolumn += job.volumn;
                    employee.totalError += job.error;
                    employee.errorRate = Number(employee.totalError/employee.totalVolumn).toFixed(2);
                    employee.projects[item.projectId] = project;
                    data_group_by_user[item.userId] = employee;
                });
                console.log("data_group_by_user", data_group_by_user);
                var idx_user = 1;
                for (var user_id in data_group_by_user) {
                    var list_rows = [];
                    var user = data_group_by_user[user_id];
                    console.log("user", user)
                    var user_row = {'employee': user.name, 'project' : '', 'job': '', 'done': user.totalDone ,'volumn': user.totalVolumn, 'error': user.totalError, 'error_rate': user.errorRate, 'received_date': '', 'return_date': ''};
                    user_row.idx_user = idx_user++;
                    list_rows.push(user_row);

                    for (var projectId in user.projects) {
                        var project = user.projects[projectId];
                        var project_row = {'employee': '', 'project' : project.name, 'job': '','done': project.totalDone , 'volumn': project.totalVolumn, 'error': '', 'error_rate': '', 'received_date': '', 'return_date': ''};
                        list_rows.push(project_row);

                        for (var jobId in project.jobs) {
                            var job = project.jobs[jobId];
                            var job_row = {'employee': '', 'project' : '', 'job': job.name, 'done': job.done , 'volumn': job.volumn, 'error': '', 'error_rate': '', 'received_date': job.receivedDate , 'return_date': job.returnDate};
                            list_rows.push(job_row);
                        }
                    }
                    user.list_rows = list_rows;
                    vm.deliveryQualityReport.push(user);
                }
                console.log("vm.deliveryQualityReport", vm.deliveryQualityReport);
            });
        }

        function _getFreelancerReport() {
            vm.currentScreen = 4;
            var params = _getParamsDatePicker();
            reportService.getFreelancerReport(params).then(function (result) {
                console.log(result);
                vm.freelancerReport = [];
                var data_group_by_user = {};
                result.report.forEach(function (item, index) {
                    var employee = {'projects': {}, 'totalDone': 0,  'totalVolumn': 0, 'totalError': 0};
                    if (data_group_by_user[item.userId] != null) {
                        employee = data_group_by_user[item.userId];
                    }
                    employee.name = item.employee;
                    employee.id = item.userId;

                    var project = {'totalDone': 0,  'totalVolumn': 0, 'jobs': {}};
                    if (employee.projects[item.projectId] != null) {
                        project = employee.projects[item.projectId];
                    }
                    project.name = item.projectName;
                    project.id = item.projectId;

                    var job = {'volumn': 0, 'error': 0, 'done': 0};
                    if (project.jobs[item.jobId] != null) {
                        job = project.jobs[item.jobId];
                    }
                    job.name = item.jobName;
                    job.id = item.jobId;
                    job.done += item.done;
                    job.volumn += item.volumn;
                    job.error += item.error;
                    job.returnDate = moment(item.returnDate).format("DD/MM/YYYY");
                    job.receivedDate = moment(item.receivedDate).format("DD/MM/YYYY");

                    project.jobs[item.jobId] = job;
                    project.totalDone += job.done;
                    project.totalVolumn += job.volumn;

                    employee.totalDone += job.done;
                    employee.totalVolumn += job.volumn;
                    employee.totalError += job.error;
                    employee.errorRate = Number(employee.totalError/employee.totalVolumn).toFixed(2);
                    employee.projects[item.projectId] = project;
                    data_group_by_user[item.userId] = employee;
                });
                console.log("data_group_by_user", data_group_by_user);
                var idx_user = 1;
                for (var user_id in data_group_by_user) {
                    var list_rows = [];
                    var user = data_group_by_user[user_id];
                    console.log("user", user)
                    var user_row = {'employee': user.name, 'project' : '', 'job': '', 'done': user.totalDone ,'volumn': user.totalVolumn, 'error': user.totalError, 'error_rate': user.errorRate, 'received_date': '', 'return_date': ''};
                    user_row.idx_user = idx_user++;
                    list_rows.push(user_row);

                    for (var projectId in user.projects) {
                        var project = user.projects[projectId];
                        var project_row = {'employee': '', 'project' : project.name, 'job': '','done': project.totalDone , 'volumn': project.totalVolumn, 'error': '', 'error_rate': '', 'received_date': '', 'return_date': ''};
                        list_rows.push(project_row);

                        for (var jobId in project.jobs) {
                            var job = project.jobs[jobId];
                            var job_row = {'employee': '', 'project' : '', 'job': job.name, 'done': job.done , 'volumn': job.volumn, 'error': '', 'error_rate': '', 'received_date': job.receivedDate , 'return_date': job.returnDate};
                            list_rows.push(job_row);
                        }
                    }
                    user.list_rows = list_rows;
                    vm.freelancerReport.push(user);
                }
                console.log("vm.freelancerReport", vm.freelancerReport);
            });
        }

        function _getCheckInReport() {
            vm.currentScreen = 5;
            var params = _getParamsDatePicker();
            reportService.getCheckInReport(params).then(function (result) {
                console.log(result);
                var data_group_by_user = {};
                var data_group_by_date = {};
                result.report.forEach(function (item, index) {
                    var date = moment(item.day).format("DD/MM/YYYY");
                    if (data_group_by_date[date] == null) {
                        data_group_by_date[date] = {};
                    }
                    data_group_by_date[date][item.userId] = item;

                    var user = {'name': item.employee, 'dates': {}};
                    if (data_group_by_user[item.userId] != null) {
                        user = data_group_by_user[item.userId];
                    }
                    user.dates[date] = item;
                    data_group_by_user[item.userId] = user;
                });

                vm.checkInReportByDate = [];
                vm.checkInReportByUser = [];
                var idx_first_row = 0;
                var count = 0;
                for (var idx_date in data_group_by_date) {
                    var item = data_group_by_date[idx_date];
                    var is_first = true;
                    for (var idx_user in item) {
                        count++;
                        var user = item[idx_user];
                        var checkin = moment(user.checkin).format("hh:mm A");
                        var checkout = moment(user.checkout).format("hh:mm A");
                        var diff_sec = moment(user.checkout).diff(moment(user.checkin))/1000;
                        var diff = Math.floor(diff_sec/3600) + "h" + Math.floor((diff_sec - Math.floor(diff_sec/3600)*3600)/60) + "m";
                        var type = "";
                        if (moment(idx_date, "DD/MM/YYYY").day() == 0) {
                            type = "SUN";
                        }
                        if (moment(idx_date, "DD/MM/YYYY").day() == 6) {
                            type = "SAT";
                        }
                        var css_class = "";
                        var row = {'date': '', 'user_name' : user.employee, 'in': checkin, 'out': checkout, 'hours_working' : diff, 'type': type}
                        if (is_first) {
                            is_first = false;
                            row.date = idx_date;
                            idx_first_row = count;
                            row.css_class = "parent treegrid-" + count;
                        } else {
                            row.css_class = "treegrid-" + count + " treegrid-parent-" + idx_first_row;
                        }
                        vm.checkInReportByDate.push(row);
                    }
                }

                console.log("vm.checkInReportByDate", vm.checkInReportByDate);

                idx_first_row = 0;
                count = 0;
                for (var idx_user in data_group_by_user) {
                    var item = data_group_by_user[idx_user];
                    var is_first = true;
                    for (var idx_date in item.dates) {
                        count++;
                        var user = item.dates[idx_date];
                        var checkin = moment(user.checkin).format("hh:mm A");
                        var checkout = moment(user.checkout).format("hh:mm A");
                        var diff_sec = moment(user.checkout).diff(moment(user.checkin))/1000;
                        var diff = Math.floor(diff_sec/3600) + "h" + Math.floor((diff_sec - Math.floor(diff_sec/3600)*3600)/60) + "m";
                        var type = "";
                        if (moment(idx_date, "DD/MM/YYYY").day() == 0) {
                            type = "SUN";
                        }
                        if (moment(idx_date, "DD/MM/YYYY").day() == 6) {
                            type = "SAT";
                        }
                        var css_class = "";
                        var row = {'user_name' : '', 'date': idx_date,  'in': checkin, 'out': checkout, 'hours_working' : diff, 'type': type}
                        if (is_first) {
                            is_first = false;
                            row.user_name = item.name;
                            idx_first_row = count;
                            row.css_class = "parent treegrid-" + count;
                        } else {
                            row.css_class = "treegrid-" + count + " treegrid-parent-" + idx_first_row;
                        }
                        vm.checkInReportByUser.push(row);
                    }
                }
                console.log("vm.checkInReportByUser", vm.checkInReportByUser);
                $timeout(function() {
                    angular.element('.tree').treegrid();
                }, 2);

            });
        }

        function _getProjectAndMemberReport() {
            vm.currentScreen = 6;
            var params = _getParamsDatePicker();
            reportService.getProjectAndMemberReport(params).then(function (result) {
                // console.log(result);
                // result = {
                //     'members':
                //         [
                //             {
                //             'id' : 1,
                //             'userName': 'User 1'
                //             },
                //             {
                //                 'id' : 2,
                //                 'userName': 'User 2'
                //             }
                //         ],
                //     'projects':
                //         {
                //             '1':
                //                 {
                //                     'id': 1,
                //                     'name': 'Project A',
                //                     'jobs': {
                //                         '1':
                //                             {
                //                                 'id': 1,
                //                                 'credit': 3,
                //                                 'name': 'Job A',
                //                                 'members': {
                //                                     '1' :
                //                                         {
                //                                             'id': 1,
                //                                             'userName': 'User 1',
                //                                             'done': 100,
                //                                             'volume': 100
                //                                         },
                //                                     '2' :
                //                                         {
                //                                             'id': 2,
                //                                             'userName': 'User 2',
                //                                             'done': 102,
                //                                             'volume': 102
                //                                         }
                //                                 }
                //                             }
                //                     }
                //                 }
                //         }
                // };

                var projects = [];
                for (var idx_project in result.report.projects) {
                    var first_row = true;
                    var project = result.report.projects[idx_project];
                    for (var idx_job in project.jobs) {
                        var job = project.jobs[idx_job];
                        var itemRow = {'project_name': '', 'job_name': job.name, 'job_credit': job.credit, 'users': []};
                        if (first_row) {
                            itemRow.project_name = project.name;
                            first_row = false;
                        }
                        var list_users = [];
                        for (var idx_user in result.report.members) {
                            var user =  result.report.members[idx_user];
                            if (job.members[user.id] != null) {
                                user.done = job.members[user.id].done;
                                user.volume = job.members[user.id].volume;
                            }
                            itemRow.users.push(user.done)
                            itemRow.users.push(user.volume)
                        }
                        projects.push(itemRow);
                    }
                    vm.projectAndMemberReport.projects = projects;
                    vm.projectAndMemberReport.members = result.report.members;
                    console.log("vm.projectAndMemberReport: ", vm.projectAndMemberReport);
                }

            });
        }

        function _initDateRangePicker() {
            $('input[name="datefilter"]').daterangepicker({
                autoUpdateInput: false,
                locale: {
                    cancelLabel: 'Clear'
                },
                startDate : moment().subtract(7, 'days'),
                endDate : moment(),
                ranges: {
                    'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    'Last 7 Days': [moment().subtract(7, 'days'), moment()],
                    'Last 30 Days': [moment().subtract(30, 'days'), moment()],
                    'This Month': [moment().startOf('month'), moment().endOf('month')],
                    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                },
                "opens": "left",
            });

            $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
                $(this).val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
                $('.range-date').html(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
                _fetchDataCurrentScreen();
            });

            $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
                $(this).val('');
            });
            var picker = $("#datepicker").data('daterangepicker');
            $('input[name="datefilter"]').val(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
            $('.range-date').html(picker.startDate.format('DD/MM/YYYY') + ' - ' + picker.endDate.format('DD/MM/YYYY'));
        }

        function _getParamsDatePicker() {
            var picker = $("#datepicker").data('daterangepicker');
            var fromDate = picker.startDate.startOf('day').toDate();
            var toDate = picker.endDate.endOf('day').toDate();
            var params = {
                fromDate : fromDate,
                toDate : toDate
            };
            console.log("Params: ", params);
            return params;
        }
        function _fetchDataCurrentScreen() {
            switch (vm.currentScreen) {
                case 1:
                    _getProductionBonusReport();
                    break;
                case 2:
                    _getQualityReport();
                    break;
                case 3:
                    _getDeliveryQualityReport();
                    break;
                case 4:
                    _getFreelancerReport();
                    break;
                case 5:
                    _getCheckInReport();
                    break;
                case 6:
                    _getProjectAndMemberReport();
                    break;
                default:
                    break;
            }
        }
        _initDateRangePicker();
        _getProductionBonusReport();
    }
})();
