(function() {
    'use strict';

    angular
        .module('nessoApp')
        .factory('StringUtils', StringUtils);

    StringUtils.$inject = [];

    function StringUtils() {

        var service = {
            isNotBlank: isNotBlank,
            trimToEmpty: trimToEmpty
        };



        function isNotBlank(str) {
            if (!str) {
                return false;
            }
            if(str.trim() === '') {
                return false;
            }

            return true;
        }

        function trimToEmpty(str) {
            if(!str) return '';
            return (str + '').trim();
        }

        return service;
    }

})();
