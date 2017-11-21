(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('JobTeam', JobTeam);

    JobTeam.$inject = ['$resource'];

    function JobTeam ($resource) {
        var resourceUrl =  'api/job-teams/:id';

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
