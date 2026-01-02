package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import pages.RegisterUserModal;
import utils.DriverManager;
import utils.TestDataGenerator;
import utils.TestDataGenerator.UserTestData;

import static org.junit.Assert.assertTrue;

public class RegisterUserSteps {

    private LoginPage loginPage;
    private RegisterUserModal registerUserModal;

    // =========================
    // CASO: Registro exitoso
    // =========================

    @Given("el usuario se encuentra en la pantalla de login")
    public void el_usuario_se_encuentra_en_la_pantalla_de_login() {
        loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
    }

    @When("abre el formulario de crear nuevo usuario")
    public void abre_el_formulario_de_crear_nuevo_usuario() {
        loginPage.clickCreateNewUser();
        registerUserModal = new RegisterUserModal(DriverManager.getDriver());
    }

    @When("ingresa datos validos de registro")
    public void ingresa_datos_validos_de_registro() {
        // Generar datos únicos de prueba para cada ejecución
        UserTestData userData = TestDataGenerator.generateUserTestData();

        registerUserModal.fillName(userData.getFirstName());
        registerUserModal.fillLastName(userData.getLastName());
        registerUserModal.fillEmail(userData.getEmail());
        registerUserModal.fillPassword(userData.getPassword());
        registerUserModal.submit();
    }

    /**
     * Valida el registro exitoso del usuario
     * Requerimientos: 3.1, 3.2
     */
    @Then("el usuario se registra exitosamente")
    public void el_usuario_se_registra_exitosamente() {
        assertTrue("El registro debe ser exitoso - debe mostrarse mensaje de éxito o redirección", 
                   registerUserModal.isRegistrationSuccessful());
    }

    // =========================
    // CASO: Registro erróneo - Datos inválidos
    // =========================

    /**
     * Paso para ingresar datos de registro inválidos
     * Requerimientos: 3.4
     */
    @When("ingresa datos invalidos de registro")
    public void ingresa_datos_invalidos_de_registro() {
        // Generar nombres realistas pero usar email inválido
        UserTestData userData = TestDataGenerator.generateUserTestData();
        String invalidEmail = TestDataGenerator.generateInvalidEmail();
        
        registerUserModal.fillName(userData.getFirstName());
        registerUserModal.fillLastName(userData.getLastName());
        registerUserModal.fillEmail(invalidEmail); // Email inválido
        registerUserModal.fillPassword(userData.getPassword());
        registerUserModal.submit();
    }

    /**
     * Paso para ingresar datos de registro con email duplicado
     * Requerimientos: 3.4
     */
    @When("ingresa un email ya registrado")
    public void ingresa_un_email_ya_registrado() {
        // Generar nombres realistas pero usar email existente
        UserTestData userData = TestDataGenerator.generateUserTestData();
        String existingEmail = TestDataGenerator.generateExistingEmail();
        
        registerUserModal.fillName(userData.getFirstName());
        registerUserModal.fillLastName(userData.getLastName());
        registerUserModal.fillEmail(existingEmail); // Email de usuario existente
        registerUserModal.fillPassword(userData.getPassword());
        registerUserModal.submit();
    }

    /**
     * Paso para ingresar contraseñas diferentes
     * Requerimientos: TC002
     */
    @When("ingresa contraseñas diferentes")
    public void ingresa_contrasenas_diferentes() {
        UserTestData userData = TestDataGenerator.generateUserTestData();
        
        registerUserModal.fillName(userData.getFirstName());
        registerUserModal.fillLastName(userData.getLastName());
        registerUserModal.fillEmail(userData.getEmail());
        registerUserModal.fillPassword("password123");
        registerUserModal.fillConfirmPassword("differentPassword456"); // Contraseña diferente
        registerUserModal.submit();
    }

    /**
     * Paso para registro con formato de email inválido
     * Requerimientos: TC005
     */
    @When("ingresa un email con formato invalido")
    public void ingresa_un_email_con_formato_invalido() {
        UserTestData userData = TestDataGenerator.generateUserTestData();
        
        registerUserModal.fillName(userData.getFirstName());
        registerUserModal.fillLastName(userData.getLastName());
        registerUserModal.fillEmail("email-invalido-sin-arroba-ni-dominio");
        registerUserModal.fillPassword(userData.getPassword());
        registerUserModal.submit();
    }

