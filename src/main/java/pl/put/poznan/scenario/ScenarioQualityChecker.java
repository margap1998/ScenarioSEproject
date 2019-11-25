package pl.put.poznan.scenario;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap; 
import java.util.Map; 
import java.util.ArrayList; 
import org.json.JSONArray; 
import org.json.JSONObject;

interface  ScenarioElement {
    void accept(ScenarioElementVisitor visitor);
}

interface ScenarioElementVisitor {
    void visit(Scenario scenario);
    void visit(Step step);
    void startRecursion();
    void endRecursion();
}

/**
 * This class represents one step in our scenario
 * One step may contain several substeps
 */
class Step implements ScenarioElement {
    public String name;
    public ArrayList<Step> substeps;

    /**
     * This is class constructor which is setting value of variable 'name' and creates a list of possible substeps
     * @param jo JSONObject representing one step
     */
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

    /**
     * This method makes visitor visit step and it's substeps
     * @param visitor Parameter representing visitor
     */
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

    /**
     * This method creates JSONObject and adds step and subsets int it
     * @return JSONObject of step
     */
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

/**
 * Class representing whole scenario which contains many steps
 */
class Scenario implements ScenarioElement {
    public String title;
    public ArrayList<String> actors;
    public String system;
    public ArrayList<Step> steps;

    /**
     * This is class constructor where elements of scenario are assigned to corresponding variables
     * @param jo JSONObject of Scenario
     * @throws Exception
     */
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

    /**
     * In this method we make visitor visit our scenario and each step
     * @param visitor Visitor
     */
    @Override
    public void accept(ScenarioElementVisitor visitor) {
        visitor.visit(this);
        for(int i = 0; i < steps.size(); i++) {
            steps.get(i).accept(visitor);
        }
    }

    /**
     * This method creates JSONobject and adds into it title and names of actors and system and scenario's steps
     * @return JSONObject
     */
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

/**
 * This class counts how many steps are in the scenario
 */
class ScenarioStepCountVisitor implements ScenarioElementVisitor {
    int result;

    /**
     * One step increases the number by one
     * @param step represents one step
     */
    @Override
    public void visit(Step step) {
        result++;
    }

    /**
     * At the beginning, the number equals zero
     * @param scenario represents whole scenario
     */
    @Override
    public void visit(Scenario scenario) {
        result = 0;
    }

    @Override
    public void startRecursion() {}
    @Override
    public void endRecursion() {}

    /**
     * @return number of steps
     */
    public int getResult() {
        return result;
    }
}

/**
 * Transforming scenario into a text file
 */
class ScenarioToTextVisitor implements ScenarioElementVisitor {
    StringBuilder result;
    ArrayList<Integer> stepNr;

    /**
     * For each step, write a number, a dot and the rest of the step
     * @param step one step of a scenario
     */
    @Override
    public void visit(Step step) {
        int lastIndex = stepNr.size()-1;
        stepNr.set(lastIndex, stepNr.get(lastIndex)+1);

        for(int i = 0; i < stepNr.size(); i++) {
            result.append(stepNr.get(i) + ".");
        }
        result.append(" " + step.name + "\n");
    }

    /**
     * At the beginning, create necessary variables
     * @param scenario our scenario
     */
    @Override
    public void visit(Scenario scenario) {
        result = new StringBuilder();
        stepNr = new ArrayList<Integer>();
        stepNr.add(0);
    }

    /**
     * Add zero at beginning of substep
     */
    @Override
    public void startRecursion() {
        stepNr.add(0);
    }

    /**
     * Removing last number at the end of list of substeps
     */
    @Override
    public void endRecursion() {
        stepNr.remove(stepNr.size()-1);
    }

    /**
     * @return scenario converted into a string
     */
    public String getResult() {
        return result.toString();
    }
}

/**
 * This class limits nest level of subsets in a single step
 */
class ScenarioNestLimitVisitor implements ScenarioElementVisitor {
    int nestLimit;
    int nestLevel;

    /**
     * If a nest level is too high, delete substep
     * @param step One step of scenario
     */
    @Override
    public void visit(Step step) {
        if(nestLevel == nestLimit)
            step.substeps.clear();
    }

    /**
     * At the beginning, set the nestlevel at 0
     * @param scenario One step of scenario
     */
    @Override
    public void visit(Scenario scenario) {
        nestLevel = 0;
    }

    /**
     * Increase the nest level by one when a subset appears
     */
    @Override
    public void startRecursion() {
        nestLevel += 1;
    }

    /**
     *  Decrease the nest level by one when the subset closes
     */
    @Override
    public void endRecursion() {
        nestLevel -= 1;
    }

    /**
     * Sets the limitation of possible substeps in a substep
     * @param limit maximum number of substeps
     */
    public ScenarioNestLimitVisitor(int limit) {
        nestLimit = limit-1;
    }
}

/**
 * This class checks the quality of Scenario
 */
public class ScenarioQualityChecker {
    /**
     * Trying to read the file containing the scenario and to parse it in order to check the quality of it
     * @param args
     */
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

        ScenarioToTextVisitor v = new ScenarioToTextVisitor();
        scenario.accept(v);
        System.out.println(v.getResult());

        //System.out.println(scenario.toJSON().toString(4));
    }
}
