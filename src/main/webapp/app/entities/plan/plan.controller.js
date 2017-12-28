(function () {
    'use strict';

    angular
        .module('nessoApp')
        .controller('PlanController', PlanController);

    PlanController.$inject = ['$state', 'Project'];

    function PlanController($state, Project) {

        var vm = this;
        vm.projects = [];
        vm.filters = ['Task Code', 'Project Code'];
        vm.treeData = [];

        _loadProjects();

        function _loadProjects() {
            Project.query({}, onSuccess, onError);

            function onSuccess(result) {
                vm.projects = result;
                for(var i = 0; i < vm.projects.length; i++) {
                    var project = vm.projects[i];

                    // for(var j = 0; j < project.jobs.length; j++) {
                    //     var job = project.jobs;
                    // }
                    //
                    // var records = {
                    //     Project: project.name, Total: 0, ToDo: 0, ToCheck: 0, Done: 0, Delivery: 0,
                    //     children: [
                    //
                    //     ]
                    // };
                }
            }

            function onError(error) {

            }
        }

        vm.treeData = [
            {
                Name: "USA", Area: 9826675, Population: 318212000, TimeZone: "UTC -5 to -10",
                children: [
                    {
                        Name: "California", Area: 423970, Population: 38340000, TimeZone: "Pacific Time",
                        children: [
                            {Name: "San Francisco", Area: 231, Population: 837442, TimeZone: "PST"},
                            {Name: "Los Angeles", Area: 503, Population: 3904657, TimeZone: "PST"}
                        ]
                    },
                    {
                        Name: "Illinois", Area: 57914, Population: 12882135, TimeZone: "Central Time Zone",
                        children: [
                            {Name: "Chicago", Area: 234, Population: 2695598, TimeZone: "CST"}
                        ]
                    }
                ]
            },
            {
                Name: "Texas", Area: 268581, Population: 26448193, TimeZone: "Mountain"
            }
        ];
    }
})();
