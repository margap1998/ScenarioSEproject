package put.io.sqc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap; 
import java.util.Map; 
import java.util.ArrayList; 
import org.json.JSONArray; 
import org.json.JSONObject; 

interface ScenarioElement {
    void accept(ScenarioElementVisitor visitor);
}

interface ScenarioElementVisitor {
    void visit(Scenario scenario);
    void visit(Step step);
    void startRecursion();
    void endRecursion();
}

class Step implements ScenarioElement {
    public String name;
    public ArrayList<Step> substeps;

    public Step(JSONObject jo) {
        name = jo.getString("name");
        substeps = new ArrayList<Step>();
        if(jo.has("substeps")) {
            JSONArray substepsArray = jo.getJSONArray("substeps");
            for(int i = 0; i < substepsArray.length(); i++) {
                JSONObject stepJson = substepsArray.getJSONObject(i);
                substeps.add(new Step(stepJson));
            }
        }
    }

    @Override
    public void accept(ScenarioElementVisitor visitor) {
        visitor.visit(this);
        if(substeps.size() > 0) {
            visitor.startRecursion();
            for(int i = 0; i < substeps.size(); i++) {
                substeps.get(i).accept(visitor);
            }
            visitor.endRecursion();
        }
    }

    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("name", name);
        if(substeps.size() > 0) {
            JSONArray substepsArray = new JSONArray();
            for(int i = 0; i < substeps.size(); i++) {
                JSONObject substep = substeps.get(i).toJSON();
                substepsArray.put(substep);
            }
            jo.put("substeps", substepsArray);
        }
        return jo;
    }
}

class Scenario implements ScenarioElement {
    public String title;
    public ArrayList<String> actors;
    public String system;
    public ArrayList<Step> steps;

    public Scenario(JSONObject jo) throws Exception {
        title = jo.getString("title");

        JSONArray actorsArray = jo.getJSONArray("actors");
        actors = new ArrayList<String>();
        for(int i = 0; i < actorsArray.length(); i++) {
            actors.add(actorsArray.getString(i));
        }

        system = jo.getString("system");

        steps = new ArrayList<Step>();
        JSONArray stepsArray = jo.getJSONArray("steps");
        for(int i = 0; i < stepsArray.length(); i++) {
            JSONObject stepJson = stepsArray.getJSONObject(i);
            steps.add(new Step(stepJson));
        }
    }

    @Override
    public void accept(ScenarioElementVisitor visitor) {
        visitor.visit(this);
        for(int i = 0; i < steps.size(); i++) {
            steps.get(i).accept(visitor);
        }
    }

    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("title", title);
        jo.put("actors", actors);
        jo.put("system", system);

        JSONArray steps_array = new JSONArray();
        for(int i = 0; i < steps.size(); i++) {
            JSONObject step = steps.get(i).toJSON();
            steps_array.put(step);
        }
        jo.put("steps", steps_array);

        return jo;
    }
}

class ScenarioStepCountVisitor implements ScenarioElementVisitor {
    int result;

    @Override
    public void visit(Step step) {
        result++;
    }

    @Override
    public void visit(Scenario scenario) {
        result = 0;
    }

    @Override
    public void startRecursion() {}
    @Override
    public void endRecursion() {}

    public int getResult() {
        return result;
    }
}

public class ScenarioQualityChecker {
    public static void main(String args[]) {
        String text;
        try {
            text = Files.readString(Paths.get("example_scenario.json"));
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
            return;
        }

        ScenarioStepCountVisitor stepCountVisitor = new ScenarioStepCountVisitor();
        scenario.accept(stepCountVisitor);
        System.out.println(stepCountVisitor.getResult());
        System.out.println(scenario.toJSON().toString(4));
    }
}
