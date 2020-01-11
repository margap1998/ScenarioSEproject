package pl.put.poznan.scenario.logic;

import org.junit.jupiter.api.*;

public class ScenerioStepCountTests
{
    static Scenario a;
    @BeforeEach
    public void init()
    {
        a = new Scenario();
        a.title = "tytuł";
        a.actors.add("user");
        a.system ="System";
    }
    @Test
    public void test0steps()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        a.accept(test);
        Assertions.assertEquals(0,test.getResult());
    }
    private void addPrimarySteps(int q)
    {

        for(int i=0; i<q; i++)
        {
            a.steps.add(new Step());
        }
    }
    private void addSecondarySteps(int which,int q)
    {
        for(int i=0; i<q; i++)
        {
            a.steps.get(which).substeps.add(new Step());
        }
    }
    @Test
    public void test1step()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(1);
        a.accept(test);
        Assertions.assertEquals(1,test.getResult());
    }

    @Test
    public void test2steps()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(2);
        a.accept(test);
        Assertions.assertEquals(2,test.getResult());
    }

    @Test
    public void test1stepWith1SubstepOn0()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(1);
        addSecondarySteps(0,1);
        a.accept(test);
        Assertions.assertEquals(2,test.getResult());
    }

    @Test
    public void test1stepWith2SubstepsOn0()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(1);
        addSecondarySteps(0,2);
        a.accept(test);
        Assertions.assertEquals(3,test.getResult());
    }

    @Test
    public void test2stepsWith1SubstepOn0()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(2);
        addSecondarySteps(0,1);
        a.accept(test);
        Assertions.assertEquals(3,test.getResult());
    }

    @Test
    public void test2stepsWith2SubstepsOn0()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(2);
        addSecondarySteps(0,2);
        a.accept(test);
        Assertions.assertEquals(4,test.getResult());
    }

    @Test
    public void test2stepsWith1SubstepOn1()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(2);
        addSecondarySteps(1,1);
        a.accept(test);
        Assertions.assertEquals(3,test.getResult());
    }

    @Test
    public void test2stepsWith2SubstepsOn1()
    {
        ScenarioStepCountVisitor test = new ScenarioStepCountVisitor();
        addPrimarySteps(2);
        addSecondarySteps(1,2);
        a.accept(test);
        Assertions.assertEquals(4,test.getResult());
    }
}
