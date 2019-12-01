package pl.put.poznan.scenario.logic;

public interface ScenarioElement {
    void accept(ScenarioElementVisitor visitor);
}
