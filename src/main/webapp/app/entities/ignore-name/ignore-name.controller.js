(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('IgnoreNameController', IgnoreNameController);

    IgnoreNameController.$inject = ['$state','DTOptionsBuilder', 'DTColumnBuilder', 'moment', '$scope', 'AuthServerProvider', 'IgnoreName', '$compile'];

    function IgnoreNameController($state, DTOptionsBuilder, DTColumnBuilder, moment, $scope, AuthServerProvider, IgnoreName, $compile) {

        var vm = this;

        vm.name;

        vm.dtInstance = {};


        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                headers: {
                    Authorization: 'Bearer ' + AuthServerProvider.getToken().replace(new RegExp('"', 'g'), '')
                },
                // Either you specify the AjaxDataProp here
                // dataSrc: 'data',
                url: 'api/ignore-names',
                data: function(d){
                    var params = {};


                    if(d.order && d.order.length) {
                        params.sort = d.columns[d.order[0].column].data + ',' + d.order[0].dir;
                        for(var i = 1; i < d.order.length; i++) {
                            params.sort += d.columns[d.order[i].column].data + ',' + d.order[i].dir;
                        }
                    }
                    params.page = d.start / d.length;
                    params.size = d.length;

                    return params;
                },
                type: 'GET'
            })
            .withDataProp('data')
            .withPaginationType('full_numbers')
            .withOption('bFilter', false)
            .withOption('processing', true) // required
            .withOption('serverSide', true) // required
            .withOption('createdRow', function (row, data, dataIndex) {
                $compile(angular.element(row).contents())($scope);
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('#'),
            DTColumnBuilder.newColumn('name').withTitle('NAME'),
            DTColumnBuilder.newColumn(null).withTitle('DELETE').notSortable().renderWith(function(data) {
                return '<button class="btn btn-danger" ng-click="vm.delete(' + data.id + ')">Delete</button>'
            })
        ];

        angular.element(document).ready(function () {
            console.log("initializeSelectPicker");
            $('#option_search').selectpicker('show');
        });

        vm.delete = function(id) {
            IgnoreName.delete({id : id}, function() {
                vm.dtInstance.reloadData(null, true);
            });
        };

        vm.create = function() {

            function onSaveSuccess(result) {
                console.log(result);
                vm.dtInstance.reloadData(null, true);
                vm.name = undefined;
            }

            function onSaveError(error) {

            }

            IgnoreName.save({name: vm.name} , onSaveSuccess, onSaveError);
        }


    }

})();
