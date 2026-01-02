package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utilidad para generación de datos de prueba únicos.
 * Evita colisiones entre ejecuciones de pruebas automatizadas.
 * 
 * Esta clase solo genera datos y no contiene validaciones ni lógica de test.
 * Proporciona métodos para crear información realista y única que puede
 * ser utilizada en pruebas automatizadas sin interferir entre ejecuciones.
 */
public class TestDataGenerator {
    
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Random random = new Random();
    
    // Arrays para generar nombres realistas
    private static final String[] FIRST_NAMES = {
        "Ana", "Carlos", "María", "José", "Carmen", "Antonio", "Isabel", "Manuel",
        "Pilar", "Francisco", "Dolores", "David", "Rosario", "Daniel", "Antonia"
    };
    
    private static final String[] LAST_NAMES = {
        "García", "Rodríguez", "González", "Fernández", "López", "Martínez", "Sánchez",
        "Pérez", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández", "Díaz", "Moreno"
    };
    
    private static final String[] TASK_PREFIXES = {
        "Implementar", "Desarrollar", "Crear", "Diseñar", "Configurar", "Optimizar",
        "Revisar", "Actualizar", "Corregir", "Mejorar", "Validar", "Documentar"
    };
    
    private static final String[] TASK_SUBJECTS = {
        "funcionalidad", "módulo", "componente", "interfaz", "sistema", "proceso",
        "algoritmo", "base de datos", "API", "servicio", "aplicación", "feature"
    };

    /**
     * Genera una dirección de email única
     * Requerimientos: 7.1
     */
    public static String generateUniqueEmail() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int uniqueId = counter.incrementAndGet();
        return String.format("test_%s_%d@automation.com", timestamp, uniqueId);
    }

    /**
     * Genera un email único con dominio personalizado
     * Requerimientos: 7.1
     */
    public static String generateUniqueEmail(String domain) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int uniqueId = counter.incrementAndGet();
        return String.format("test_%s_%d@%s", timestamp, uniqueId, domain);
    }

    /**
     * Genera un nombre aleatorio
     * Requerimientos: 7.1
     */
    public static String generateFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }

    /**
     * Genera un apellido aleatorio
     * Requerimientos: 7.1
     */
    public static String generateLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    /**
     * Genera un nombre de usuario único
     * Requerimientos: 7.1
     */
    public static String generateUniqueUsername() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int uniqueId = counter.incrementAndGet();
        return String.format("user_%s_%d", timestamp, uniqueId);
    }

    /**
     * Genera una contraseña segura con longitud específica
     * Requerimientos: 7.1
     */
    public static String generatePassword(int length) {
        if (length < 8) {
            length = 8; // Longitud mínima segura
        }
        
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*";
        
        StringBuilder password = new StringBuilder();
        
        // Asegurar al menos un carácter de cada categoría
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // Llenar el resto aleatoriamente
        String allChars = upperCase + lowerCase + numbers + specialChars;
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Mezclar la contraseña
        return shuffleString(password.toString());
    }

    /**
     * Genera una contraseña segura por defecto
     * Requerimientos: 7.1
     */
    public static String generatePassword() {
        return generatePassword(12);
    }

    /**
     * Genera un título de tarea único
     * Requerimientos: 7.1
     */
    public static String generateUniqueTaskTitle() {
        String prefix = TASK_PREFIXES[random.nextInt(TASK_PREFIXES.length)];
        String subject = TASK_SUBJECTS[random.nextInt(TASK_SUBJECTS.length)];
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int uniqueId = counter.incrementAndGet();
        
        return String.format("%s %s %s_%d", prefix, subject, timestamp, uniqueId);
    }

    /**
     * Genera una descripción de tarea única
     * Requerimientos: 7.1
     */
    public static String generateTaskDescription() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int uniqueId = counter.incrementAndGet();
        
        return String.format("Descripción automática generada el %s con ID único %d para pruebas de automatización", 
                           timestamp, uniqueId);
    }

    /**
     * Genera una prioridad aleatoria entre 1 y 5
     * Requerimientos: 7.1
     */
    public static int generateTaskPriority() {
        return random.nextInt(5) + 1; // 1 a 5
    }

    /**
     * Genera una prioridad aleatoria dentro del rango especificado
     * Requerimientos: 7.1
     */
    public static int generateTaskPriority(int min, int max) {
        if (min >= max) {
            return min;
        }
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Genera datos únicos de prueba para registro de usuario
     * Requerimientos: 7.1
     */
    public static UserTestData generateUserTestData() {
        return new UserTestData(
            generateFirstName(),
            generateLastName(),
            generateUniqueEmail(),
            generatePassword()
        );
    }

    /**
     * Genera datos únicos de prueba para creación de tareas
     * Requerimientos: 7.1
     */
    public static TaskTestData generateTaskTestData() {
        return new TaskTestData(
            generateUniqueTaskTitle(),
            generateTaskDescription(),
            generateTaskPriority()
        );
    }

    /**
     * Genera email inválido para pruebas negativas
     * Requerimientos: 7.1
     */
    public static String generateInvalidEmail() {
        String[] invalidEmails = {
            "", // Email vacío
            "invalid-email", // Sin símbolo @
            "@domain.com", // Sin parte local
            "user@", // Sin dominio
            "user@domain", // Sin TLD
            "user name@domain.com", // Espacio en parte local
            "user@domain .com" // Espacio en dominio
        };
        return invalidEmails[random.nextInt(invalidEmails.length)];
    }

    /**
     * Genera un email existente para pruebas de duplicados
     * Requerimientos: 7.1
     */
    public static String generateExistingEmail() {
        // Retornar un email existente conocido para pruebas de duplicados
        return "scastro@sentra.cl";
    }

    /**
     * Método auxiliar para mezclar un string
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    /**
     * Clase de datos para datos de prueba de usuario
     */
    public static class UserTestData {
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String password;

        public UserTestData(String firstName, String lastName, String email, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    /**
     * Clase de datos para datos de prueba de tareas
     */
    public static class TaskTestData {
        private final String title;
        private final String description;
        private final int priority;

        public TaskTestData(String title, String description, int priority) {
            this.title = title;
            this.description = description;
            this.priority = priority;
        }

        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getPriority() { return priority; }
    }
}