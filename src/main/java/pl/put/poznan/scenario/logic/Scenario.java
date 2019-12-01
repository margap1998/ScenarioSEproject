package pl.put.poznan.scenario.logic;

import java.util.ArrayList; 
import org.json.JSONArray; 
import org.json.JSONObject; 


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
    public static String[] Keywords = {"FOR EACH:","IF:","ELSE:"};
    /**
     * This is class constructor where elements of scenario are assigned to corresponding variables
     * @param jo JSONObject of Scenario
     * @throws Exception Thrown in case of problem with parsing
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

