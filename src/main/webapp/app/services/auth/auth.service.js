(function() {
    'use strict';

    angular
        .module('nessoApp')
        .factory('Auth', Auth);

    Auth.$inject = ['$rootScope', '$state', '$sessionStorage', '$q', 'Principal', 'AuthServerProvider', 'Account', 'LoginService', 'Register', 'Activate', 'Password', 'PasswordResetInit', 'PasswordResetFinish', 'JhiTrackerService'];

    function Auth ($rootScope, $state, $sessionStorage, $q, Principal, AuthServerProvider, Account, LoginService, Register, Activate, Password, PasswordResetInit, PasswordResetFinish, JhiTrackerService) {
        var service = {
            activateAccount: activateAccount,
            authorize: authorize,
            changePassword: changePassword,
            createAccount: createAccount,
            getPreviousState: getPreviousState,
            login: login,
            logout: logout,
            loginWithToken: loginWithToken,
            resetPasswordFinish: resetPasswordFinish,
            resetPasswordInit: resetPasswordInit,
            resetPreviousState: resetPreviousState,
            storePreviousState: storePreviousState,
            updateAccount: updateAccount
        };

        return service;

        function activateAccount (key, callback) {
            var cb = callback || angular.noop;

            return Activate.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function authorize (force) {
            var authReturn = Principal.identity(force).then(authThen);

            return authReturn;

            function authThen () {
                var isAuthenticated = Principal.isAuthenticated();

                function getDefaultHome() {
                    var defaultHome = {
                            "ROLE_USER": "home",
                            "ROLE_PROJECT_MANAGER": "report",
                            "ROLE_TEAM_LEADER": "folder",
                            "ROLE_ADMIN": "user-setting",
                            "ROLE_FREELANCER": "check-in",
                    }

                    if (Principal.hasAnyAuthority(['ROLE_USER'])) {
                        return defaultHome['ROLE_USER'];
                    }
                    else if (Principal.hasAnyAuthority(['ROLE_PROJECT_MANAGER'])) {
                        return defaultHome['ROLE_PROJECT_MANAGER'];
                    }
                    else if (Principal.hasAnyAuthority(['ROLE_TEAM_LEADER'])) {
                        return defaultHome['ROLE_TEAM_LEADER'];
                    }
                    else if (Principal.hasAnyAuthority(['ROLE_ADMIN'])) {
                        return defaultHome['ROLE_ADMIN'];
                    }
                    else if (Principal.hasAnyAuthority(['ROLE_FREELANCER'])) {
                        return defaultHome['ROLE_FREELANCER'];
                    }
                }

                function shouldBeRedirected() {
                    return !Principal.hasAnyAuthority(['ROLE_USER']) && $rootScope.toState.name === 'home';
                }

                // an authenticated user can't access to login and register pages
                if (isAuthenticated && $rootScope.toState.parent === 'account' && ($rootScope.toState.name === 'login' || $rootScope.toState.name === 'register' || $rootScope.toState.name === 'social-auth')) {
                    $state.go('home');
                }
                // each user has its own default page to be loaded
                else if (isAuthenticated && shouldBeRedirected()) {
                    $state.go(getDefaultHome());
                }

                // recover and clear previousState after external login redirect (e.g. oauth2)
                if (isAuthenticated && !$rootScope.fromState.name && getPreviousState()) {
                    var previousState = getPreviousState();
                    resetPreviousState();
                    $state.go(previousState.name, previousState.params);
                }

                if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 && !Principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
                    if (isAuthenticated) {
                        // user is signed in but not authorized for desired state
                        $state.go('accessdenied');
                    }
                    else {
                        // user is not authenticated. stow the state they wanted before you
                        // send them to the login service, so you can return them when you're done
                        storePreviousState($rootScope.toState.name, $rootScope.toStateParams);

                        // now, send them to the signin state so they can log in
                        $state.go('accessdenied').then(function() {
                            LoginService.open();
                        });
                    }
                }
            }
        }

        function changePassword (currenPassword, newPassword, confirmPassword, callback) {
            var cb = callback || angular.noop;

            return Password.save({
                currentPassword: currenPassword,
                newPassword: newPassword,
                confirmPassword: confirmPassword
            }, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function createAccount (account, callback) {
            var cb = callback || angular.noop;

            return Register.save(account,
                function () {
                    return cb(account);
                },
                function (err) {
                    this.logout();
                    return cb(err);
                }.bind(this)).$promise;
        }

        function login (credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            AuthServerProvider.login(credentials)
                .then(loginThen)
                .catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            function loginThen (data) {
                Principal.identity(true).then(function(account) {
                    JhiTrackerService.sendActivity();
                    deferred.resolve(data);
                });
                return cb();
            }

            return deferred.promise;
        }

        function loginWithToken(jwt, rememberMe) {
            return AuthServerProvider.loginWithToken(jwt, rememberMe);
        }

        function logout () {
            AuthServerProvider.logout();
            Principal.authenticate(null);
        }

        function resetPasswordFinish (keyAndPassword, callback) {
            var cb = callback || angular.noop;

            return PasswordResetFinish.save(keyAndPassword, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function resetPasswordInit (mail, callback) {
            var cb = callback || angular.noop;

            return PasswordResetInit.save(mail, function() {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function updateAccount (account, callback) {
            var cb = callback || angular.noop;

            return Account.save(account,
                function () {
                    return cb(account);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function getPreviousState() {
            var previousState = $sessionStorage.previousState;
            return previousState;
        }

        function resetPreviousState() {
            delete $sessionStorage.previousState;
        }

        function storePreviousState(previousStateName, previousStateParams) {
            var previousState = { "name": previousStateName, "params": previousStateParams };
            $sessionStorage.previousState = previousState;
        }
    }
})();
