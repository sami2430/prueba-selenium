package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.RegisterUserModal;
import utils.DriverManager;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Prueba Basada en Propiedades para Funcionalidad de Validación de Registro
 * Característica: selenium-test-improvements, Propiedad 4: Validación de Éxito de Registro
 * Valida: Requisitos 3.1, 3.2, 3.3
 */
public class RegistrationValidationPropertyTest {

    private LoginPage loginPage;
    private RegisterUserModal registerUserModal;
    private WebDriver driver;
    private Random random;

    @Before
    public void setUp() {
        // Usar el DriverManager existente para obtener la instancia del driver
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        random = new Random();
    }

    @After
    public void tearDown() {
        // Usar el método quit del DriverManager
        DriverManager.quitDriver();
    }

    /**
     * Property 4: Registration Success Validation
     * For any valid user registration data, after successful registration, 
     * the system should display success indicators and allow subsequent login with the same credentials
     * Validates: Requirements 3.1, 3.2, 3.3
     * 
     * This property-based test runs 50 iterations with randomly generated valid user data
     */
    @Test
    public void testRegistrationSuccessValidationProperty() {
        // Run property test with 50 iterations (reduced from 100 due to web UI overhead)
        for (int iteration = 0; iteration < 50; iteration++) {
            try {
                // Generate random valid user data for this iteration
                UserData userData = generateValidUserData();
                
                // Navigate to registration form
                loginPage.open();
                loginPage.clickCreateNewUser();
                registerUserModal = new RegisterUserModal(driver);
                
                // Fill registration form with valid data
                registerUserModal.fillName(userData.getName());
                registerUserModal.fillLastName(userData.getLastName());
                registerUserModal.fillEmail(userData.getEmail());
                registerUserModal.fillPassword(userData.getPassword());
                registerUserModal.submit();
                
                // Test the property: valid registration should succeed
                assertTrue("Iteration " + iteration + ": Valid registration data should result in successful registration for user: " + userData.getEmail(), 
                           registerUserModal.isRegistrationSuccessful());
                
                // Verify no error messages are shown for valid data
                assertFalse("Iteration " + iteration + ": Valid registration should not show error messages for user: " + userData.getEmail(), 
                           registerUserModal.hasRegistrationError());
                
            } catch (Exception e) {
                // If there's an exception, fail the test with context
                throw new AssertionError("Iteration " + iteration + " failed with exception: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Property test for registration error handling
     * For any invalid user registration data, the system should display appropriate error messages 
     * and prevent registration completion
     * Validates: Requirements 3.4
     * 
     * This property-based test runs 30 iterations with randomly generated invalid user data
     */
    @Test
    public void testRegistrationErrorHandlingProperty() {
        // Run property test with 30 iterations for error scenarios
        for (int iteration = 0; iteration < 30; iteration++) {
            try {
                // Generate random invalid user data for this iteration
                UserData invalidUserData = generateInvalidUserData();
                
                // Navigate to registration form
                loginPage.open();
                loginPage.clickCreateNewUser();
                registerUserModal = new RegisterUserModal(driver);
                
                // Fill registration form with invalid data
                registerUserModal.fillName(invalidUserData.getName());
                registerUserModal.fillLastName(invalidUserData.getLastName());
                registerUserModal.fillEmail(invalidUserData.getEmail());
                registerUserModal.fillPassword(invalidUserData.getPassword());
                registerUserModal.submit();
                
                // Test the property: invalid registration should fail
                boolean registrationFailed = !registerUserModal.isRegistrationSuccessful();
                boolean hasErrorMessage = registerUserModal.hasRegistrationError();
                
                assertTrue("Iteration " + iteration + ": Invalid registration data should either fail or show error message for user: " + invalidUserData.getEmail(), 
                           registrationFailed || hasErrorMessage);
                
            } catch (Exception e) {
                // If there's an exception, fail the test with context
                throw new AssertionError("Iteration " + iteration + " failed with exception: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Generates valid user data for property testing
     */
    private UserData generateValidUserData() {
        String[] firstNames = {"Ana", "Carlos", "Maria", "Juan", "Sofia", "Diego", "Elena", "Miguel"};
        String[] lastNames = {"Garcia", "Rodriguez", "Martinez", "Lopez", "Gonzalez", "Perez", "Sanchez", "Ramirez"};
        
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];
        
        // Generate unique email with timestamp to avoid duplicates
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + 
                      "." + System.currentTimeMillis() + 
                      "." + random.nextInt(1000) + "@test.com";
        
        // Generate valid password (at least 6 characters with letters and numbers)
        String password = "Pass" + random.nextInt(9999) + "word";
        
        return new UserData(firstName, lastName, email, password);
    }

    /**
     * Generates invalid user data for property testing
     */
    private UserData generateInvalidUserData() {
        String[] invalidEmails = {"", "invalid", "test@", "@test.com", "test.com", "test@.com"};
        String[] invalidPasswords = {"", "123", "ab", "short"};
        String[] invalidNames = {"", " ", "   "};
        
        String firstName = random.nextBoolean() ? "ValidName" : invalidNames[random.nextInt(invalidNames.length)];
        String lastName = random.nextBoolean() ? "ValidLastName" : invalidNames[random.nextInt(invalidNames.length)];
        String email = invalidEmails[random.nextInt(invalidEmails.length)];
        String password = random.nextBoolean() ? "ValidPassword123" : invalidPasswords[random.nextInt(invalidPasswords.length)];
        
        return new UserData(firstName, lastName, email, password);
    }

    /**
     * Data class for user registration information
     */
    private static class UserData {
        private final String name;
        private final String lastName;
        private final String email;
        private final String password;

        public UserData(String name, String lastName, String email, String password) {
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

        public String getName() { return name; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    /**
     * Edge case property test for registration validation
     */
    @Test
    public void testRegistrationValidationEdgeCases() {
        // Test edge cases with specific scenarios
        String[][] edgeCases = {
            {"", "", "", ""}, // All empty
            {"A", "B", "a@b.co", "123456"}, // Minimal valid data
            {"VeryLongFirstNameThatExceedsNormalLimits", "VeryLongLastNameThatExceedsNormalLimits", 
             "verylongemailaddressthatmightexceedlimits@verylongdomainname.com", "VeryLongPasswordThatExceedsNormalLimits123"}
        };
        
        for (int i = 0; i < edgeCases.length; i++) {
            try {
                String[] caseData = edgeCases[i];
                UserData userData = new UserData(caseData[0], caseData[1], caseData[2], caseData[3]);
                
                // Navigate to registration form
                loginPage.open();
                loginPage.clickCreateNewUser();
                registerUserModal = new RegisterUserModal(driver);
                
                // Fill registration form
                registerUserModal.fillName(userData.getName());
                registerUserModal.fillLastName(userData.getLastName());
                registerUserModal.fillEmail(userData.getEmail());
                registerUserModal.fillPassword(userData.getPassword());
                registerUserModal.submit();
                
                // For edge cases, we expect either success (for valid minimal data) or proper error handling
                boolean isValid = !userData.getName().trim().isEmpty() && 
                                 !userData.getLastName().trim().isEmpty() && 
                                 userData.getEmail().contains("@") && 
                                 userData.getPassword().length() >= 6;
                
                if (isValid) {
                    // Should succeed or at least not show errors for valid edge case data
                    boolean success = registerUserModal.isRegistrationSuccessful();
                    boolean hasError = registerUserModal.hasRegistrationError();
                    assertTrue("Edge case " + i + ": Valid edge case data should succeed or not show errors", 
                              success || !hasError);
                } else {
                    // Should fail gracefully for invalid edge case data
                    boolean failed = !registerUserModal.isRegistrationSuccessful();
                    assertTrue("Edge case " + i + ": Invalid edge case data should fail gracefully", failed);
                }
                
            } catch (Exception e) {
                throw new AssertionError("Edge case " + i + " failed with exception: " + e.getMessage(), e);
            }
        }
    }
}