'use strict';

describe('Controller Tests', function() {

    describe('JobTeamUserTaskTracking Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockJobTeamUserTaskTracking, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockJobTeamUserTaskTracking = jasmine.createSpy('MockJobTeamUserTaskTracking');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'JobTeamUserTaskTracking': MockJobTeamUserTaskTracking,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("JobTeamUserTaskTrackingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nessoApp:jobTeamUserTaskTrackingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
