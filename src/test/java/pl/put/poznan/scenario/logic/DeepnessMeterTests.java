package pl.put.poznan.scenario.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeepnessMeterTests
{
    @Test
    public void test0Step()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        a.accept(test);
        Assertions.assertEquals(0,test.getDeepest());
    }

    @Test
    public void test1Step()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        a.steps.add(new Step());
        a.accept(test);
        Assertions.assertEquals(1,test.getDeepest());
    }

    @Test
    public void test2Steps()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        for (int i =0; i<2; i++)
            a.steps.add(new Step());
        a.accept(test);
        Assertions.assertEquals(1,test.getDeepest());
    }

    @Test
    public void test2StepsWith1substepOn0()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        for (int i =0; i<2; i++)
            a.steps.add(new Step());
        a.steps.get(0).substeps.add(new Step());
        a.accept(test);
        Assertions.assertEquals(2,test.getDeepest());
    }

    @Test
    public void test2StepsWith1substepOn0and1()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        for (int i =0; i<2; i++)
            a.steps.add(new Step());
        a.steps.get(0).substeps.add(new Step());
        a.steps.get(1).substeps.add(new Step());
        a.accept(test);
        Assertions.assertEquals(2,test.getDeepest());
    }
    @Test
    public void test2StepsWith2substepsOn0()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        for (int i =0; i<2; i++)
            a.steps.add(new Step());
        a.steps.get(0).substeps.add(new Step());
        a.accept(test);
        Assertions.assertEquals(2,test.getDeepest());
    }

    @Test
    public void test2StepsWith1substepOn0With4substep()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        for (int i =0; i<2; i++)
            a.steps.add(new Step());
        Step s = new Step();
        for (int i =0; i<4; i++)
            s.substeps.add(new Step());
        a.steps.get(0).substeps.add(s);
        a.accept(test);
        Assertions.assertEquals(3,test.getDeepest());
    }
    @Test
    public void test2StepsWith4substepOn0and1()
    {
        ScenarioDeepnessMeterVisitor test = new ScenarioDeepnessMeterVisitor();
        Scenario a = new Scenario();
        for (int i =0; i<2; i++)
            a.steps.add(new Step());
        for (int i =0; i<4; i++)
            a.steps.get(0).substeps.add(new Step());
        for (int i =0; i<4; i++)
            a.steps.get(1).substeps.add(new Step());
        a.accept(test);
        Assertions.assertEquals(2,test.getDeepest());
    }
}
