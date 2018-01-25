(function() {
    'use strict';

    angular
        .module('nessoApp')
        .controller('FolderController', FolderController);

    FolderController.$inject = ['$state', '$uibModal', 'FolderService', 'moment', '$scope', '$timeout'];

    function FolderController($state, $uibModal, FolderService, moment, $scope, $timeout) {

        var vm = this;
        vm.project_tree        = $('#project_tree');
        vm.project_tree_object = {};
        vm.root_node           = { id: "#", li_attr : { relative_path: "" } };
        vm.node_being_opened   = {};
        vm.node_being_selected = {};
        vm.loadRootTree        = loadRootTree;
        vm.currentFileList     = [];
        vm.checkAll            = false;

        vm.showResourceDateTime   = showResourceDateTime;
        vm.reloadCurrentDirectory = reloadCurrentDirectory;
        vm.delivery               = delivery;
        vm.checkAllFiles          = checkAllFiles;

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
        }

        function delivery() {
            var movedFiles = [];
            vm.currentFileList.filter(
            function (file) {
                if(file.checked == 1) {
                    movedFiles.push(file.name + '.' + file.type);
                }
            });
            console.log(movedFiles);
            //FolderService.delivery(movedFiles).then(onGetDirectoriesSuccess);
        }

        function onNodeOpen(event, data) {
            var ref = vm.project_tree.jstree(true);
            console.log("open node" + data.node.id);
            vm.node_being_opened = data.node;
            ref.delete_node(data.node.children);
            loadDirectories(vm.node_being_opened);
        }

        function onNodeSelected(event, data) {
            console.log("select node" + data.node.id);
            var ref = vm.project_tree.jstree(true);
            vm.node_being_selected = ref.get_node(ref.get_selected());
            loadFiles(vm.node_being_selected);
        }


        function loadDirectories(node) {
            if (node.id === "#") {
                var ref = vm.project_tree.jstree(true);
                ref.delete_node(ref.get_node("#").children);
            }
            FolderService.getDirectories(node.li_attr.relative_path).then(onGetDirectoriesSuccess);
        }

        function onGetDirectoriesSuccess(response) {
            console.log("get Directories successfully");
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
                    console.log(result);
                }
            }
        }

        function reloadCurrentDirectory() {
            vm.checkAll = false;
            loadFiles(vm.node_being_selected);
        }

        function loadFiles(node) {
            FolderService.getFiles(node.li_attr.relative_path).then(onGetFilesSuccess);
        }

        function onGetFilesSuccess(response) {
            console.log("get Files successfully");
            if (response.success) {
                vm.currentFileList = response.files;
            }
        }



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
    }

})();
