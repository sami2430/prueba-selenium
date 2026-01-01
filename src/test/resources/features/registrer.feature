@registro
Feature: Registro de usuario

  Scenario: Registro exitoso de un nuevo usuario
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa datos validos de registro
    Then el usuario se registra exitosamente

  Scenario: Registro fallido con datos invalidos
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa datos invalidos de registro
    Then se muestra un mensaje de error de registro
    And el registro no se completa

  Scenario: Registro fallido con email duplicado
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa un email ya registrado
    Then se muestra un mensaje de error de registro
    And el registro no se completa
