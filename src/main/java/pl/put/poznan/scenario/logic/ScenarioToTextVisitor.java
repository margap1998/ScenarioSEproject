package pl.put.poznan.scenario.logic;

import java.util.ArrayList; 

public class ScenarioToTextVisitor implements ScenarioElementVisitor {
    StringBuilder result;
    ArrayList<Integer> stepNr;

    @Override
    public void visit(Step step) {
        int lastIndex = stepNr.size()-1;
        stepNr.set(lastIndex, stepNr.get(lastIndex)+1);

        for(int i = 0; i < stepNr.size(); i++) {
            result.append(stepNr.get(i) + ".");
        }
        result.append(" " + step.name + "\n");
    }

    @Override
    public void visit(Scenario scenario) {
        result = new StringBuilder();
        stepNr = new ArrayList<Integer>();
        stepNr.add(0);
    }

    @Override
    public void startRecursion() {
        stepNr.add(0);
    }
    @Override
    public void endRecursion() {
        stepNr.remove(stepNr.size()-1);
    }

    public String getResult() {
        return result.toString();
    }
}
