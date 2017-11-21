'use strict';

describe('Controller Tests', function() {

    describe('JobTeamUserTask Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockJobTeamUserTask, MockJobTeamUser, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockJobTeamUserTask = jasmine.createSpy('MockJobTeamUserTask');
            MockJobTeamUser = jasmine.createSpy('MockJobTeamUser');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'JobTeamUserTask': MockJobTeamUserTask,
                'JobTeamUser': MockJobTeamUser,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("JobTeamUserTaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nessoApp:jobTeamUserTaskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
