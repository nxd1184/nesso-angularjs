(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$stateParams', 'Task'];

    function TaskDetailController($stateParams, Task) {
        var vm = this;

        vm.load = load;
        vm.task = {};

        vm.load($stateParams.login);

        function load(login) {
            Task.get({login: login}, function(result) {
                vm.task = result;
            });
        }
    }
})();
