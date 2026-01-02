package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * Configuración principal para la ejecución de pruebas automatizadas.
 * 
 * ESTRATEGIA DE TESTING:
 * - Los escenarios de negocio se validan mediante Cucumber + Gherkin (BDD)
 * - Las pruebas técnicas adicionales validan reglas del dominio y no flujos UI
 * - Ambos enfoques son complementarios para garantizar cobertura completa
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        tags = "@createTask",
        plugin = {"pretty"}
)
public class TestRunner {
        
}
