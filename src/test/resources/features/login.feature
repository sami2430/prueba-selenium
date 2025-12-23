@login
Feature: Inicio de sesion

  Scenario: Login exitoso con credenciales validas
    Given el usuario esta en la pagina de login
    When ingresa email y contrasena validos
    Then se muestra la pantalla principal
