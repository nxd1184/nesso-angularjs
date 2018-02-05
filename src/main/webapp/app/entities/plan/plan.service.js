(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('planService', planService);

    planService.$inject = ['$http', '$q'];

    function planService ($http, $q) {
        var service = {
            getAllPlans: getAllPlans,
            getJobPlanDetail: getJobPlanDetail,
            update: update,
            getUserJobDetail: getUserJobDetail,
            adjustFiles: adjustFiles
        };

        function getAllPlans(view) {
            var url = 'api/plans?view=' + view;
            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function getJobPlanDetail(jobId) {
            var url = 'api/plan/job-detail/' + jobId;
            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function getUserJobDetail(params) {

            var url = 'api/plan/user-detail/' + params.jobTeamUserId + '/' + params.jobId;
            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function update(data) {
            var url = 'api/plan/update/';
            var defer = $q.defer();
            $http(LA.RequestUtils.put(url, data)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function adjustFiles(params) {
            var url = 'api/plan/adjust';
            var rq = {
                jobId: params.jobId,
                jobTeamUserId: params.jobTeamUserId,
                toUserId: params.toUserId,
                totalFilesAdjustment: params.totalFilesAdjustment
            };
            var defer = $q.defer();
            $http(LA.RequestUtils.put(url, rq)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        return service;
    }
})();
