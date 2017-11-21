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
})();
