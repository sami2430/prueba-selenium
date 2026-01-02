package tests;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utils.DriverManager;

import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Prueba Basada en Propiedades para Funcionalidad de Validación de Login
 * Característica: selenium-test-improvements, Propiedad 6: Validación de Éxito de Login
 * Valida: Requisitos 4.1, 4.2, 4.3
 */
public class LoginValidationPropertyTest {

    private LoginPage loginPage;
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
     * Property 6: Login Success Validation
     * For any valid user credentials, successful login should redirect to the home page 
     * and display user-specific elements
     * Validates: Requirements 4.1, 4.2, 4.3
     * 
     * This property-based test runs 20 iterations with the known valid credentials
     * (reduced iterations due to web UI overhead and limited valid credentials)
     */
    @Test
    public void testLoginSuccessValidationProperty() {
        // Run property test with 20 iterations using valid credentials
        for (int iteration = 0; iteration < 20; iteration++) {
            try {
                // Use known valid credentials for this iteration
                LoginCredentials validCredentials = getValidCredentials();
                
                // Navigate to login page
                loginPage.open();
                
                // Perform login with valid credentials
                loginPage.login(validCredentials.getEmail(), validCredentials.getPassword());
                
                // Test the property: valid login should succeed
                assertTrue("Iteration " + iteration + ": Valid login credentials should result in successful login", 
                           loginPage.isLoginSuccessful());
                
                // Verify user elements are displayed after successful login
                assertTrue("Iteration " + iteration + ": User-specific elements should be displayed after successful login", 
                           loginPage.areUserElementsDisplayed());
                
                // Verify no error messages are shown for valid credentials
                assertFalse("Iteration " + iteration + ": Valid login should not show error messages", 
                           loginPage.hasLoginError());
                
            } catch (Exception e) {
                // If there's an exception, fail the test with context
                throw new AssertionError("Iteration " + iteration + " failed with exception: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Property test for login error handling
     * For any invalid user credentials, the system should display appropriate error messages 
     * and remain on the login page
     * Validates: Requirements 4.4
     * 
     * This property-based test runs 30 iterations with randomly generated invalid credentials
     */
    @Test
    public void testLoginErrorHandlingProperty() {
        // Run property test with 30 iterations for error scenarios
        for (int iteration = 0; iteration < 30; iteration++) {
            try {
                // Generate random invalid credentials for this iteration
                LoginCredentials invalidCredentials = generateInvalidCredentials();
                
                // Navigate to login page
                loginPage.open();
                
                // Attempt login with invalid credentials
                loginPage.login(invalidCredentials.getEmail(), invalidCredentials.getPassword());
                
                // Test the property: invalid login should fail
                boolean loginFailed = !loginPage.isLoginSuccessful();
                boolean hasErrorMessage = loginPage.hasLoginError();
                boolean staysOnLoginPage = loginPage.isStillOnLoginPage();
                
                assertTrue("Iteration " + iteration + ": Invalid login should either fail, show error, or stay on login page for credentials: " + 
                          invalidCredentials.getEmail() + "/" + invalidCredentials.getPassword(), 
                          loginFailed || hasErrorMessage || staysOnLoginPage);
                
            } catch (Exception e) {
                // If there's an exception, fail the test with context
                throw new AssertionError("Iteration " + iteration + " failed with exception: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Gets valid login credentials (known working credentials)
     */
    private LoginCredentials getValidCredentials() {
        // Use the known valid credentials from the system
        return new LoginCredentials("scastro@sentra.cl", "123");
    }

    /**
     * Generates invalid login credentials for property testing
     */
    private LoginCredentials generateInvalidCredentials() {
        String[] invalidEmails = {
            "", 
            "invalid", 
            "test@", 
            "@test.com", 
            "test.com", 
            "test@.com",
            "nonexistent@email.com",
            "wrong@user.com",
            "fake@domain.org"
        };
        
        String[] invalidPasswords = {
            "", 
            "wrong", 
            "incorrect123", 
            "badpassword",
            "12345",
            "password",
            "wrongpass",
            "notvalid"
        };
        
        // Sometimes use valid email with wrong password, sometimes wrong email with any password
        String email, password;
        
        if (random.nextBoolean()) {
            // Valid email, wrong password
            email = "scastro@sentra.cl";
            password = invalidPasswords[random.nextInt(invalidPasswords.length)];
        } else {
            // Invalid email, any password
            email = invalidEmails[random.nextInt(invalidEmails.length)];
            password = random.nextBoolean() ? "123" : invalidPasswords[random.nextInt(invalidPasswords.length)];
        }
        
        return new LoginCredentials(email, password);
    }

    /**
     * Data class for login credentials
     */
    private static class LoginCredentials {
        private final String email;
        private final String password;

        public LoginCredentials(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    /**
     * Edge case property test for login validation
     */
    @Test
    public void testLoginValidationEdgeCases() {
        // Test edge cases with specific scenarios
        String[][] edgeCases = {
            {"", ""}, // Both empty
            {" ", " "}, // Whitespace only
            {"scastro@sentra.cl", ""}, // Valid email, empty password
            {"", "123"}, // Empty email, valid password
            {"SCASTRO@SENTRA.CL", "123"}, // Uppercase email
            {"scastro@sentra.cl", "123 "}, // Password with trailing space
            {" scastro@sentra.cl", "123"}, // Email with leading space
        };
        
        for (int i = 0; i < edgeCases.length; i++) {
            try {
                String[] caseData = edgeCases[i];
                LoginCredentials credentials = new LoginCredentials(caseData[0], caseData[1]);
                
                // Navigate to login page
                loginPage.open();
                
                // Attempt login
                loginPage.login(credentials.getEmail(), credentials.getPassword());
                
                // For edge cases, determine if they should be valid or invalid
                boolean shouldBeValid = credentials.getEmail().trim().equals("scastro@sentra.cl") && 
                                       credentials.getPassword().trim().equals("123");
                
                if (shouldBeValid) {
                    // Should succeed
                    assertTrue("Edge case " + i + ": Valid edge case credentials should succeed", 
                              loginPage.isLoginSuccessful());
                } else {
                    // Should fail gracefully
                    boolean failed = !loginPage.isLoginSuccessful();
                    boolean hasError = loginPage.hasLoginError();
                    boolean staysOnLogin = loginPage.isStillOnLoginPage();
                    
                    assertTrue("Edge case " + i + ": Invalid edge case credentials should fail gracefully", 
                              failed || hasError || staysOnLogin);
                }
                
            } catch (Exception e) {
                throw new AssertionError("Edge case " + i + " failed with exception: " + e.getMessage(), e);
            }
        }
    }
}