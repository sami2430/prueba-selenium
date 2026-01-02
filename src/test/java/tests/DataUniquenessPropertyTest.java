package tests;

import org.junit.Test;
import utils.TestDataGenerator;
import utils.TestDataGenerator.UserTestData;
import utils.TestDataGenerator.TaskTestData;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Pruebas basadas en propiedades para unicidad de TestDataGenerator
 * Característica: selenium-test-improvements, Propiedad 10: Unicidad de Datos de Prueba
 * **Valida: Requisitos 7.1**
 */
public class DataUniquenessPropertyTest {

    private static final int TEST_ITERATIONS = 100;

    /**
     * Propiedad 10: Unicidad de Email
     * Para cualquier conjunto de emails generados, todos los emails deben ser únicos
     * **Valida: Requisitos 7.1**
     */
    @Test
    public void testEmailUniquenessProperty() {
        Set<String> generatedEmails = new HashSet<>();
        
        // Generar múltiples emails y verificar unicidad
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            String email = TestDataGenerator.generateUniqueEmail();
            
            // Verificar que el email no es null o vacío
            assertNotNull("El email generado no debe ser null", email);
            assertFalse("El email generado no debe estar vacío", email.trim().isEmpty());
            
            // Verificar que el formato del email es válido
            assertTrue("El email generado debe contener el símbolo @", email.contains("@"));
            assertTrue("El email generado debe contener dominio", email.contains("."));
            
            // Verificar unicidad
            assertFalse("El email generado debe ser único: " + email, 
                       generatedEmails.contains(email));
            
            generatedEmails.add(email);
        }
        
