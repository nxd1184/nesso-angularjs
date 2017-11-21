(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('UserSetting', UserSetting);

    UserSetting.$inject = ['$resource'];

    function UserSetting ($resource) {
        var resourceUrl =  'api/user-settings/:id';

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
