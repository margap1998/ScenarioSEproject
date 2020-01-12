package pl.put.poznan.scenario.logic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class StepTest
{
    @Mock
    JSONObject mockJSON;
    @Mock
    JSONArray mockSubsteps0;
    @Mock
    JSONArray mockSubsteps1;
    @Mock
    JSONObject mockSubstep;
    @Test
    public void testStepOnlyName()
    {
        try
        {
            mockSubsteps0 = Mockito.mock(JSONArray.class);
            when(mockSubsteps0.length()).thenReturn(0);

            mockJSON = Mockito.mock(JSONObject.class);
            when(mockJSON.getString("name")).thenReturn("User");
            when(mockJSON.getJSONArray("substeps")).thenReturn(mockSubsteps0);

            Step a = new Step(mockJSON);
            Assertions.assertTrue(a.substeps.isEmpty() && a.name.equals("User"));
        }catch (Exception e)
        {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void testStepNameAndSubstep()
    {
        try
        {
            mockSubsteps0 = Mockito.mock(JSONArray.class);
            when(mockSubsteps0.length()).thenReturn(0);

            mockSubstep = Mockito.mock(JSONObject.class);
            when(mockSubstep.getString("name")).thenReturn("User");
            when(mockSubstep.getJSONArray("substeps")).thenReturn(mockSubsteps0);

            mockSubsteps1 = Mockito.mock(JSONArray.class);
            when(mockSubsteps1.length()).thenReturn(1);
            when(mockSubsteps1.getJSONObject(0)).thenReturn(mockSubstep);

            mockJSON = Mockito.mock(JSONObject.class);
            when(mockJSON.getString("name")).thenReturn("IF: System");
            when(mockJSON.getJSONArray("substeps")).thenReturn(mockSubsteps1);
            when(mockJSON.has("substeps")).thenReturn(true);
            Step a = new Step(mockJSON);
            Assertions.assertTrue(a.name.equals("IF: System") && a.substeps.get(0).name.equals("User")&& a.substeps.get(0).substeps.isEmpty());
        }catch (Exception e)
        {
            e.printStackTrace();
            Assertions.assertTrue(false);
        }
    }
}
