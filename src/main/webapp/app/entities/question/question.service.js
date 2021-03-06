(function() {
    'use strict';
    angular
        .module('testifyApp')
        .factory('Question', Question);
    
    Question.$inject = ['$resource'];

    function Question ($resource) {
        var resourceUrl =  'api/questions/:id';
        
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                	if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getByQuestionnaire': {
                method: 'GET',
                isArray: true,
                url: 'api/questions/questionnaire/:id',
                transformResponse: function (data) {
                	if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
