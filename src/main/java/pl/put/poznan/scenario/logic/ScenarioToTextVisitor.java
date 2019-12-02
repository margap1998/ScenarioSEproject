package pl.put.poznan.scenario.logic;

import java.util.ArrayList; 

/**
 * Class transforming scenario into plain text
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
     * At the beginning, init necessary variables
     * @param scenario our scenario
     */
    @Override
    public void visit(Scenario scenario) {
        result = new StringBuilder();
        stepNr = new ArrayList<Integer>();
        stepNr.add(0);
    }

    /**
     * Indent the numeration scheme when entering a substeps list
     */
    @Override
    public void startRecursion() {
        stepNr.add(0);
    }

    /**
     * Removing last number at the end of a substeps list
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
