package pl.put.poznan.scenario.logic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;


public class ScenerioStepCountTests
{
    static Scenario a;
    @BeforeEach
    public void init()
    {

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
    @Test
    public void scenarioLoadingTest()
    {
        Assertions.assertTrue(a.actors.get(0).equals("user")&&a.title.equals("tytuł")&&a.system.equals("System") &&a.steps.isEmpty());
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
        JSONObject mockJSON = Mockito.mock(JSONObject.class);
        JSONArray mockSubs = Mockito.mock(JSONArray.class);
        when(mockSubs.length()).thenReturn(0);
        when(mockJSON.getString("name")).thenReturn("");
        when(mockJSON.getJSONArray("substeps")).thenReturn(mockSubs);
        for(int i=0; i<q; i++)
        {
            a.steps.add(new Step(mockJSON));
        }
    }
    private void addSecondarySteps(int which,int q)
    {
        JSONObject mockJSON = Mockito.mock(JSONObject.class);
        JSONArray mockSubs = Mockito.mock(JSONArray.class);
        when(mockSubs.length()).thenReturn(0);
        when(mockJSON.getString("name")).thenReturn("");
        when(mockJSON.getJSONArray("substeps")).thenReturn(mockSubs);
        for(int i=0; i<q; i++)
        {
            a.steps.get(which).substeps.add(new Step(mockJSON));
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
