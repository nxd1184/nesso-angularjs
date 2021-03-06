(function() {
    'use strict';

    angular
        .module('nessoApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'frapontillo.bootstrap-switch',
            'ngSanitize', 'ui.select', 'ui.slider',
            'datatables',
            'angularMoment',
            'daterangepicker',
            'ngSlimScroll'
        ])
        .run(run);

    run.$inject = ['stateHandler'];

    function run(stateHandler) {
        stateHandler.initialize();
    }
})();
