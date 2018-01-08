(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('Task', Task);

    Task.$inject = ['$resource'];

    function Task ($resource) {
        var resourceUrl =  'api/tasks/:id';

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

    angular
        .module('nessoApp')
        .factory('taskService', taskService);

    taskService.$inject = ['$http', '$q', 'StringUtils'];

    function taskService ($http, $q, StringUtils) {
        var service = {
            search: search
        };

        function search(params) {
            var url = 'api/tasks/search?'
                        + '&searchTerm=' + StringUtils.trimToEmpty(params.searchTerm)
                        + '&projectId=' + StringUtils.trimToEmpty(params.projectId)
                        + '&sort=' + StringUtils.trimToEmpty(params.sort)
                        + '&page=' + StringUtils.trimToEmpty(params.page)
                        + '&size=' + StringUtils.trimToEmpty(params.size);

            var defer = $q.defer();

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
