(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('UserManagementController', UserManagementController);

    UserManagementController.$inject = ['$stateParams', 'AlertService', '$state', '$uibModal', 'DTOptionsBuilder', 'DTColumnBuilder', 'moment', '$scope', 'userService'];

    function UserManagementController($stateParams, AlertService, $state, $uibModal, DTOptionsBuilder, DTColumnBuilder, moment, $scope, userService) {
        var vm = this;

        vm.languages = null;
        vm.users = [];
        vm.links = null;
        vm.createOrUpdate = createOrUpdate;

        function onError(error) {
            AlertService.error(error.data.message);
        }


        vm.dtInstance = {};
        vm.searchText = $stateParams.searchText;
        vm.search = search;

        function _applyCriteriaOnUrl() {

        }

        function search() {
            var resetPaging = false;
            vm.dtInstance.reloadData(searchCallback, resetPaging);
        }

        function searchCallback(data) {

        }

        vm.dtOptions = DTOptionsBuilder
            .fromSource({
                headers: LA.RequestUtils.getHeaders(),
                url: 'api/users/search',
                data: function(d){
                    var query = {};

                    query.search = vm.searchText;

                    if(d.order && d.order.length) {
                        query.sort = d.columns[d.order[0].column].data + ',' + d.order[0].dir;
                        for(var i = 1; i < d.order.length; i++) {
                            query.sort += d.columns[d.order[i].column].data + ',' + d.order[i].dir;
                        }
                    }
                    query.page = d.start / d.length;
                    query.size = d.length;

                    return query;
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
            DTColumnBuilder.newColumn('login').withTitle('USER NAME'),
            DTColumnBuilder.newColumn('lastName').withTitle('FULL NAME'),
            DTColumnBuilder.newColumn('email').withTitle('EMAIL'),
            DTColumnBuilder.newColumn('capacity').withTitle('CAPACITY'),
            DTColumnBuilder.newColumn('authorities').withTitle('ROLE').renderWith(function(data) {
                var roles = '';
                var authorities = {
                    ROLE_USER: 'User',
                    ROLE_PROJECT_MANAGER: 'Project Manager',
                    ROLE_TEAM_LEADER: 'Team Leader',
                    ROLE_ADMIN: 'Admin',
                    ROLE_QC: 'QC'
                };

                if(data) {
                    for(var i = 0; i < data.length; i++) {
                        var roleName = authorities[data[i]];
                        if(roleName) {
                            roles += roleName;
                            if(i < data.length - 1) {
                                roles += ', ';
                            }
                        }
                    }
                }

                return roles;
            }),
            DTColumnBuilder.newColumn('teamName').withTitle('TEAM'),
            DTColumnBuilder.newColumn('startDate').withTitle('START DATE').renderWith(function(data) {
                var value = '';
                if(data) {
                    value = moment(data).format('DD/MM/YYYY');
                }
                return value;
            }),
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
                var user = aData;
                if(user['startDate']) {
                    user['startDate'] = moment(user['startDate']).toDate();
                }

                $scope.$apply(function() {
                    vm.createOrUpdate(user);
                });
            });
            return nRow;
        }



        function createOrUpdate(user) {

            if(!user) {
                user = {
                    id: null, login: null, firstName: null, lastName: null, email: null,
                    startDate: null, password: null,
                    activated: true, langKey: null, createdBy: null, createdDate: null,
                    lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                    resetKey: null, authorities: null, status: 'ACTIVE'
                };
            }

            $uibModal.open({
                templateUrl: 'app/admin/user-management/user-management-dialog.html',
                controller: 'UserManagementDialogController',
                controllerAs: 'vm',
                resolve: {
                    entity: function () {
                        return user;
                    }
                }
            }).result.then(function() {
                $state.go('user-management', null, { reload: true });
            }, function() {
                $state.go('user-management');
            });
        }


    }
})();
