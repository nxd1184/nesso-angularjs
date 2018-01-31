(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'reportService', 'moment'];

    function HomeController ($scope, Principal, LoginService, $state, reportService, moment) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.productivityThisWeek = null;
        vm.productivityThisMonth = null;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getProductivityThisWeek();
        getAccount();
        getProductivityThisMonth();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        function getProductivityThisWeek() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate(),
                fromDeadlineDate: moment().startOf('day').toDate(),
                toDeadlineDate: moment().endOf('isoWeek').toDate(),
            };
            reportService.getProductivityForThisWeek(params).then(function (result) {
                var toDay = moment().startOf('day').toDate();
                result.urgentJobs.forEach(function (item, index) {
                    var deadLineTime = moment(item.deadline).diff(toDay, 'days');
                    if (deadLineTime == 0){
                        deadLineTime = "ToDay";
                    }
                    item.index = index + 1;
                    item.deadLineTime = deadLineTime;
                });
                vm.productivityThisWeek = result;
                var quality = 0;
                if (vm.productivityThisWeek.totalReceive != 0)
                    quality = Number((vm.productivityThisWeek.totalToDo * 100/ vm.productivityThisWeek.totalReceive).toFixed(2));
                bindingQualityChart(quality);
            });
        }

        function bindingQualityChart(quality) {
            Highcharts.chart('container_chart', {

                    chart: {
                        type: 'gauge',
                        plotBackgroundColor: null,
                        plotBackgroundImage: null,
                        plotBorderWidth: 0,
                        plotShadow: false
                    },

                    title: {
                        text: ''
                    },
                    credits: {
                        enabled: false
                    },
                    pane: {
                        startAngle: -150,
                        endAngle: 150,
                        background: [{
                            backgroundColor: {
                                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                                stops: [
                                    [0, '#FFF'],
                                    [1, '#333']
                                ]
                            },
                            borderWidth: 0,
                            outerRadius: '109%'
                        }, {
                            backgroundColor: {
                                linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
                                stops: [
                                    [0, '#333'],
                                    [1, '#FFF']
                                ]
                            },
                            borderWidth: 1,
                            outerRadius: '107%'
                        }, {
                            // default background
                        }, {
                            backgroundColor: '#DDD',
                            borderWidth: 0,
                            outerRadius: '105%',
                            innerRadius: '103%'
                        }]
                    },

                    // the value axis
                    yAxis: {
                        min: 0,
                        max: 100,

                        minorTickInterval: 'auto',
                        minorTickWidth: 0,
                        minorTickLength: 0,
                        minorTickPosition: 'inside',
                        minorTickColor: '#666',

                        tickPixelInterval: 30,
                        tickWidth: 0,
                        tickPosition: 'inside',
                        tickLength: 10,
                        tickColor: '#666',
                        labels: {
                            step: 2,
                            rotation: 'auto'
                        },
                        title: {
                            text: '%'
                        },
                        plotBands: [{
                            from: 0,
                            to: 40,
                            color: '#55BF3B' // green
                        }, {
                            from: 40,
                            to: 60,
                            color: '#DDDF0D' // yellow
                        }, {
                            from: 60,
                            to: 100,
                            color: '#DF5353' // red
                        }]
                    },

                    series: [{
                        name: 'ToDo/Receive',
                        data: [80],
                        tooltip: {
                            valueSuffix: ' %'
                        }
                    }]

                },
                // Add some life
                function (chart) {
                    if (!chart.renderer.forExport) {
                        var point = chart.series[0].points[0],
                            newVal,
                            inc = Math.round((Math.random() - 0.5) * 20);

                        newVal = point.y + inc;
                        if (newVal < 0 || newVal > 100) {
                            newVal = point.y - inc;
                        }

                        point.update(quality);
                    }
                });
        }


        function getProductivityThisMonth() {
            reportService.getProductivityForThisMonth().then(function (result) {
                result.urgentJobs.forEach(function (item, index) {
                    if (index == 0 || index == 1 || index == 2)
                        item.troppy = true;
                });
                vm.productivityThisMonth = result;
            });
        }

    }
})();
