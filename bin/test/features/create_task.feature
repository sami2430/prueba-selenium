@createTask
Feature: Creacion de nueva tarea

  Background:
    Given el usuario ha iniciado sesion correctamente
    And el usuario se encuentra en la pantalla home

  Scenario: Creacion exitosa de una nueva tarea
    When el usuario abre el formulario de nueva tarea
    And ingresa datos validos de la tarea
    And guarda la nueva tarea
    Then la tarea se muestra en la lista de tareas

  Scenario: Creacion fallida de tarea sin titulo
    When el usuario abre el formulario de nueva tarea
    And intenta crear una tarea sin titulo
    Then la tarea no se crea