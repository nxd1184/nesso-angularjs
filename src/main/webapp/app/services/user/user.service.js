(function () {
    'use strict';

    angular
        .module('nessoApp')
        .factory('User', User);

    User.$inject = ['$resource'];

    function User ($resource) {
        var service = $resource('api/users/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;
    }

    angular
        .module('nessoApp')
        .factory('userService', userService);

    userService.$inject = ['$http', '$q', 'StringUtils'];

    function userService($http, $q, StringUtils) {
        var service = {
            search: search
        };


        function search(param) {

            var url = 'api/users/search';

            var rq = LA.RequestUtils.get(url
                + '?page='          + StringUtils.trimToEmpty(param.page)
                + '&size='          + StringUtils.trimToEmpty(param.size)
                + '&sort='          + StringUtils.trimToEmpty(param.sortBy)
                + '&searchTerm='    + StringUtils.trimToEmpty(param.searchTerm)
                + '&roles='         + StringUtils.trimToEmpty(param.roles)
            );

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
