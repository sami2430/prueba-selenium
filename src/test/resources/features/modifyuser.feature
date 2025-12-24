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
