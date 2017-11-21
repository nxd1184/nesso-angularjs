(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('JobTeamUserTaskTracking', JobTeamUserTaskTracking);

    JobTeamUserTaskTracking.$inject = ['$resource'];

    function JobTeamUserTaskTracking ($resource) {
        var resourceUrl =  'api/job-team-user-task-trackings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
