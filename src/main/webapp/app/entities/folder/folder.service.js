(function() {
    'use strict';
    angular
        .module('nessoApp')
        .factory('FolderService', FolderService);

    FolderService.$inject = ['$http', '$q'];

    function FolderService ($http, $q) {
        var service = {
            getDirectories: getDirectories,
            getFiles: getFiles
        };

        function getDirectories(path) {
            var url = 'api/nfs/directories?path=' + path; 
            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        function getFiles(path) {
            var url = 'api/nfs/files?path=' + path; 
            var defer = $q.defer();
            $http(LA.RequestUtils.get(url)).then(function (result) {
                defer.resolve(result.data);
            }, function(error) {
                defer.reject(error);
            });

            return defer.promise;
        }

        return service;
    }
})();