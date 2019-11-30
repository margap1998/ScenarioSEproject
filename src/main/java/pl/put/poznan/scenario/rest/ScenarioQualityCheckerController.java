package pl.put.poznan.scenario.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@RestController
@RequestMapping("/api")
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    @RequestMapping(value = "/stepCount", method = RequestMethod.GET, produces = "application/json")
    public ScenarioQualityChecker scenario(@PathVariable String text) {
        logger.debug(text);

        try {
            text = new String(Files.readAllBytes(Paths.get("example_scenario.json")));
        }
        catch(Exception e) {
            System.out.println("can't read example_scenario.json");
            return;
        };

        JSONObject jo = new JSONObject(text);
        Scenario scenario;

        try {
            scenario = new Scenario(jo);
        }
        catch(Exception e) {
            System.out.println("can't parse example_scenario.json");
            System.out.println(e);
            return;
        }

        scenario.accept(new ScenarioNestLimitVisitor(2));

        ScenarioStepCountVisitor stepCountVisitor = new ScenarioStepCountVisitor();
        scenario.accept(stepCountVisitor);
        return stepCountVisitor.getResult();

    }

    @RequestMapping(value = "/incorrectstepCount", method = RequestMethod.GET, produces = "application/json")
    public Scenario scenario(@PathVariable String text) {
        logger.debug(text);

        try {
            text = new String(Files.readAllBytes(Paths.get("example_scenario.json")));
        }
        catch(Exception e) {
            System.out.println("can't read example_scenario.json");
            return;
        };

        JSONObject jo = new JSONObject(text);
        Scenario scenario;

        try {
            scenario = new Scenario(jo);
        }
        catch(Exception e) {
            System.out.println("can't parse example_scenario.json");
            System.out.println(e);
            return;
        }

        scenario.accept(new ScenarioNestLimitVisitor(2));

        ScenarioIncorrectStepCountVisitor inStepCount = new ScenarioIncorrectStepCountVisitor();
        scenario.accept(inStepCount);
        return inStepCount.getResult();

    }

    @RequestMapping(value = "/toText", method = RequestMethod.GET, produces = "application/json")
    public Scenario scenario(@PathVariable String text) {
        logger.debug(text);

        try {
            text = new String(Files.readAllBytes(Paths.get("example_scenario.json")));
        }
        catch(Exception e) {
            System.out.println("can't read example_scenario.json");
            return;
        };

        JSONObject jo = new JSONObject(text);
        Scenario scenario;

        try {
            scenario = new Scenario(jo);
        }
        catch(Exception e) {
            System.out.println("can't parse example_scenario.json");
            System.out.println(e);
            return;
        }

        scenario.accept(new ScenarioNestLimitVisitor(2));

        ScenarioToTextVisitor v = new ScenarioToTextVisitor();
        scenario.accept(v);
        return v.getResult();

    }
}
