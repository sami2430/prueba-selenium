@login
Feature: Inicio de sesion

  Scenario: Login exitoso con credenciales validas
    Given el usuario esta en la pagina de login
    When ingresa email y contrasena validos
    Then se muestra la pantalla principal

  Scenario: Login fallido con credenciales invalidas
    Given el usuario esta en la pagina de login
    When ingresa credenciales invalidas
    Then se muestra un mensaje de error de login
    And permanece en la pagina de login

  Scenario: Login fallido con credenciales vacias
    Given el usuario esta en la pagina de login
    When ingresa credenciales vacias
    Then se muestra un mensaje de error de login
    And permanece en la pagina de login
