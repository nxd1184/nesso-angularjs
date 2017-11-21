'use strict';

describe('Controller Tests', function() {

    describe('JobTeam Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockJobTeam, MockJob, MockTeam, MockProject;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockJobTeam = jasmine.createSpy('MockJobTeam');
            MockJob = jasmine.createSpy('MockJob');
            MockTeam = jasmine.createSpy('MockTeam');
            MockProject = jasmine.createSpy('MockProject');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'JobTeam': MockJobTeam,
                'Job': MockJob,
                'Team': MockTeam,
                'Project': MockProject
            };
            createController = function() {
                $injector.get('$controller')("JobTeamDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nessoApp:jobTeamUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