        // Verificar que generamos el número esperado de emails únicos
        assertEquals("Debe generar exactamente " + TEST_ITERATIONS + " emails únicos", 
                    TEST_ITERATIONS, generatedEmails.size());
    }

    /**
     * Property 10: Username Uniqueness
     * For any set of generated usernames, all usernames should be unique
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testUsernameUniquenessProperty() {
        Set<String> generatedUsernames = new HashSet<>();
        
        // Generate multiple usernames and verify uniqueness
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            String username = TestDataGenerator.generateUniqueUsername();
            
            // Verify username is not null or empty
            assertNotNull("Generated username should not be null", username);
            assertFalse("Generated username should not be empty", username.trim().isEmpty());
            
            // Verify uniqueness
            assertFalse("Generated username should be unique: " + username, 
                       generatedUsernames.contains(username));
            
            generatedUsernames.add(username);
        }
        
        // Verify we generated the expected number of unique usernames
        assertEquals("Should generate exactly " + TEST_ITERATIONS + " unique usernames", 
                    TEST_ITERATIONS, generatedUsernames.size());
    }

    /**
     * Property 10: Task Title Uniqueness
     * For any set of generated task titles, all titles should be unique
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testTaskTitleUniquenessProperty() {
        Set<String> generatedTitles = new HashSet<>();
        
        // Generate multiple task titles and verify uniqueness
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            String title = TestDataGenerator.generateUniqueTaskTitle();
            
            // Verify title is not null or empty
            assertNotNull("Generated task title should not be null", title);
            assertFalse("Generated task title should not be empty", title.trim().isEmpty());
            
            // Verify uniqueness
            assertFalse("Generated task title should be unique: " + title, 
                       generatedTitles.contains(title));
            
            generatedTitles.add(title);
        }
        
        // Verify we generated the expected number of unique titles
        assertEquals("Should generate exactly " + TEST_ITERATIONS + " unique task titles", 
                    TEST_ITERATIONS, generatedTitles.size());
    }

    /**
     * Property 10: User Test Data Uniqueness
     * For any set of generated user test data, all email addresses should be unique
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testUserTestDataUniquenessProperty() {
        Set<String> generatedEmails = new HashSet<>();
        
        // Generate multiple user test data objects and verify email uniqueness
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            UserTestData userData = TestDataGenerator.generateUserTestData();
            
            // Verify all fields are populated
            assertNotNull("First name should not be null", userData.getFirstName());
            assertNotNull("Last name should not be null", userData.getLastName());
            assertNotNull("Email should not be null", userData.getEmail());
            assertNotNull("Password should not be null", userData.getPassword());
            
            assertFalse("First name should not be empty", userData.getFirstName().trim().isEmpty());
            assertFalse("Last name should not be empty", userData.getLastName().trim().isEmpty());
            assertFalse("Email should not be empty", userData.getEmail().trim().isEmpty());
            assertFalse("Password should not be empty", userData.getPassword().trim().isEmpty());
            
            // Verify email uniqueness (key identifier for users)
            assertFalse("Generated user email should be unique: " + userData.getEmail(), 
                       generatedEmails.contains(userData.getEmail()));
            
            generatedEmails.add(userData.getEmail());
        }
        
        // Verify we generated the expected number of unique user emails
        assertEquals("Should generate exactly " + TEST_ITERATIONS + " unique user emails", 
                    TEST_ITERATIONS, generatedEmails.size());
    }

    /**
     * Property 10: Task Test Data Uniqueness
     * For any set of generated task test data, all titles should be unique
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testTaskTestDataUniquenessProperty() {
        Set<String> generatedTitles = new HashSet<>();
        
        // Generate multiple task test data objects and verify title uniqueness
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            TaskTestData taskData = TestDataGenerator.generateTaskTestData();
            
            // Verify all fields are populated
            assertNotNull("Title should not be null", taskData.getTitle());
            assertNotNull("Description should not be null", taskData.getDescription());
            assertTrue("Priority should be positive", taskData.getPriority() > 0);
            
            assertFalse("Title should not be empty", taskData.getTitle().trim().isEmpty());
            assertFalse("Description should not be empty", taskData.getDescription().trim().isEmpty());
            
            // Verify title uniqueness (key identifier for tasks)
            assertFalse("Generated task title should be unique: " + taskData.getTitle(), 
                       generatedTitles.contains(taskData.getTitle()));
            
            generatedTitles.add(taskData.getTitle());
        }
        
        // Verify we generated the expected number of unique task titles
        assertEquals("Should generate exactly " + TEST_ITERATIONS + " unique task titles", 
                    TEST_ITERATIONS, generatedTitles.size());
    }

    /**
     * Property 10: Password Security
     * For any generated password, it should meet security requirements
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testPasswordSecurityProperty() {
        // Test multiple password generations
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            String password = TestDataGenerator.generatePassword();
            
            // Verify password is not null or empty
            assertNotNull("Generated password should not be null", password);
            assertFalse("Generated password should not be empty", password.trim().isEmpty());
            
            // Verify minimum length
            assertTrue("Password should be at least 8 characters long", password.length() >= 8);
            
            // Verify contains uppercase
            assertTrue("Password should contain at least one uppercase letter", 
                      password.matches(".*[A-Z].*"));
            
            // Verify contains lowercase
            assertTrue("Password should contain at least one lowercase letter", 
                      password.matches(".*[a-z].*"));
            
            // Verify contains digit
            assertTrue("Password should contain at least one digit", 
                      password.matches(".*[0-9].*"));
            
            // Verify contains special character
            assertTrue("Password should contain at least one special character", 
                      password.matches(".*[!@#$%^&*].*"));
        }
    }

    /**
     * Property 10: Invalid Email Generation Consistency
     * For any generated invalid email, it should consistently be invalid
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testInvalidEmailGenerationProperty() {
        // Test multiple invalid email generations
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            String invalidEmail = TestDataGenerator.generateInvalidEmail();
            
            // Verify email is not null
            assertNotNull("Generated invalid email should not be null", invalidEmail);
            
            // Verify email is actually invalid (should fail basic email validation)
            boolean isValid = invalidEmail.contains("@") && 
                             invalidEmail.contains(".") && 
                             !invalidEmail.trim().isEmpty() &&
                             !invalidEmail.startsWith("@") &&
                             !invalidEmail.endsWith("@") &&
                             !invalidEmail.contains(" ");
            
            assertFalse("Generated email should be invalid: " + invalidEmail, isValid);
        }
    }

    /**
     * Property 10: Task Priority Range
     * For any generated task priority, it should be within valid range
     * **Validates: Requirements 7.1**
     */
    @Test
    public void testTaskPriorityRangeProperty() {
        // Test multiple priority generations
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            int priority = TestDataGenerator.generateTaskPriority();
            
            // Verify priority is within valid range (1-5)
            assertTrue("Priority should be at least 1", priority >= 1);
            assertTrue("Priority should be at most 5", priority <= 5);
        }
        
        // Test custom range
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            int priority = TestDataGenerator.generateTaskPriority(2, 4);
            
            // Verify priority is within custom range (2-4)
            assertTrue("Priority should be at least 2", priority >= 2);
            assertTrue("Priority should be at most 4", priority <= 4);
        }
    }
}