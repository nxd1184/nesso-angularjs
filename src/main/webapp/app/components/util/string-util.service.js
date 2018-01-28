(function() {
    'use strict';

    angular
        .module('nessoApp')
        .factory('StringUtils', StringUtils);

    StringUtils.$inject = [];

    function StringUtils() {

        var service = {
            isNotBlank: isNotBlank,
            trimToEmpty: trimToEmpty,
            encode: encode,
            decode: decode,
            toIsoTrimToMinute: toIsoTrimToMinute
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
        function toIsoTrimToMinute(d) {
            if (!d) {
                return '';
            }
            return moment(d).set({second: 0, millisecond: 0}).format('YYYY-MM-DD HH:mm:ss:SSS ZZ');
        }
        function trimToEmpty(str) {
            if(!str) return '';
            return (str + '').trim();
        }
        function encode(s) {
            return encodeURIComponent(s);
        }

        function decode(s) {
            return decodeURIComponent(s);
        }

        return service;
    }

})();
