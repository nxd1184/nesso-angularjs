(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('JobTeamUserTask', JobTeamUserTask);

    JobTeamUserTask.$inject = ['$resource', 'DateUtils'];

    function JobTeamUserTask ($resource, DateUtils) {
        var resourceUrl =  'api/job-team-user-tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastCheckInTime = DateUtils.convertDateTimeFromServer(data.lastCheckInTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    angular
        .module('nessoApp')
        .factory('jobTeamUserTaskService', jobTeamUserTaskService);

    jobTeamUserTaskService.$inject = ['$http', '$q'];

    function jobTeamUserTaskService ($http, $q) {
        var service = {
            search: search,
            checkIn: checkIn
        };

        function search(params) {
            var url = 'api/search-job-team-user-tasks?'
                        + 'statusList=' + params.statusList
                        + '&page=0&size=1000&sort=lastModifiedDate,desc';
            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function checkIn(params) {
            var url = 'api/check-in-job-team-user-task/';
            var data = {
                id: params.id
            };
            var defer = $q.defer();
            $http(LA.RequestUtils.put(url, data)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;        }


        return service;
    }
})();
