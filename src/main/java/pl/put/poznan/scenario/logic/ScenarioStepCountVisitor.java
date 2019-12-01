package pl.put.poznan.scenario.logic;
/**
 * This class counts how many steps are in the scenario
 */
public class ScenarioStepCountVisitor implements ScenarioElementVisitor {
    /**
     * Counter of steps.
     */
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
