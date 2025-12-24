@password
Feature: Cambio de contrasena

  Background:
    Given el usuario ha iniciado sesion correctamente
    And el usuario se encuentra en la pantalla de perfil

  Scenario: Cambio de contrasena exitoso
    When el usuario abre el formulario de cambiar contrasena
    And ingresa contrasena actual y nueva contrasena valida
    Then se muestra mensaje de contrasena modificada correctamente

  Scenario: Cambio de contrasena con error
    When el usuario abre el formulario de cambiar contrasena
    And ingresa contrasenas nuevas distintas
    Then se muestra mensaje de error de contrasena
