(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('FolderController', FolderController);

    FolderController.$inject = ['$state', '$uibModal', 'FolderService', 'moment', '$scope', '$timeout', 'AlertService'];

    function FolderController($state, $uibModal, FolderService, moment, $scope, $timeout, AlertService) {

        var vm = this;
        vm.project_tree             = $('#project_tree');
        vm.project_tree_object      = {};
        vm.root_node                = { id: "#", li_attr : { relative_path: "" } };
        vm.node_being_opened        = {};
        vm.node_being_selected      = {};
        vm.currentFileList          = [];
        vm.currentSelectedFileList  = [];
        vm.checkAll                 = false;
        vm.checkAllDisabled         = true;
        vm.checkCount               = 0;
        vm.folderList               = [];
        vm.deletedFiles             = [];

        vm.loadRootTree           = loadRootTree;
        vm.showResourceDateTime   = showResourceDateTime;
        vm.reloadCurrentDirectory = reloadCurrentDirectory;
        vm.delivery               = delivery;
        vm.checkAllFiles          = checkAllFiles;
        vm.checkboxIsChecked      = checkboxIsChecked;
        vm.selectNode             = selectNode;
        vm.deleteFiles            = deleteFiles;

        function showResourceDateTime(isoStr) {
            if (!isoStr)
                return "";

            return moment(isoStr,'YYYY-MM-DD HH:mm:ss:SSS ZZ').format("DD/MM/YYYY hh:mmA");
        }

        /*function search() {
            console.log("Searching user setting by " + vm.searchType);
            var resetPaging = false;
            vm.dtInstance.reloadData(searchCallback, resetPaging);
        }*/

        function loadRootTree() {
            vm.node_being_opened = vm.root_node;
            loadDirectories(vm.root_node);
        }

        function checkAllFiles() {
            vm.currentFileList.forEach(function(file) {
                file.checked = vm.checkAll;
            })

            if (vm.checkAll === true)
                vm.checkCount = vm.currentFileList.length;
            else
                vm.checkCount = 0;
        }

        function checkboxIsChecked(index) {
            if (vm.currentFileList[index].checked)
                vm.checkCount++;
            else
                vm.checkCount--;

            vm.checkAll = vm.checkCount === vm.currentFileList.length;
        }

        /************************* Handle open/select node in folder tree *************************/
        function buildFolderList(node) {
            vm.folderList = [];
            var ref = vm.project_tree.jstree(true);

            //vm.folderList.unshift(node.text);
            vm.folderList.unshift(node);
            for (var i = 0; i < node.parents.length - 1; ++i) {
                vm.folderList.unshift(ref.get_node(node.parents[i]));
            }
        }

        function onNodeOpen(event, data) {
           console.log("open node" + data.node.id);
            var ref = vm.project_tree.jstree(true);
            vm.node_being_opened = data.node;
            ref.delete_node(vm.node_being_opened.children);
            loadDirectories(vm.node_being_opened);
        }

        function onNodeSelected(event, data) {
            console.log("select node" + data.node.id);
            var ref = vm.project_tree.jstree(true);

            if (vm.node_being_selected === data.node) {
                if (data.node.state.opened) {
                    ref.close_node(data.node);
                    return;
                }
            }

            var ref = vm.project_tree.jstree(true);
            if (!data.node.state.opened) {
                ref.open_node(data.node);
            }

            vm.node_being_selected = data.node;
            vm.checkAll = false;
            loadFiles(vm.node_being_selected);
            buildFolderList(vm.node_being_selected);
        }

        function selectNode(node) {
            var ref = vm.project_tree.jstree(true);
            ref.deselect_node(vm.node_being_selected);
            ref.select_node(node);
        }
        /******************************************************************************************/



        /************************************ Load directory ************************************/
        function loadDirectories(node) {
            if (node.id === "#") {
                var ref = vm.project_tree.jstree(true);
                ref.delete_node(ref.get_node("#").children);
            }
            FolderService.getDirectories(node.li_attr.relative_path).then(onGetDirectoriesSuccess);
        }

        function onGetDirectoriesSuccess(response) {
            console.log("Get directories successfully");
            if (response.success ) {
                var directories = response.directories;
                for (var i = 0; i < directories.length; ++i) {
                    var ref = vm.project_tree.jstree();
                    var node = {
                                    text: directories[i].name,
                                    state:    {
                                        opened : false
                                    },
                                    li_attr : { relative_path: directories[i].relativePath }
                                };

                    if (directories[i].hasChild)
                        node.children = [''];

                    var result =  ref.create_node(vm.node_being_opened.id, node, 'last');
                }
            }
        }
        /************************************************************************************ßßßßß*****/



        /************************************ Load directory ************************************/
        function reloadCurrentDirectory() {
            if(vm.node_being_selected.li_attr)
                loadFiles(vm.node_being_selected);
        }

        function loadFiles(node) {
            vm.checkAll = false;
            vm.checkCount = 0;
            FolderService.getFiles(node.li_attr.relative_path).then(onGetFilesSuccess);
        }

        function onGetFilesSuccess(response) {
            console.log("Get files successfully");
            if (response.success) {
                vm.currentFileList = response.files;
                if (vm.currentFileList.length > 0)
                    vm.checkAllDisabled = false;
                else
                    vm.checkAllDisabled = true;
            }
        }
        /****************************************************************************************/



        /************************************ Deliver files ************************************/
        function delivery() {
            var movedFiles = [];
            vm.currentFileList.filter(
            function (file) {
                if(file.checked == 1) {
                    movedFiles.push(file.name + '.' + file.type);
                }
            });
            console.log(movedFiles);

            if (movedFiles.length > 0)
                FolderService.deliverFilesFromDoneFolder(movedFiles).then(onSuccessDeliverFiles, onFailedDeliverFiles);
            else
                AlertService.error("No file to be delivered");
        }

        function onSuccessDeliverFiles(response) {
            if (response.failedList.length > 0) {
                var error_message = "The following files are failed to be delivered:\n";
                for (var i = 0; i < response.failedList.length; ++i) {
                    error_message += " - " + response.failedList[i] + "\n";
                }
                AlertService.error(error_message);
            }
            else {
                AlertService.success("Delivered files successfully");
            }
        }

        function onFailedDeliverFiles(response) {
            AlertService.failed("Delivered files failed");
        }
        /*******************************************************************************************/


        /************************************ Delete files *****************************************/
        function deleteFiles() {
            vm.deletedFiles = [];
            vm.currentFileList.filter(
                function (file) {
                    if(file.checked == 1) {
                        vm.deletedFiles.push(file.name + '.' + file.type);
                    }
                }
            );

            console.log(vm.deletedFiles);

            if (vm.deletedFiles.length > 0)
                FolderService.deleteFiles(vm.deletedFiles).then(onSuccessDeleteFiles, onFailedDeleteFiles);
            else
                AlertService.error("No file to be delivered");
        }

        function onSuccessDeleteFiles(response) {
            if (response.failedList.length > 0) {
                var error_message = "The following files are failed to be deleted:\n";
                for (var i = 0; i < response.failedList.length; ++i) {
                    error_message += " - " + response.failedList[i] + "\n";
                    vm.deletedFiles = vm.deletedFiles.filter(function(item) { item !== response.failedList[i] });
                }
                AlertService.error(error_message);

            }
            else {
                AlertService.success("Deleted files successfully");
            }

            for (var i = 0; i < vm.deletedFiles.length; ++i)
                vm.currentFileList = vm.currentFileList.filter(function(item) { (item.name + '.' + item.type) !== vm.deletedFiles[i] });
        }

        function onFailedDeleteFiles(response) {
            AlertService.failed("Deleted files failed");
        }
        /*******************************************************************************************/


        /************************************ Initialize jstree ************************************/
        angular.element(document).ready(function () {
            // Initialize jstree object
            console.log("initialize Folder Tree");
            vm.project_tree.jstree({"core" : {
                                    "themes" : { "responsive": false },
                                    "check_callback" : true
                                },
                                "types" : {
                                    "default" : { "icon" : "fa fa-folder icon-state-warning icon-lg" },
                                    "file"    : { "icon" : "fa fa-file icon-state-warning icon-lg" }
                                },
                                "plugins": ["types"]
                            });
            vm.project_tree.on('open_node.jstree', onNodeOpen);
            vm.project_tree.on('select_node.jstree', onNodeSelected);

            //Load root directories
            loadRootTree();
        });
        /******************************************************************************************/
    }

})();
