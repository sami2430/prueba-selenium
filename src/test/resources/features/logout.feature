@logout
Feature: Logout de usuario

  Scenario: Logout exitoso
    Given el usuario esta logueado en el sistema
    When hace click en logout
    Then el usuario es redirigido a la pantalla de login
    And la sesion se cierra correctamente

  Scenario: Verificar acceso despues de logout
    Given el usuario esta logueado en el sistema
    When hace click en logout
    And intenta acceder a una pagina protegida
    Then es redirigido automaticamente al login