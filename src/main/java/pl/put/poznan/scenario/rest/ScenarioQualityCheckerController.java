package pl.put.poznan.scenario.rest;

import org.json.JSONObject;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.scenario.logic.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/api")
public class ScenarioQualityCheckerController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioQualityCheckerController.class);

    @CrossOrigin()
    @RequestMapping(value = "/stepCount", method = RequestMethod.POST, produces = "application/json")
    public int stepCount(@RequestBody String scenarioText) {

        JSONObject jo;
        try {
            scenarioText = URLDecoder.decode(scenarioText, StandardCharsets.UTF_8.toString());
            // NOTE(piotr): Z jakiegoś powodu na końcu zawsze jest niepotrzebny znak zapytania, więc go usuwamy... Warto by zrozumieć dlaczego tam jest.
            scenarioText = scenarioText.substring(0, scenarioText.length() - 1);
            jo = new JSONObject(scenarioText);
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

    @CrossOrigin()
    @RequestMapping(value = "/incorrectStepList", method = RequestMethod.POST, produces = "application/json")
    public ArrayList<String> incorrectStepList(@RequestBody String scenarioText) {

        JSONObject jo;
        try {
            scenarioText = URLDecoder.decode(scenarioText, StandardCharsets.UTF_8.toString());
            scenarioText = scenarioText.substring(0, scenarioText.length() - 1);
            jo = new JSONObject(scenarioText);
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

        ScenarioIncorrectStepListVisitor inStepList = new ScenarioIncorrectStepListVisitor();
        scenario.accept(inStepList);
        return inStepList.getIncorrectSteps();

    }

    @CrossOrigin()
    @RequestMapping(value = "/toText", method = RequestMethod.POST, produces = "application/json")
    public String toText(@RequestBody String scenarioText) {

        JSONObject jo;
        try {
            scenarioText = URLDecoder.decode(scenarioText, StandardCharsets.UTF_8.toString());
            scenarioText = scenarioText.substring(0, scenarioText.length() - 1);
            jo = new JSONObject(scenarioText);
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
}
