package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/*@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = "steps"
)
public class TestRunner {
}*/

/*@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "steps",
    tags = "@modificar",
    plugin = {"pretty"}
)*/

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "steps",
        tags = "@password",
        plugin = {"pretty"}
)


public class TestRunner {
        
}
