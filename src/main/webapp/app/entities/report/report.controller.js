(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope','$state', '$timeout', 'reportService', '$window'];

    function ReportController($scope, $state, $timeout, reportService, $window) {
        var vm = this;

        vm.dateRangePicker = { "startDate": "2017-12-31T17:00:00.000Z", "endDate": "2018-01-30T16:59:59.999Z" };

        vm.options = {
            applyClass: 'btn-green',
            locale: {
                applyLabel: "Apply",
                fromLabel: "From",
                format: "YYYY-MM-DD", //will give you 2017-01-06
                //format: "D-MMM-YY", //will give you 6-Jan-17
                //format: "D-MMMM-YY", //will give you 6-January-17
                toLabel: "To",
                cancelLabel: 'Cancel',
                customRangeLabel: 'Custom range'
            },
            ranges: {
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()]
            }
        }

        function _getProductionBonusReport() {
            var params = {
                fromDate : moment().startOf('isoWeek').toDate(),
                toDate : moment().endOf('isoWeek').toDate()
            };
            reportService.getProductionBonus(params).then(function (result) {
                console.log(result);
            });
        }

        _getProductionBonusReport();
    }
})();
