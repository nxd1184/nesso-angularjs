(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanController', PlanController);

    PlanController.$inject = ['$scope','$state', 'Project', '$timeout'];

    function PlanController($scope, $state, Project, $timeout) {

        var vm = this;
        vm.projects = [];
        vm.filters = ['Task Code', 'Project Code'];

        vm.rows = [];

        vm.editJob = editJob;

        _loadProjects();

        function _loadProjects() {
            Project.query({}, onSuccess, onError);

            function onSuccess(result) {
                vm.projects = result;
                var projectNodes = [];
                for(var i = 0; i < vm.projects.length; i++) {
                    var project = vm.projects[i];

                    vm.rows.push({
                        id: project.id,
                        cssClass: 'parent treegrid-' + i,
                        type: 'project',
                        name: project.name,
                        total: 0,
                        toDo: 0,
                        toCheck: 0,
                        done: 0,
                        delivery: 0
                    });

                    for(var j = 0; j < project.jobs.length; j++) {
                        var job = project.jobs[j];

                        vm.rows.push({
                            id: job.id,
                            cssClass: 'parent treegrid-parent-' + i + ' treegrid-' + i + j,
                            type: 'job',
                            name: job.name,
                            total: 0,
                            toDo: 0,
                            toCheck: 0,
                            done: 0,
                            delivery: 0
                        });
                    }

                }

                $timeout(function() {
                    angular.element('.tree').treegrid();
                }, 2);

            }

            function onError(error) {

            }
        }

        function editJob(job) {

        }

    }
})();
