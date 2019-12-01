package pl.put.poznan.scenario.logic;


/**
 * This class limits nest level of subsets in a single step
 */
public class ScenarioNestLimitVisitor implements ScenarioElementVisitor {
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
