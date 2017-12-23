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

    userService.$inject = ['$http', '$q'];

    function userService($http, $q) {
        var service = {
            search: search
        };

        function search() {
            function search(param) {

                var url = 'api/users/search';

                var rq = LA.RequestUtils.get(url
                    + '?page='          + param.page
                    + '&size='          + param.size
                    + '&sort='          + param.sortBy
                    + '&searchTerm='    + param.searchTerm
                );

                var defer = $q.defer();

                $http(rq).then(function(result) {
                    defer.resolve(result);
                }, function(error) {
                    defer.reject(error);
                });

                return defer.promise;
            }
        }

        return service;
    }

})();
