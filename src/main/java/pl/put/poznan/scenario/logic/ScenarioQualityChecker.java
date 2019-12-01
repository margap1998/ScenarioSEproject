package pl.put.poznan.scenario.logic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap; 
import java.util.Map; 
import java.util.ArrayList; 
import org.json.JSONArray; 
import org.json.JSONObject; 

public class ScenarioQualityChecker {
    public static void main(String args[]) {
        String text;
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
        System.out.println(stepCountVisitor.getResult());

        ScenarioIncorrectStepCountVisitor inStepCount = new ScenarioIncorrectStepCountVisitor();
        scenario.accept(inStepCount);
        System.out.println(inStepCount.getResult());

        ScenarioToTextVisitor v = new ScenarioToTextVisitor();
        scenario.accept(v);
        System.out.println(v.getResult());

        //System.out.println(scenario.toJSON().toString(4));
    }
}
