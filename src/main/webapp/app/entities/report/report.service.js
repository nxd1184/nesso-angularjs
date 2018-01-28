/**
 * Created by LenVo on 1/28/18.
 */
(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('reportService', reportService);

    reportService.$inject = ['$http', '$q', 'StringUtils'];

    function reportService ($http, $q, StringUtils) {
        var service = {
            getProductivityForThisWeek: getProductivityForThisWeek,
            getProductivityForThisMonth: getProductivityForThisMonth
        };

        function getProductivityForThisWeek(params) {

            var fromDate = StringUtils.encode(StringUtils.toIsoTrimToMinute(params.fromDate));
            var toDate = StringUtils.encode(StringUtils.toIsoTrimToMinute(params.toDate));
            var fromDeadlineDate = StringUtils.encode(StringUtils.toIsoTrimToMinute(params.fromDeadlineDate));
            var toDeadlineDate = StringUtils.encode(StringUtils.toIsoTrimToMinute(params.toDeadlineDate));

            var url = 'api/report/dashboard/this-week?fromDate=' + fromDate + '&toDate=' + toDate + '&fromDeadlineDate=' + fromDeadlineDate + '&toDeadlineDate=' + toDeadlineDate;

            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function getProductivityForThisMonth() {
            var url = 'api/report/dashboard/this-month';

            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        return service;
    }
})();
