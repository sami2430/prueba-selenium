# Prueba Selenium – Sentra Tasks

Este proyecto corresponde a una automatización funcional del sistema **Sentra Tasks**, desarrollada como evaluación técnica del área de QA, utilizando **Selenium WebDriver**, **Java**, **Gradle** y **Cucumber (BDD)**.

El objetivo es validar los principales flujos del sistema desde la perspectiva de un QA, incluyendo escenarios exitosos y erróneos, aplicando buenas prácticas de automatización como **Page Object Model**, **Gherkin** y **código autodocumentado**.

---

## Sistema bajo prueba: Sentra Tasks

Sentra Tasks es una aplicación web que permite a los usuarios:

- Registrarse e iniciar sesión
- Modificar información de perfil y contraseña
- Crear tareas
- Visualizar la lista de tareas
- Ordenar tareas por título, prioridad y fecha de término
- Marcar tareas como completadas

---

## Tecnologías utilizadas

- **Java 17**
- **Selenium WebDriver**
- **Cucumber (BDD)**
- **JUnit**
- **Gradle**
- **Google Chrome**

---

## Instalación

### Requisitos previos

- Java JDK 17 instalado y configurado en el PATH
- Gradle instalado
- Google Chrome
- Visual Studio Code (recomendado)

### Clonar o descargar el proyecto

El proyecto puede ser entregado como carpeta ZIP o repositorio Git.

---
## Estructura del proyecto

La estructura del proyecto es un poco extensa, pero es más fácil de entender al visualizarla.

```plaintext
src
└── test
    ├── java
    │   ├── pages
    │   │   ├── BasePage.java
    │   │   ├── LoginPage.java
    │   │   ├── HomePage.java
    │   │   ├── ProfilePage.java
    │   │   ├── TasksPage.java
    │   │   ├── RegisterUserModal.java
    │   │   ├── EditUserModal.java
    │   │   ├── ChangePasswordModal.java
    │   │   └── CreateTaskModal.java
    │   ├── steps
    │   │   ├── LoginSteps.java
    │   │   ├── RegisterUserSteps.java
    │   │   ├── ModifyUserSteps.java
    │   │   ├── ChangePasswordSteps.java
    │   │   ├── CreateTaskSteps.java
    │   │   ├── TasksSteps.java
    │   │   ├── SortTaskSteps.java
    │   │   └── CommonSteps.java
    │   ├── runners
    │   │   └── TestRunner.java
    │   ├── tests
    │   │   ├── DateSortingPropertyTest.java
    │   │   ├── TitleSortingPropertyTest.java
    │   │   ├── LoginValidationPropertyTest.java
    │   │   ├── RegistrationValidationPropertyTest.java
    │   │   ├── TaskCreationPropertyTest.java
    │   │   └── DataUniquenessPropertyTest.java
    │   └── utils
    │       ├── DriverManager.java
    │       └── TestDataGenerator.java
    └── resources
        └── features
            ├── login.feature
            ├── registrer.feature
            ├── modifyuser.feature
            ├── changepassword.feature
            ├── create_task.feature
            └── tasks_list.feature
```

## Descripción de carpetas

**pages/**  
Contiene las clases Page Object y Modales, encapsulando los localizadores y acciones de cada pantalla.

**steps/**  
Implementación de los pasos definidos en los archivos Gherkin (@Given, @When, @Then).

**runners/**  
Contiene el TestRunner encargado de ejecutar los escenarios de Cucumber.

**tests/**  
Contiene las pruebas basadas en propiedades (Property-Based Tests) para validación robusta de funcionalidades.

**utils/**  
Manejo centralizado del WebDriver y generación de datos de prueba únicos.

**resources/features/**  
Archivos .feature escritos en lenguaje Gherkin, organizados por funcionalidad.

## Funcionalidades automatizadas

- **Registro de usuario**
- **Inicio de sesión** (escenario exitoso y erróneo)
- **Modificación de información de usuario** (OK y erróneo)
- **Cambio de contraseña**
- **Visualización de lista de tareas** con validación
- **Creación de nueva tarea** (OK y errónea)
- **Ordenamiento de tareas por:**
  - Título
  - Prioridad
  - Fecha de término
- **Pruebas basadas en propiedades** para validación robusta
- **Generación de datos únicos** para evitar conflictos entre pruebas

## Notas

El proyecto utiliza Page Object Model para facilitar mantenimiento y escalabilidad.

Los escenarios están escritos en Gherkin para mayor legibilidad.

Se incluyen validaciones funcionales reales según los criterios de evaluación.

## Ejecución del proyecto

Desde la carpeta raíz del proyecto, ejecutar en consola:
*Nota* En el testrunner se dejaron algunas ejecuciones comentadas por si se quería correr algun flujo en específico sin que se mezclaran entre sí 
Para ejectuar escenarios específicos se dejan ejemplos abajo, además del comando general.

```bash
gradle clean
gradle test


gradle test "-Dcucumber.filter.tags=@login"
gradle test "-Dcucumber.filter.tags=@registro"
gradle test "-Dcucumber.filter.tags=@modificar"

