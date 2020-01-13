package pl.put.poznan.scenario.logic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import static org.mockito.Mockito.*;

public class ScenarioTest
{
    @Mock JSONArray mockStepArray;
    @Mock JSONArray mockActors;
    @Mock JSONObject mockJSON;
    @Mock JSONObject mockStep;
    @Mock
    JSONArray mockSubsteps0;
    @Mock
    JSONArray mockSubsteps1;
    @Mock
    JSONObject mockSubstep;

    @Test
    public void loadingTest1()
    {
        {
            try
            {
                String title = "tytuł";
                String actor1 ="user";
                String system ="System";
                mockStepArray = Mockito.mock(JSONArray.class);
                when(mockStepArray.length()).thenReturn(0);

                mockActors = Mockito.mock(JSONArray.class);
                when(mockActors.length()).thenReturn(1);
                when(mockActors.getString(0)).thenReturn(actor1);

                mockJSON = Mockito.mock(JSONObject.class);
                when(mockJSON.getString("title")).thenReturn(title);
                when(mockJSON.getJSONArray("actors")).thenReturn(mockActors);
                when(mockJSON.getString("system")).thenReturn(system);
                when(mockJSON.getJSONArray("steps")).thenReturn(mockStepArray);

                Scenario a = new Scenario(mockJSON);
                Assertions.assertTrue(a.actors.get(0).equals("user")&&a.title.equals("tytuł")&&a.system.equals("System") &&a.steps.isEmpty());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Assertions.assertFalse(true);
            }
        }
    }
    @Test
    public void loadingTest2()
    {
        {
            try
            {
                String title = "tytuł";
                String actor1 ="user";
                String system ="System";
                mockStepArray = Mockito.mock(JSONArray.class);
                when(mockStepArray.length()).thenReturn(1);

                mockActors = Mockito.mock(JSONArray.class);
                when(mockActors.length()).thenReturn(1);
                when(mockActors.getString(0)).thenReturn(actor1);


                mockStep = Mockito.mock(JSONObject.class);
                when(mockStep.getString("name")).thenReturn("User");
                when(mockStep.getJSONArray("substeps")).thenReturn(mockSubsteps0);
                when(mockStepArray.getJSONObject(0)).thenReturn(mockStep);
                mockJSON = Mockito.mock(JSONObject.class);
                when(mockJSON.getString("title")).thenReturn(title);
                when(mockJSON.getJSONArray("actors")).thenReturn(mockActors);
                when(mockJSON.getString("system")).thenReturn(system);
                when(mockJSON.getJSONArray("steps")).thenReturn(mockStepArray);

                Scenario a = new Scenario(mockJSON);
                Assertions.assertTrue(a.actors.get(0).equals("user")&&a.title.equals("tytuł")&&a.system.equals("System")
                        && a.steps.get(0).name.equals("User")&& a.steps.get(0).substeps.isEmpty());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Assertions.assertFalse(true);
            }
        }
    }
    @Test
    public void loadingTest3()
    {
        {
            try
            {
                String title = "tytuł";
                String actor1 ="user";
                String system ="System";
                mockStepArray = Mockito.mock(JSONArray.class);
                when(mockStepArray.length()).thenReturn(2);

                mockActors = Mockito.mock(JSONArray.class);

                when(mockActors.length()).thenReturn(1);
                when(mockActors.getString(0)).thenReturn(actor1);

                mockSubsteps0 = Mockito.mock(JSONArray.class);
                when(mockSubsteps0.length()).thenReturn(0);

                mockSubstep = Mockito.mock(JSONObject.class);
                when(mockSubstep.getString("name")).thenReturn("User");
                when(mockSubstep.getJSONArray("substeps")).thenReturn(mockSubsteps0);

                mockSubsteps1 = Mockito.mock(JSONArray.class);
                when(mockSubsteps1.length()).thenReturn(1);
                when(mockSubsteps1.getJSONObject(0)).thenReturn(mockSubstep);

                mockStep = Mockito.mock(JSONObject.class);
                when(mockStep.getString("name")).thenReturn("IF: System");
                when(mockStep.getJSONArray("substeps")).thenReturn(mockSubsteps1);
                when(mockStep.has("substeps")).thenReturn(true);
                when(mockStepArray.getJSONObject(0)).thenReturn(mockStep);
                when(mockStepArray.getJSONObject(1)).thenReturn(mockSubstep);

                mockJSON = Mockito.mock(JSONObject.class);
                when(mockJSON.getString("title")).thenReturn(title);
                when(mockJSON.getJSONArray("actors")).thenReturn(mockActors);
                when(mockJSON.getString("system")).thenReturn(system);
                when(mockJSON.getJSONArray("steps")).thenReturn(mockStepArray);

                Scenario a = new Scenario(mockJSON);
                Assertions.assertTrue(a.actors.get(0).equals("user")&&a.title.equals("tytuł")&&a.system.equals("System"));
                Assertions.assertTrue(a.steps.get(0).name.equals("IF: System")&&
                        a.steps.get(1).name.equals("User")&&a.steps.get(1).substeps.isEmpty()&&
                        a.steps.get(0).substeps.get(0).name.equals("User")&&a.steps.get(0).substeps.get(0).substeps.isEmpty());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Assertions.assertFalse(true);
            }
        }
    }

    @Test
    public void invalidScenarioClear()
    {
        Exception ex = Assertions.assertThrows(Exception.class, ()->{
            String title = "tytuł";
            String actor1 ="user";
            String system ="System";

            mockJSON = Mockito.mock(JSONObject.class);
            Scenario a = new Scenario(mockJSON);
        });
    }

    @Test
    public void invalidScenarioOnlyTitle()
    {
        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            String title = "tytuł";
            String actor1 = "user";
            String system = "System";

            mockJSON = Mockito.mock(JSONObject.class);
            when(mockJSON.getString("title")).thenReturn(title);
            Scenario a = new Scenario(mockJSON);
        });
    }

    @Test
    public void invalidScenarioOnlySystem()
    {
        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            String title = "tytuł";
            String actor1 = "user";
            String system = "System";

            mockJSON = Mockito.mock(JSONObject.class);
            when(mockJSON.getString("system")).thenReturn(system);
            Scenario a = new Scenario(mockJSON);
        });
    }

    @Test
    public void invalidScenarioOnlySystemAndTitle()
    {
        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            String title = "tytuł";
            String actor1 = "user";
            String system = "System";

            mockJSON = Mockito.mock(JSONObject.class);
            when(mockJSON.getString("system")).thenReturn(system);
            when(mockJSON.getString("title")).thenReturn(title);
            Scenario a = new Scenario(mockJSON);
        });
    }

    @Test
    public void invalidScenarioOnlyHeader()
    {
        Exception ex = Assertions.assertThrows(Exception.class, () -> {
            String title = "tytuł";
            String actor1 = "user";
            String system = "System";

            mockActors = Mockito.mock(JSONArray.class);
            when(mockActors.length()).thenReturn(1);
            when(mockActors.getString(0)).thenReturn(actor1);

            mockJSON = Mockito.mock(JSONObject.class);
            when(mockJSON.getString("title")).thenReturn(title);
            when(mockJSON.getJSONArray("actors")).thenReturn(mockActors);
            when(mockJSON.getString("system")).thenReturn(system);
            when(mockJSON.getJSONArray("steps")).thenThrow(new Exception());
            Scenario a = new Scenario(mockJSON);
        });
    }
}
