package pl.put.poznan.scenario.rest;

import org.json.JSONObject;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario.logic.*;


import java.io.UnsupportedEncodingException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


/**
 * Controller class for RESTful API provided by Spring Boot
 */
@RestController
@RequestMapping("/api")
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    /**
     * Handling request to count steps in scenario from JSON scenario sent in request body.
     * @param scenarioText JSON text of scenario
     * @return number of steps.
     * @throws Exception Thrown if the scenarioText is incorrect
     */
    @CrossOrigin()
    @RequestMapping(value = "/stepCount", method = RequestMethod.POST, produces = "application/json")
    public int stepCount(@RequestParam(value="scenario") String scenarioText) throws Exception {
        logger.debug(scenarioText);
        JSONObject jo;
        jo = new JSONObject(scenarioText);

        Scenario scenario;
        scenario = new Scenario(jo);

        ScenarioStepCountVisitor stepCountVisitor = new ScenarioStepCountVisitor();
        scenario.accept(stepCountVisitor);
        return stepCountVisitor.getResult();
    }


    /**
     * Handling request to list incorrect steps in scenario
     * @param scenarioText JSON text of scenario sent in request body
     * @return List of incorrect steps
     * @throws Exception Thrown if the scenarioText is incorrect
     */
    @CrossOrigin()
    @RequestMapping(value = "/incorrectStepList", method = RequestMethod.POST, produces = "application/json")
    public ArrayList<String> incorrectStepList(@RequestParam(value="scenario") String scenarioText) throws Exception {
        logger.debug(scenarioText);
        JSONObject jo;
        jo = new JSONObject(scenarioText);

        Scenario scenario;
        scenario = new Scenario(jo);

        ScenarioIncorrectStepListVisitor inStepList = new ScenarioIncorrectStepListVisitor();
        scenario.accept(inStepList);

        ArrayList<String> incorrectSteps = inStepList.getIncorrectSteps();
        logger.debug(incorrectSteps.toString());
        return incorrectSteps;
    }

    /**
     * Handling request to convert scenerio to numbered list in plain text.
     * @param scenarioText JSON text of scenario
     * @return Transformed scenario to plain text
     * @throws Exception Thrown if the scenarioText is incorrect
     */
    @CrossOrigin()
    @RequestMapping(value = "/toText", method = RequestMethod.POST, produces = "application/json")
    public String toText(@RequestParam(value="scenario") String scenarioText) throws Exception {
        logger.debug(scenarioText);
        JSONObject jo;
        jo = new JSONObject(scenarioText);

        Scenario scenario;
        scenario = new Scenario(jo);

        ScenarioToTextVisitor visitor = new ScenarioToTextVisitor();
        scenario.accept(visitor);
        return visitor.getResult();
    }

    /**
     * Handling request to limit nesting level in scenario from JSON scenario sent in request body.
     * @param scenarioText JSON text of scenario
     * @param level maximal nesting level allowed
     * @return JSON text of scenario with steps exceeding the maximal nesting level removed
     * @throws Exception Thrown if the scenarioText or level is incorrect
     */
    @CrossOrigin()
    @RequestMapping(value = "/nestLimit", method = RequestMethod.POST, produces = "application/json")
    public String nestLimit(@RequestParam(value="scenario") String scenarioText, @RequestParam(value="level", defaultValue="1") int level) throws Exception {
        logger.debug(scenarioText);
        JSONObject jo;
        jo = new JSONObject(scenarioText);

        Scenario scenario;
        scenario = new Scenario(jo);

        ScenarioNestLimitVisitor visitor = new ScenarioNestLimitVisitor(level);
        scenario.accept(visitor);
        return scenario.toJSON().toString();
    }

    /**
     * Handling request to count steps with a keyword in a scenario.
     * @param scenarioText JSON text of scenario
     * @return number of steps starting with a keyword
     * @throws Exception Thrown if the scenarioText is incorrect
     */
    @CrossOrigin()
    @RequestMapping(value = "/keywordCount", method = RequestMethod.POST, produces = "application/json")
    public int keywordCount(@RequestParam(value="scenario") String scenarioText) throws Exception {
        logger.debug(scenarioText);
        JSONObject jo;
        jo = new JSONObject(scenarioText);

        Scenario scenario;
        scenario = new Scenario(jo);

        ScenarioKeywordCountVisitor visitor = new ScenarioKeywordCountVisitor();
        scenario.accept(visitor);
        return visitor.getResult();
    }
}
