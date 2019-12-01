package pl.put.poznan.scenario.logic;

public class ScenarioNestLimitVisitor implements ScenarioElementVisitor {
    int nestLimit;
    int nestLevel;

    @Override
    public void visit(Step step) {
        if(nestLevel == nestLimit)
            step.substeps.clear();
    }

    @Override
    public void visit(Scenario scenario) {
        nestLevel = 0;
    }

    @Override
    public void startRecursion() {
        nestLevel += 1;
    }
    @Override
    public void endRecursion() {
        nestLevel -= 1;
    }

    public ScenarioNestLimitVisitor(int limit) {
        nestLimit = limit-1;
    }
}
