(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('Team', Team);

    Team.$inject = ['$resource'];

    function Team ($resource) {
        var resourceUrl =  'api/teams/:id';

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
            'update': { method:'PUT' },
            'getAllTeam': {
                method: 'GET',
                url: 'api/teams/all'
            }
        });
    }

    angular
        .module('nessoApp')
        .factory('teamService', teamService);

    teamService.$inject = ['$http', '$q']

    function teamService ($http, $q) {
        var teamService = {
            update: update
        };

        function update(params) {
            var url = 'api/teams/update';

            var rq = {
                teamId: params.id,
                teamName: params.name,
                leaderId: params.leaderId,
                status: params.status,
                members: params.members
            };

            var defer = $q.defer();

            $http(LA.RequestUtils.put(url, rq)).then(function(result) {
               defer.resolve(result.data);
            });

            return defer.promise;
        }

        return teamService;
    }

})();
