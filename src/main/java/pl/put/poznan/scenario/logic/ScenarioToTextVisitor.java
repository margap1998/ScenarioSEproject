package pl.put.poznan.scenario.logic;

import java.util.ArrayList; 

/**
 * Transforming scenario into a text file
 */
public class ScenarioToTextVisitor implements ScenarioElementVisitor {
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
