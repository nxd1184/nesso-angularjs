if(!window.LA) {
    window.LA = {}
}
LA.StringUtils = {
    isEmpty: function (str) {
        if (typeof str === 'undefined' || str === null || str === '' || str === 'null') {
            return true;
        }
        return false;
    },

    trimToEmpty: function (str) {
        if (this.isEmpty(str)) {
            return '';
        }
        if (typeof str === 'string') {
            return str.trim();
        }
        return str + '';
    },

    trimToNull: function (str) {
        var result = this.trimToEmpty(str);
        if (!result) {
            return null;
        }
        return result;
    },

    // return d2 - d1 --> format to HH:mm
    hourDiff: function(d2, d1) {
        if (!d1 || !d2) {
            return '';
        }
        return moment.utc(moment(d2).diff(moment(d1))).format('HH:mm');
    },


    formatDate: function (date) {
        if (!date) {
            return '';
        }
        var d = new Date(date);
        if (d instanceof Date && !isNaN(d.valueOf())) {
            var month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();

            if (month.length < 2) month = '0' + month;
            if (day.length < 2) day = '0' + day;

            return [year, month, day].join('-');
        }
        return '';
    },

    formatDateTime: function (date) {
        if (!date) {
            return '';
        }
        var d = new Date(date);
        if (d instanceof Date && !isNaN(d.valueOf())) {
            var month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear(),
                hour = '' + d.getHours(),
                minute = '' + d.getMinutes();

            if (month.length < 2) month = '0' + month;
            if (day.length < 2) day = '0' + day;
            if (hour.length < 2) hour = '0' + hour;
            if (minute.length < 2) minute = '0' + minute;

            return [year, month, day].join('-') + " " + hour + ":" + minute;
        }
        return '';
    },

    urlEncode : function (s) {
        return encodeURIComponent(s);
    },

    toStartOfDay : function(d) {
        if (d) {
            return moment(d).startOf('day').format('YYYY-MM-DD HH:mm:ss:SSS ZZ');
        }
        return null;
    },

    toEndOfDay : function(d) {
        if (d) {
            return moment(d).endOf('day').format('YYYY-MM-DD HH:mm:ss:SSS ZZ');
        }
        return null;
    },

    parseIso : function(s) {
        if (!s) {
            return null;
        }
        if (typeof s === 'number') {
            return moment(s).toDate();
        }
        return moment(s, 'YYYY-MM-DD HH:mm:ss:SSS ZZ').toDate();
    },

    /**
     *
     * @param s e.g. 2017-06-13 04:01:57:244 +0200
     * @returns {*} moment object, not Javascript Date object
     */
    parseIsoMoment : function(s) {
        if (!s) {
            return null;
        }
        if (typeof s === 'number') {
            return moment(s);
        }
        return moment(s, 'YYYY-MM-DD HH:mm:ss:SSS ZZ');
    },

    toStringIsoDate: function(o) {
        if (!o) {
            return null;
        }
        if (typeof o === 'number') {
            return moment(o).format('YYYY-MM-DD');
        }
        if(typeof o === 'date') {
            return moment(o).format('YYYY-MM-DD');
        }
        return moment(o, 'YYYY-MM-DD HH:mm:ss:SSS ZZ').format('YYYY-MM-DD');
    },

    toIso : function(d) {
        if (!d) {
            return '';
        }
        return moment(d).format('YYYY-MM-DD HH:mm:ss:SSS ZZ');
    },

    toIsoTrimToMinute : function(d) {
        if (!d) {
            return '';
        }
        return moment(d).set({second: 0, millisecond: 0}).format('YYYY-MM-DD HH:mm:ss:SSS ZZ');
    },

    uuid : function() {
        var d = new Date().getTime();
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (d + Math.random()*16)%16 | 0;
            d = Math.floor(d/16);
            return (c=='x' ? r : (r&0x3|0x8)).toString(16);
        });
        return uuid;
    },

    /**
     *
     * @param text 'abc xyz 123 @x '
     * @returns 'ABC_XYZ_123_X'
     */
    genCode: function(text) {
        if (AT.StringUtils.isEmpty(text)) {
            return '';
        }
        return text.replace(/[^0-9a-zA-Z\s\-\\\/]/g, '').trim().replace(/[\s\-\\\/]+/g, '_').toUpperCase();
    },

    formatNumber: function(number) {
        if(!number) {
            return '';
        }
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, "'");
    },

    encode: function(s) {
        return encodeURIComponent(s);
    },

    decode: function(s) {
        return decodeURIComponent(s);
    },

    trimToEmpty: function(s) {
        if(!s) return '';
        return (s + '').trim();
    }
};
