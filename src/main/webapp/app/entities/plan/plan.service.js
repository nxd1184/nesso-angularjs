(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('planService', planService);

    planService.$inject = ['$http', '$q'];

    function planService ($http, $q) {
        var service = {};

        return service;
    }
})();
