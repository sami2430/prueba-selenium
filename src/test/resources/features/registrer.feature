@registro
Feature: Registro de usuario

  Scenario: Registro exitoso de un nuevo usuario
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa datos validos de registro
