(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('headerService', headerService);

    headerService.$inject = ['$http', '$q', 'StringUtils'];

    function headerService ($http, $q, StringUtils) {
        var service = {
            getNotify: getNotify
        };

        function getNotify() {
            var url = 'api/notification/get-notify';

            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });
            return defer.promise
        }

        return service;
    }
})();
