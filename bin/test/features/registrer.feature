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

  Scenario: Registro fallido con contraseñas diferentes
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa contrasenas diferentes
    Then se muestra un mensaje de error de contrasenas
    And el registro no se completa

  Scenario: Registro fallido sin nombre
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa datos sin nombre
    Then se muestra un mensaje de error de campo requerido
    And el registro no se completa

  Scenario: Registro fallido sin email
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa datos sin email
    Then se muestra un mensaje de error de campo requerido
    And el registro no se completa

  Scenario: Registro fallido con campos vacios
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And deja todos los campos vacios
    Then se muestra un mensaje de error de campos requeridos
    And el registro no se completa

  Scenario: Registro fallido con email invalido
    Given el usuario se encuentra en la pantalla de login
    When abre el formulario de crear nuevo usuario
    And ingresa un email con formato invalido
    Then se muestra un mensaje de error de formato de email
    And el registro no se completa
