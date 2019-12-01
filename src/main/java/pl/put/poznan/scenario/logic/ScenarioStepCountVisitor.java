package pl.put.poznan.scenario.logic;

public class ScenarioStepCountVisitor implements ScenarioElementVisitor {
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
