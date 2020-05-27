#Author: kunal.patil@inovisionsoftware.com
Feature: Check countries returned by API
	Background:
		* url COUNTRIES_BASE
		* header Accept = 'application/json' 

  Scenario: Get all countries
    Given path '/rest/v2/all'
    And param fields = 'name;capital;region;cioc'
    When method get
    Then status 200
    	* print response[0].name
    
    Given path '/rest/v2/name/' + response[0].name 
    And param fields = 'name;capital;region;cioc'
    When method GET
    Then status 200
    	* print response
