(function () {
    'use strict';

    angular
        .module('nessoApp')
        .factory('timesheetService', timesheetService);




    timesheetService.$inject = ['$http', '$q'];

    function timesheetService($http, $q) {
        var service = {
            submit: submit
        };


        function submit() {

            var url = 'api/timesheet/submit';

            var rq = LA.RequestUtils.post(url, {});

            var defer = $q.defer();

            $http(rq).then(function(result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }


        return service;
    }

})();
