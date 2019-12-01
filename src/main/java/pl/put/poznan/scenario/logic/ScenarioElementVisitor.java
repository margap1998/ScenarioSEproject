package pl.put.poznan.scenario.logic;

public interface ScenarioElementVisitor {
    void visit(Scenario scenario);
    void visit(Step step);
    void startRecursion();
    void endRecursion();
}
