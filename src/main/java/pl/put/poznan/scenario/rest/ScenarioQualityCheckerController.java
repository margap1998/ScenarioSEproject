package pl.put.poznan.scenario.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario.ScenarioQualityChecker;

import java.util.Arrays;

@RestController
@RequestMapping("/{text}")
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    @RequestMapping(method = RequestMethod.GET, produces = "scenario/json")
    public String get(@PathVariable String text,
                      @RequestParam(value="scenario") String[] scenario) {

        logger.debug(text);
        logger.debug(Arrays.toString("TODO"));

        ScenarioQualityChecker scenarioQchecker = new ScenarioQualityChecker(scenario);
        return scenarioQchecker;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "scenario/json")
    public String post(@PathVariable String text,
                       @RequestBody String[] scenario) {

        logger.debug(text);
        logger.debug(Arrays.toString("TODO"));

        ScenarioQualityChecker scenarioQchecker = new ScenarioQualityChecker(scenario);
        return scenarioQchecker;
    }
}
