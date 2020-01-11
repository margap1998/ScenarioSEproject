package pl.put.poznan.scenario.logic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;


public class KeywordCountTests
{
    int ri;
    static Scenario a;
    @BeforeEach
    public void init()
    {
        ri = new Random().nextInt(3);
        a = new Scenario();
        a.title = "tytuł";
        a.actors.add("user");
        a.system ="System";
    }
    private void addPrimarySteps(int q, String text)
    {
        for(int i=0; i<q; i++)
        {
            Step tmp = new Step();
            tmp.name = text;
            a.steps.add(tmp);
        }
    }
    private void addSecondarySteps(int which,int q, String text)
    {
        for(int i=0; i<q; i++)
        {
            Step tmp = new Step();
            tmp.name = text;
            a.steps.get(which).substeps.add(tmp);
        }
    }
    private void addPrimarySteps(int q)
    {
        addPrimarySteps(q,"");
    }
    private void addSecondarySteps(int which, int q)
    {
        addSecondarySteps(which, q,"");
    }
    @Test
    public void test0keyword1step()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(1);
        a.accept(test);
        Assertions.assertEquals(0, test.getResult());
    }
    @Test
    public void test1keyword1step()
    {

        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(1, Scenario.Keywords[ri]);
        a.accept(test);
        Assertions.assertEquals(1,test.getResult());
    }

    @Test
    public void test2steps1keyword()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(1);
        addPrimarySteps(1, Scenario.Keywords[ri]);
        a.accept(test);
        Assertions.assertEquals(1,test.getResult());
    }

    @Test
    public void test1stepWith1SubstepOn0And0Keywords()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(1);
        addSecondarySteps(0,1);
        a.accept(test);
        Assertions.assertEquals(0,test.getResult());
    }

    @Test
    public void test1stepWith2SubstepsOn01Keyword()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(1);
        addSecondarySteps(0,1,Scenario.Keywords[ri]);
        addSecondarySteps(0,1);
        a.accept(test);
        Assertions.assertEquals(1,test.getResult());
    }

    @Test
    public void test2stepsWith1SubstepOn0and2Keywords()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(2,Scenario.Keywords[ri]);
        addSecondarySteps(0,1);
        a.accept(test);
        Assertions.assertEquals(2,test.getResult());
    }

    @Test
    public void test2stepsWith2SubstepsOn0()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(2);
        addSecondarySteps(0,2);
        a.accept(test);
        Assertions.assertEquals(0,test.getResult());
    }

    @Test
    public void test2stepsWith2SubstepsOn1_2Keywords()
    {
        ScenarioKeywordCountVisitor test = new ScenarioKeywordCountVisitor();
        addPrimarySteps(2);
        addSecondarySteps(1,2,Scenario.Keywords[ri]);
        a.accept(test);
        Assertions.assertEquals(2,test.getResult());
    }
}

