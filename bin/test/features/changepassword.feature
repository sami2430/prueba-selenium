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

  Scenario: Cambio con campos vacios
    When el usuario abre el formulario de cambiar contrasena
    And deja todos los campos de contrasena vacios
    And intenta guardar el cambio de contrasena
    Then se muestra mensaje de error de campos requeridos
    And el cambio no se realiza

  Scenario: Cancelar cambio de contrasena
    When el usuario abre el formulario de cambiar contrasena
    And ingresa nueva contrasena
    And cancela el cambio de contrasena
    Then el formulario se cierra sin cambiar la contrasena
    And la contrasena original se mantiene

  Scenario: Verificar contrasena antigua no funciona
    Given el usuario ha cambiado su contrasena exitosamente
    When cierra sesion
    And intenta iniciar sesion con la contrasena antigua
    Then el login falla con la contrasena antigua
    And se muestra mensaje de credenciales invalidas