    // =========================
    // CASO: Registro erróneo - Campos faltantes
    // =========================

    /**
     * Paso para registro sin nombre
     * Requerimientos: TC003
     */
    @When("ingresa datos sin nombre")
    public void ingresa_datos_sin_nombre() {
        UserTestData userData = TestDataGenerator.generateUserTestData();
        
        // Dejar nombre vacío
        registerUserModal.fillName("");
        registerUserModal.fillLastName(userData.getLastName());
        registerUserModal.fillEmail(userData.getEmail());
        registerUserModal.fillPassword(userData.getPassword());
        registerUserModal.submit();
    }

    /**
     * Paso para registro sin email
     * Requerimientos: TC004
     */
    @When("ingresa datos sin email")
    public void ingresa_datos_sin_email() {
        UserTestData userData = TestDataGenerator.generateUserTestData();
        
        registerUserModal.fillName(userData.getFirstName());
        registerUserModal.fillLastName(userData.getLastName());
        // Dejar email vacío
        registerUserModal.fillEmail("");
        registerUserModal.fillPassword(userData.getPassword());
        registerUserModal.submit();
    }

    /**
     * Paso para registro con todos los campos vacíos
     * Requerimientos: TC006
     */
    @When("deja todos los campos vacios")
    public void deja_todos_los_campos_vacios() {
        // No llenar ningún campo, solo enviar
        registerUserModal.submit();
    }

    // =========================
    // VALIDACIONES DE ERROR
    // =========================

    /**
     * Valida escenarios de error en el registro
     * Requerimientos: 3.4
     */
    @Then("se muestra un mensaje de error de registro")
    public void se_muestra_un_mensaje_de_error_de_registro() {
        assertTrue("Debe mostrarse un mensaje de error de registro", 
                   registerUserModal.hasRegistrationError());
    }

    /**
     * Valida que el registro fue prevenido
     * Requerimientos: 3.4
     */
    @Then("el registro no se completa")
    public void el_registro_no_se_completa() {
        // El registro debe fallar - ya sea mensaje de error mostrado o modal aún abierto
        boolean hasError = registerUserModal.hasRegistrationError();
        boolean registrationFailed = !registerUserModal.isRegistrationSuccessful();
        
        assertTrue("El registro debe ser prevenido cuando se proporcionan datos inválidos", 
                   hasError || registrationFailed);
    }

    /**
     * Valida error de contraseñas no coincidentes
     * Requerimientos: TC002
     */
    @Then("se muestra un mensaje de error de contraseñas")
    public void se_muestra_un_mensaje_de_error_de_contrasenas() {
        assertTrue("Debe mostrarse error de contraseñas no coincidentes", 
                   registerUserModal.hasPasswordMismatchError());
    }

    /**
     * Valida error de campo requerido
     * Requerimientos: TC003, TC004
     */
    @Then("se muestra un mensaje de error de campo requerido")
    public void se_muestra_un_mensaje_de_error_de_campo_requerido() {
        assertTrue("Debe mostrarse error de campo requerido", 
                   registerUserModal.hasRequiredFieldError());
    }

    /**
     * Valida error de múltiples campos requeridos
     * Requerimientos: TC006
     */
    @Then("se muestra un mensaje de error de campos requeridos")
    public void se_muestra_un_mensaje_de_error_de_campos_requeridos() {
        assertTrue("Debe mostrarse error de múltiples campos requeridos", 
                   registerUserModal.hasRequiredFieldError());
    }

    /**
     * Valida error de formato de email
     * Requerimientos: TC005
     */
    @Then("se muestra un mensaje de error de formato de email")
    public void se_muestra_un_mensaje_de_error_de_formato_de_email() {
        assertTrue("Debe mostrarse error de formato de email", 
                   registerUserModal.hasEmailFormatError());
    }
}
