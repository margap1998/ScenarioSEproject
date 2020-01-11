package pl.put.poznan.scenario.logic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.mockito.Mockito.when;

public class KeywordCountTests
{
    int ri;
    static Scenario a;
    @BeforeEach
    public void init()
    {
        ri = new Random().nextInt(3);
        String title = "tytuł";
        String actor1 ="user";
        String system ="System";
        JSONObject mockJSON= Mockito.mock(JSONObject.class);
        when(mockJSON.getString("title")).thenReturn(title);
        JSONArray mockActors = Mockito.mock(JSONArray.class);
        when(mockActors.length()).thenReturn(1);
        when(mockActors.getString(0)).thenReturn(actor1);
        when(mockJSON.getJSONArray("actors")).thenReturn(mockActors);
        when(mockJSON.getString("system")).thenReturn(system);
        JSONArray mockStepArray = Mockito.mock(JSONArray.class);
        when(mockStepArray.length()).thenReturn(0);
        when(mockJSON.getJSONArray("steps")).thenReturn(mockStepArray);
        try
        {
            a = new Scenario(mockJSON);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void addPrimarySteps(int q, String text)
    {
        JSONObject mockJSON = Mockito.mock(JSONObject.class);
        JSONArray mockSubs = Mockito.mock(JSONArray.class);
        when(mockSubs.length()).thenReturn(0);
        when(mockJSON.getString("name")).thenReturn(text);
        when(mockJSON.getJSONArray("substeps")).thenReturn(mockSubs);
        for(int i=0; i<q; i++)
        {
            a.steps.add(new Step(mockJSON));
        }
    }
    private void addSecondarySteps(int which,int q, String text)
    {
        JSONObject mockJSON = Mockito.mock(JSONObject.class);
        JSONArray mockSubs = Mockito.mock(JSONArray.class);
        when(mockSubs.length()).thenReturn(0);
        when(mockJSON.getString("name")).thenReturn(text);
        when(mockJSON.getJSONArray("substeps")).thenReturn(mockSubs);
        for(int i=0; i<q; i++)
        {
            a.steps.get(which).substeps.add(new Step(mockJSON));
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

