@tasks
Feature: Lista de tareas

Background:
  Given el usuario ha iniciado sesion correctamente
  And el usuario se encuentra en la pantalla Home

@tasksList
Scenario: Visualizar lista de tareas con validacion
  Given el usuario ha iniciado sesion correctamente
  When el usuario accede a la lista de tareas
  Then se muestra la lista de tareas
  And cada tarea tiene titulo fecha creacion fecha vencimiento y prioridad

@createTask
Scenario: Crear nueva tarea exitosamente
  When el usuario crea una nueva tarea con datos validos
  Then la tarea se muestra en la lista

@createTaskError
Scenario: Crear nueva tarea sin titulo
  When el usuario intenta crear una tarea sin titulo
  Then la tarea no es creada

@sortTitle
Scenario: Ordenar tareas por titulo
  When ordena las tareas por titulo
  Then las tareas quedan ordenadas alfabeticamente por titulo

@sortPriority
Scenario: Ordenar tareas por prioridad
  When ordena las tareas por prioridad
  Then las tareas quedan ordenadas por prioridad

@sortDate
Scenario: Ordenar tareas por fecha termino
  When ordena las tareas por fecha termino
  Then las tareas quedan ordenadas por fecha termino
