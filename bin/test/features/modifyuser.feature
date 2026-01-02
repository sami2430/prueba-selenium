@modificar
Feature: Modificacion de informacion de usuario

  Background:
    Given el usuario ha iniciado sesion correctamente
    And el usuario se encuentra en la pantalla de perfil

  Scenario: Modificacion exitosa de los datos del usuario
    When el usuario abre el formulario de edicion de datos
    And modifica su nombre y apellido con datos validos
    And guarda los cambios del perfil
    Then se muestra un mensaje de confirmacion de modificacion exitosa
    And los datos actualizados se reflejan en el perfil del usuario

  Scenario: Modificacion erronea de datos del usuario
    When el usuario abre el formulario de edicion de datos
    And modifica los datos del usuario con valores invalidos
    And guarda los cambios del perfil
    Then el sistema permite guardar datos invalidos

  Scenario: Modificacion fallida con campos vacios
    When el usuario abre el formulario de edicion de datos
    And deja todos los campos vacios
    And guarda los cambios del perfil
    Then se muestra un mensaje de error de campos requeridos
    And los cambios no se guardan

  Scenario: Modificar solo nombre
    When el usuario abre el formulario de edicion de datos
    And modifica solo el nombre
    And guarda los cambios del perfil
    Then se muestra un mensaje de confirmacion de modificacion exitosa
    And solo el nombre se actualiza en el perfil

  Scenario: Modificar solo apellido
    When el usuario abre el formulario de edicion de datos
    And modifica solo el apellido
    And guarda los cambios del perfil
    Then se muestra un mensaje de confirmacion de modificacion exitosa
    And solo el apellido se actualiza en el perfil

  Scenario: Modificacion con email invalido
    When el usuario abre el formulario de edicion de datos
    And modifica el email con formato invalido
    And guarda los cambios del perfil
    Then se muestra un mensaje de error de formato de email
    And los cambios no se guardan

  Scenario: Cancelar modificacion
    When el usuario abre el formulario de edicion de datos
    And modifica los datos del usuario
    And cancela la modificacion
    Then el formulario se cierra sin guardar cambios
    And los datos originales se mantienen
