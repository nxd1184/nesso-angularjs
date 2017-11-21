'use strict';

describe('Controller Tests', function() {

    describe('JobTeamUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockJobTeamUser, MockJobTeam, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockJobTeamUser = jasmine.createSpy('MockJobTeamUser');
            MockJobTeam = jasmine.createSpy('MockJobTeam');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'JobTeamUser': MockJobTeamUser,
                'JobTeam': MockJobTeam,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("JobTeamUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nessoApp:jobTeamUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
