@something
Feature: Test

  Scenario: Test Something
    Given user is in "home" page
    When user navigate to "akun" page
    And user is in "akun_onboarding" page
    And user click "akun_onboarding_daftar_akun_button" element
    Then user is in "daftar" page
