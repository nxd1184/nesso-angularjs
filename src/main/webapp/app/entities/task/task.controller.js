(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('TaskController', TaskController);

    TaskController.$inject = ['Principal', 'Task', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', '$uibModal', 'DTOptionsBuilder', 'DTColumnBuilder', 'moment', '$scope'];

    function TaskController(Principal, Task, ParseLinks, AlertService, $state, pagingParams, paginationConstants, $uibModal, DTOptionsBuilder, DTColumnBuilder, moment, $scope) {
        var vm = this;

        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.currentAccount = null;
        vm.languages = null;
        vm.setActive = setActive;
        vm.tasks = [];
        vm.clear = clear;
        vm.createOrUpdate = createOrUpdate;

        Principal.identity().then(function(account) {
            vm.currentAccount = account;
        });

        function setActive (task, isActivated) {
            task.activated = isActivated;
            Task.update(task, function () {
                vm.loadAll();
                vm.clear();
            });
        }



        function clear () {
            vm.task = {
                id: null, code: null, name: null, taskCredit: null, status: null,
                projectId: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null
            };
        }


        vm.dtInstance = {};
        vm.searchText = '';
        vm.search = search;

        function search() {
            var resetPaging = true;
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
                url: 'api/tasks/search',
                data: function(d){
                    var params = {};

                    params.search = vm.searchText;

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

        vm.dtColumns = [
            DTColumnBuilder.newColumn('id').withTitle('#'),
            DTColumnBuilder.newColumn('code').withTitle('TASK CODE'),
            DTColumnBuilder.newColumn('projectCode').withTitle('PROJECT CODE'),
            DTColumnBuilder.newColumn('name').withTitle('TASK NAME'),
            DTColumnBuilder.newColumn('taskCredit').withTitle('TASK CREDIT'),
            DTColumnBuilder.newColumn('status').withTitle('STATUS').renderWith(function(data) {
                if(data === 'ACTIVE') {
                    return '<p class="active">Active</p>';
                }else {
                    return '<p class="inactive">Inactive</p>';
                }
            })
        ];

        function rowClick(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
            // Unbind first in order to avoid any duplicate handler (see https://github.com/l-lin/angular-datatables/issues/87)
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function() {
                var task = aData;
                // if(user['startDate']) {
                //     user['startDate'] = moment(user['startDate']).toDate();
                // }

                $scope.$apply(function() {
                    vm.createOrUpdate(task);
                });
            });
            return nRow;
        }



        function createOrUpdate(task) {

            if(!task) {
                task = {
                    id: null, code: null, name: null, taskCredit: null, projectId: null,
                    langKey: null, createdBy: null, createdDate: null,
                    lastModifiedBy: null, lastModifiedDate: null, status: 'ACTIVE'
                };
            }

            $uibModal.open({
                templateUrl: 'app/entities/task/task-dialog.html',
                controller: 'TaskDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                resolve: {
                    entity: function () {
                        return task;
                    }
                }
            }).result.then(function() {
                $state.go('task', null, { reload: true });
            }, function() {
                $state.go('task');
            });
        }


    }
})();
