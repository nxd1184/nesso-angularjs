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
        vm.loadAll = loadAll;
        vm.setActive = setActive;
        vm.tasks = [];
        vm.page = 1;
        vm.totalItems = null;
        vm.clear = clear;
        vm.links = null;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.transition = transition;
        vm.createOrUpdate = createOrUpdate;

        vm.loadAll();
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

        function loadAll () {
            Task.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.page = pagingParams.page;
            vm.tasks = data;
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }

        function clear () {
            vm.task = {
                id: null, code: null, name: null, taskCredit: null, status: null,
                projectId: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null
            };
        }

        function sort () {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
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
                url: 'api/tasks',
                data: function(d){
                    d.search = vm.searchText;
                },
                type: 'GET'
            })
            .withOption('bFilter', false)
            .withOption('rowCallback', rowClick);;

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

            if(task) {
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
            }else {
                $uibModal.open({
                    templateUrl: 'app/entities/task/task-dialog.html',
                    controller: 'TaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, code: null, name: null, taskCredit: null, projectId: null,
                                langKey: null, createdBy: null, createdDate: null,
                                lastModifiedBy: null, lastModifiedDate: null, status: 'ACTIVE'
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('task', null, { reload: true });
                }, function() {
                    $state.go('task');
                });
            }
        }


    }
})();
