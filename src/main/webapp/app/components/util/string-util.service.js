(function() {
    'use strict';

    angular
        .module('nessoApp')
        .factory('StringUtils', StringUtils);

    StringUtils.$inject = [];

    function StringUtils() {

        var service = {
            isNotBlank: isNotBlank
        };

        return service;

        function isNotBlank(str) {
            if (!str) {
                return false;
            }
            if(str.trim() === '') {
                return false;
            }

            return true;
        }
    }

})();
