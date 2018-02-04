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

    teamService.$inject = ['$http', '$q', 'StringUtils']

    function teamService ($http, $q, StringUtils) {
        var teamService = {
            update: update,
            search: search
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

        function search(params) {
            var url = 'api/teams/search?' +
                'searchTerm=' + StringUtils.trimToEmpty(params.searchTerm) +
                '&teamId=' + StringUtils.trimToEmpty(params.teamId) +
                '&sort=' + StringUtils.trimToEmpty(params.sort) +
                '&page=' + StringUtils.trimToEmpty(params.page) +
                '&size=' + StringUtils.trimToEmpty(params.size);

            var defer = $q.defer();

            $http(LA.RequestUtils.get(url)).then(function(result) {
                defer.resolve(result.data);
            });

            return defer.promise;
        }

        return teamService;
    }

})();
