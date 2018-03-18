(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }

    angular
        .module('nessoApp')
        .factory('projectService', projectService);

    projectService.$inject = ['$http', '$q'];

    function projectService ($http, $q) {

        var service = {
            syncUp: syncUp,
            search: search
        };

        function syncUp(projectCode) {
            var defer = $q.defer();

            var url = '/api/projects/sync-up/';

            var data = {
                projectCode: projectCode
            };

            $http(LA.RequestUtils.put(url, data)).then(function(result) {
                defer.resolve(result.data);
            }, function error(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function search(params) {
            var defer = $q.defer();

            var url = '/api/projects/search/?search=' + LA.StringUtils.trimToEmpty(params.code);


            $http(LA.RequestUtils.get(url)).then(function(result) {
                defer.resolve(result.data);
            }, function error(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        return service;
    }

})();
