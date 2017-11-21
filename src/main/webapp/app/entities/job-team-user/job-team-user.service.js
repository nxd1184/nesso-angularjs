(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('JobTeamUser', JobTeamUser);

    JobTeamUser.$inject = ['$resource'];

    function JobTeamUser ($resource) {
        var resourceUrl =  'api/job-team-users/:id';

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
