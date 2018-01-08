(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('planService', planService);

    planService.$inject = ['$http', '$q'];

    function planService ($http, $q) {
        var service = {
            getJobPlanDetail: getJobPlanDetail
        };

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


        return service;
    }
})();
