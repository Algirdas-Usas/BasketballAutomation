Feature: User Registration
  As a user, I want to create an account on Basketball England.

  Scenario: Create user successfully
    Given The user is on the registration page
    When The user fills in all required fields correctly
    When The user accepts the terms and conditions
    When The user clicks Join
    Then A new account is created successfully

  Scenario: Create user, missing last name
    Given The user is on the registration page
    When The user leaves the last name empty
    When The user clicks Join
    Then An error message for last name is displayed

  Scenario: Create user, passwords do not match
    Given The user is on the registration page
    When The user enters different passwords
    When The user clicks Join
    Then An error message for password mismatch is displayed

  Scenario: Create user, terms not accepted
    Given The user is on the registration page
    When The user fills in everything except accepting the terms
    When The user clicks Join
    Then An error message for terms and conditions is displayed