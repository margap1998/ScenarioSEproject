package pl.put.poznan.scenario.logic;


/**
 * Class that removes steps exceeding a given maximal nesting level from a scenario
 */
public class ScenarioNestLimitVisitor implements ScenarioElementVisitor {
    int nestLimit;
    int nestLevel;


    /**
     * If the nesting level is too high, delete substep
     * @param step One step of scenario
     */
    @Override
    public void visit(Step step) {
        if(nestLevel == nestLimit)
            step.substeps.clear();
    }

    /**
     * At the beginning, set the nestLevel to 0
     * @param scenario One step of a scenario
     */
    @Override
    public void visit(Scenario scenario) {
        nestLevel = 0;
    }

    /**
     * Increase the nest level by one when entering a substep list
     */
    @Override
    public void startRecursion() {
        nestLevel += 1;
    }

    /**
     *  Decrease the nest level by one when leaving a substep list
     */
    @Override
    public void endRecursion() {
        nestLevel -= 1;
    }

    /**
     * Sets the maximal nesting level allowed
     * @param limit maximum nesting level
     */
    public ScenarioNestLimitVisitor(int limit) {
        nestLimit = limit-1;
    }
}
