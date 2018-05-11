(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('IgnoreName', IgnoreName);

    IgnoreName.$inject = ['$resource'];

    function IgnoreName ($resource) {
        var resourceUrl =  'api/ignore-names/:id';

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
