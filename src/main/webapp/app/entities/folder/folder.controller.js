(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('FolderController', FolderController);

    FolderController.$inject = ['$state', '$uibModal', 'DTOptionsBuilder', 'DTColumnBuilder', 'moment', '$scope', '$timeout'];

    function FolderController($state, $uibModal, DTOptionsBuilder, DTColumnBuilder, moment, $scope, $timeout) {

        var vm = this;
        vm.dtInstance = {};
        vm.searchText = $state.searchText;
        vm.search = search;
        vm.searchType = "Name";

        function search() {
            console.log("Searching user setting by " + vm.searchType);
            var resetPaging = false;
            vm.dtInstance.reloadData(searchCallback, resetPaging);
        }

        function searchCallback(data) {

        }


        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('ajax', {
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('jhi-authenticationToken').replace(new RegExp('"', 'g'), '')
                },
                // Either you specify the AjaxDataProp here
                // dataSrc: 'data',
                url: 'api/search-user-settings',
                data: function(d){
                    var params = {};

                    if(vm.searchType === "Name") {
                        params.name = vm.searchText;
                    }
                    else if (vm.searchType === "Date") {
                        if (vm.searchText)
                        {
                            //params.date = new Date(vm.searchText).toISOString();
                            params.date = LA.StringUtils.urlEncode(LA.StringUtils.toIsoFromMoment(moment(vm.searchText, "DD/MM/YYYY")));
                            console.log(params.date);
                        }
                    }

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
            .withOption('serverSide', true)// required
            .withOption('rowCallback', rowClick);
        ;

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('#'),
            DTColumnBuilder.newColumn('name').withTitle('NAME'),
            DTColumnBuilder.newColumn('configInfo').withTitle('CONFIG INFO'),
            DTColumnBuilder.newColumn('auto').withTitle('AUTO').renderWith(function (data) {
                var html = '';
                if(data) {
                    html = '<i class="fa fa-check" aria-hidden="true"></i>';
                }
                return html;
            }),
            DTColumnBuilder.newColumn('type').withTitle('TYPE'),
            DTColumnBuilder.newColumn('userConfigName').withTitle('USER CONFIG'),
            DTColumnBuilder.newColumn('createdDate').withTitle('CREATE DATE').renderWith(function(data) {
                var value = data;
                if(data) {
                    value = moment(data).format('DD/MM/YYYY hh:mm');
                }
                return value;
            })
        ];

        function rowClick(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            // Unbind first in order to avoid any duplicate handler (see https://github.com/l-lin/angular-datatables/issues/87)
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function () {
                var project = aData;
                if (project['startDate']) {
                    project['startDate'] = moment(project['startDate']).toDate();
                }

                $scope.$apply(function () {
                    vm.createOrUpdateSetting(project);
                });
            });
            return nRow;
        }

        angular.element(document).ready(function () {
            console.log("initialize Folder Tree");

            $('#tree_1').jstree({
                                "core" : {
                                    "themes" : {
                                        "responsive": false
                                    }
                                },
                                "types" : {
                                    "default" : {
                                        "icon" : "fa fa-folder icon-state-warning icon-lg"
                                    },
                                    "file" : {
                                        "icon" : "fa fa-file icon-state-warning icon-lg"
                                    }
                                },
                                "plugins": ["types"]
                            });
        });
    }

})();
