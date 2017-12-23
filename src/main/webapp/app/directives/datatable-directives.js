(function() {
    'use strict';
    angular
        .module('nessoApp')
        .directive('datatable', datatable);

    function datatable($http) {
        return {
            restrict: 'A',
            link: function ($scope, $e, attrs) {

                $scope.options = {};

                var sModel = '',
                    tableId = '#' + attrs['id'],
                    sOptions = {},
                    table = null;

                // Start with the defaults. Change this to your defaults.
                $scope.options = {
                    filter: false,
                    bLengthChange: false,
                    iDisplayLength: 30,
                    columns: _buildTableColumns(tableId),
                    order: _buildDefaultOrder(attrs),
                    stateSave: true,
                    fnDrawCallback: function () {
                        if ($(tableId + '_paginate li.paginate_button').size() > 3) {
                            $(tableId + '_paginate').show();
                        } else {
                            $(tableId + '_paginate').hide();
                        }
                    }
                };

                // If dtOptions is defined in the controller, extend our default option.
                if (typeof $scope['dtOptions'] !== 'undefined') {
                    angular.extend($scope.options, $scope.dtOptions);
                }

                if (attrs['dtoptions'] !== 'undefined') {
                    sOptions = $scope[attrs['dtoptions']];
                } else {
                    // Falling back to default option
                    sOptions = {};
                }

                if (attrs['dtmodel'] !== 'undefined') {
                    sModel = attrs['dtmodel'];
                } else {
                    throw "datatable model is not defined.";
                }

                if ((typeof sOptions['fnServerDataSource'] !== 'undefined') && (typeof sOptions['data'] === 'undefined')) {

                    _requestDataSource(sOptions['fnServerDataSource'], $scope, sModel, tableId);

                } else if (typeof sOptions['data'] !== 'undefined') {

                    $scope.options.data = sOptions['data'];

                } else {

                    throw "both fnServerDataSource and data options aren't defined in $scope.";

                }

                $scope.$watch('datas', function (datas, oldVal) {
                    if (datas) {
                        $scope.options.data = datas;
                        $scope.options.aaData = datas;

                        var events = $scope[attrs['tableEvents']],
                            atdataEvents = $scope[attrs['atdataEvents']];

                        if (table) {
                            for (var key in events) {
                                $(tableId + ' > tbody').off(key, 'tr');
                            }
                            $(tableId + ' > tbody').off('click', '.btn-table');
                            table.destroy();
                        }

                        if (atdataEvents && atdataEvents['post-load']) {
                            atdataEvents['post-load'](tableId, datas);
                        }

                        table = angular.element(tableId).DataTable($scope.options);

                        if (datas.length > 0) {
                            // Enable sort when data.
                            $(tableId + ' > thead th').each(function (i, row) {
                                $(this).removeClass('no-sort-table');
                            });
                            // Table columns event register.
                            _tableColumnEventRegister({
                                tableId: tableId,
                                table: table,
                                scope: $scope,
                                events: events
                            });
                        } else {
                            // Disable sort when no data.
                            $(tableId + ' > tbody tr').addClass('datatable-no-data');
                            $(tableId + ' > thead th').each(function (i, row) {
                                $(this).addClass('no-sort-table');
                            });
                            $(tableId + ' .dataTables_empty').text('No Data');
                        }
                        if (atdataEvents && atdataEvents['dom-loaded']) {
                            atdataEvents['dom-loaded'](tableId, datas);
                        }
                    } else {
                        $scope.options.language = {
                            sEmptyTable: '<i class="fa fa-refresh fa-spin"></i>'
                        };
                        table = angular.element(tableId).DataTable($scope.options);
                    }
                }, true);

                $scope.$watch('fnRefeshTable', function (newVal, oldVal) {
                    if (newVal) {
                        _requestDataSource(sOptions['fnServerDataSource'], $scope, sModel, tableId);
                        $scope.fnRefeshTable = false;
                    }
                }, true);
            }
        };

        function _requestDataSource(request, $scope, sModel, tableId) {
            $http(request).then(function (result) {
                if (result) {

                    var _data = result.data;
                    var paths = sModel.split('.');
                    for (var i = 0; i < paths.length; i++) {
                        _data = _data[paths[i]];
                    }

                    $scope.datas = _data;
                }
                if (typeof $scope.onTableLoad === 'function') {
                    $scope.onTableLoad(tableId, result.data);
                }
            });
        }

        function _buildDefaultOrder(attrs) {
            var order = [];
            if (attrs.order) {
                var orderArray = attrs.order.split(',');
                if (orderArray.length > 1) {
                    for (var i = 0; i < orderArray.length; i++) {
                        var orderArrayElement = orderArray[i].split('-'),
                            tempArray = [];
                        tempArray.push(orderArrayElement[0]);
                        tempArray.push(orderArrayElement[1]);
                        order.push(tempArray);
                    }
                } else {
                    order.push(orderArray[0].split('-'));
                }
            } else {
                order = [['0', 'asc']];
            }
            return order;
        }

        function _buildTableColumns(tableId) {
            var columns = [];
            $(tableId + ' > thead th').each(function (i, row) {
                var colattr = $(row).data(),
                    colResponsiveClass = '';

                if (typeof colattr.mresponsive !== 'undefined') {
                    colResponsiveClass = colattr.mresponsive;
                }

                var generalClass = colResponsiveClass;

                if (colattr.mdata.indexOf("()") > 1) {
                    var colFnName = colattr.mdata.substring(0, colattr.mdata.length - 2),
                        colLabel = colattr.mlabel,
                        colDynamicLabel = colattr.mdynamiclabel, /* field name in data object*/
                        colDisabled = colattr.mdisabled,
                        colHidden = colattr.mhidden,
                        colClass = (typeof colattr.mclass === 'undefined') ? 'btn btn-primary btn-table' : 'btn btn-' + colattr.mclass + ' btn-table',
                        colRenderWhen = colattr.renderWhen;
                    columns.push({
                        'className': colFnName + " " + generalClass,
                        'isFn': true,
                        'orderable': false,
                        'data': null,
                        'render': function (data, type, row) {
                            var mRender = "",
                                uniqueIdButtonClass = "";

                            // Handler for renderWhen attribute
                            if (colRenderWhen) {
                                var renderWhenSplit = colRenderWhen.split("=");
                                if (data[renderWhenSplit[0]] === JSON.parse(renderWhenSplit[1])) {
                                    mRender = "at-is-show";
                                } else {
                                    mRender = "at-is-hide";
                                }
                            }

                            // Handler for unique id to button
                            if (colattr.mid) {
                                uniqueIdButtonClass = data[colattr.mid];
                            }

                            var buttonClass = colFnName + ' ' + uniqueIdButtonClass + ' ' + colClass + ' ' + mRender;
                            if (colDisabled && data[colDisabled]) {
                                buttonClass  = buttonClass + ' disabled ';
                            }
                            if (colHidden && data[colHidden]) {
                                buttonClass  = buttonClass + ' hidden ';
                            }

                            var _label = colLabel;
                            if (colDynamicLabel) {
                                _label = data[colDynamicLabel];
                            }

                            return '<a style="color:white;" class="' + buttonClass + '">' + _label + '</a>';
                        }
                    });
                } else if (colattr.renderer) {
                    columns.push({
                        'className': generalClass,
                        'orderable': true,
                        'data': colattr.mdata,
                        'render': AT.TableColumnRenderers[colattr.renderer]
                    });
                } else {
                    columns.push({
                        'data': colattr.mdata,
                        'className': generalClass
                    });
                }
            });
            return columns;
        }


        function _tableColumnEventRegister(params) {
            var tableId = params.tableId,
                table = params.table,
                scope = params.scope,
                events = params.events;

            // Registering table events as: click on rows, double click on rows...
            for (var key in events) {
                $(tableId + ' > tbody').on(key, 'tr', function (e) {
                    if (!AT.ObjectUtils.getSelectionHtml().length > 0) {
                        scope.$apply(events[key](table.row(this).data()));
                    }
                    e.stopImmediatePropagation();
                })
            }

            // Registering table button events as: button remove, button details...
            $(tableId + ' > tbody').on('click', '.btn-table', function (e) {
                var targetClassArray = e.target.className.split(" "),
                    fnName = targetClassArray[0],
                    fn = scope[fnName],
                    rowData = table.row($(this).closest("tr")).data();
                if (typeof fn === 'function') {
                    scope.$apply(fn(rowData));
                    e.stopImmediatePropagation();
                }
            });
        }
    }
});
