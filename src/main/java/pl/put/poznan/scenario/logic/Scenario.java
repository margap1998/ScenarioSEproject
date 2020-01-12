package pl.put.poznan.scenario.logic;

import java.util.ArrayList; 
import org.json.JSONArray; 
import org.json.JSONObject; 



/**
 * Class representing one step in a scenario
 * One step may contain several substeps
 */
class Step implements ScenarioElement {
    public String name;
    public ArrayList<Step> substeps;

    /**
     * Constructor creating an empty Step object. Name is a empty String, and substeps are empty list.
     */
    public Step() {name = ""; substeps = new ArrayList<Step>();}

    /**
     * Constructor creating 1 step with name, without substeps
     * @param n Name of the step
     */
    public Step(String n) {name = n; substeps = new ArrayList<Step>();}
    /**
     * Constructor creating a Step object from it's JSON representation
     * @param jo JSON representation of a step
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
     * Method making a visitor visit the step and it's substeps
     * @param visitor The visitor object being accepted
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
     * Method creating a JSON representation of the step
     * @return JSON representation of the step
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
 * Class representing a whole scenario which may contain many steps
 */
public class Scenario implements ScenarioElement {
    public String title;
    public ArrayList<String> actors;
    /**
     * System actor
     */
    public String system;
    public ArrayList<Step> steps;
    /**
     * Keywors beeing used in scenarios
     */
    public static String[] Keywords = {"FOR EACH","IF:","ELSE:"};

    /**
     * Constructor creating empty Scenario object.
     * Title and system are empty strings.
     * Actors and steps are empty lists
     */
    public Scenario()
    {
        title ="";
        system ="";
        actors=new ArrayList<String>();
        steps=new ArrayList<Step>();
    }
    /**
     * Constructor, creating a Scenario object from it's JSON representation
     * @param jo JSON representation of a scenario
     * @throws Exception Thrown in case of a problem with parsing
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
     * Method making visitor visit the scenario and each step
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
     * Method creating a JSON representation of the scenario
     * @return JSON representation of the scenario
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

