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
     */
    @CrossOrigin()
    @RequestMapping(value = "/stepCount", method = RequestMethod.POST, produces = "application/json")
    public int stepCount(@RequestBody String scenarioText) {
        logger.debug(scenarioText);
        JSONObject jo;
        try {
            jo = getJsonObjectFromText(scenarioText);
        }
        catch(Exception e) {
            System.out.println("can't parse request body into json");
            System.out.println(e);
            return -1;
        }

        Scenario scenario;
        try {
            scenario = new Scenario(jo);
        }
        catch(Exception e) {
            System.out.println("can't parse scenario");
            System.out.println(e);
            return -1;
        }

        ScenarioStepCountVisitor stepCountVisitor = new ScenarioStepCountVisitor();
        scenario.accept(stepCountVisitor);
        return stepCountVisitor.getResult();
    }

    /**
     * Handling request to list incorrect steps in scenario
     * @param scenarioText JSON text of scenario sent in request body
     * @return List of incorrect steps
     */
    @CrossOrigin()
    @RequestMapping(value = "/incorrectStepList", method = RequestMethod.POST, produces = "application/json")
    public ArrayList<String> incorrectStepList(@RequestBody String scenarioText) {
        // TODO(piotr): throw an exception instead of returning invalid results in all requests
        ArrayList<String> InvalidResult = new ArrayList<String>();
        logger.debug(scenarioText);
        JSONObject jo;
        try {
            jo = getJsonObjectFromText(scenarioText);
        }
        catch(Exception e) {
            System.out.println("can't parse request body into json");
            System.out.println(e);
            return InvalidResult;
        }

        Scenario scenario;
        try {
            scenario = new Scenario(jo);
        }
        catch(Exception e) {
            System.out.println("can't parse scenario");
            System.out.println(e);
            return InvalidResult;
        }

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
     */
    @CrossOrigin()
    @RequestMapping(value = "/toText", method = RequestMethod.POST, produces = "application/json")
    public String toText(@RequestBody String scenarioText) {
        logger.debug(scenarioText);
        JSONObject jo;
        try {
            jo = getJsonObjectFromText(scenarioText);
        }
        catch(Exception e) {
            System.out.println("can't parse request body into json");
            System.out.println(e);
            return "";
        }

        Scenario scenario;
        try {
            scenario = new Scenario(jo);
        }
        catch(Exception e) {
            System.out.println("can't parse scenario");
            System.out.println(e);
            return "";
        }

        ScenarioToTextVisitor v = new ScenarioToTextVisitor();
        scenario.accept(v);
        return v.getResult();

    }
    /**
     * Method used to convert text sent in request from text to JSONObject.
     * @param scenarioText scenario to convert
     * @return JSON Object to process on
     * @throws UnsupportedEncodingException Thrown if there is unsupported type of scenario.
     */
    private JSONObject getJsonObjectFromText (@RequestBody String scenarioText) throws UnsupportedEncodingException
    {
        JSONObject jo;
		// NOTE(piotr): Z jakiegoś powodu na końcu zawsze jest niepotrzebny znak zapytania, więc go usuwamy... Warto by zrozumieć dlaczego tam jest.
        scenarioText = URLDecoder.decode(scenarioText, StandardCharsets.UTF_8.toString());
        scenarioText = scenarioText.substring(0, scenarioText.length() - 1);
        logger.debug(scenarioText);
        jo = new JSONObject(scenarioText);
        return jo;
    }
}
