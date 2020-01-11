package pl.put.poznan.scenario.logic;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

public class IncorrectStepsTests
{
    @Test
    public void testIncorrectStepsVisitorWithCorrectSimpleStep()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "User does something on System";
        testVis.visit(mockStep);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsVisitorWithIncorrectSimpleStep()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "Andrzej does nothing on System";
        testVis.visit(mockStep);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }
    @Test
    public void testIncorrectStepsKeywordIFIncorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "IF: He does something on System";
        testVis.visit(mockStep);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }
    @Test
    public void testIncorrectStepsKeywordELSEIncorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "ELSE: Darth Vader does something on System";
        testVis.visit(mockStep);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }
    @Test
    public void testIncorrectStepsKeywordFOREACHIncorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "FOR EACH: xd lol";
        testVis.visit(mockStep);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }

    @Test
    public void testIncorrectStepsKeywordIFCorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "IF: User does something on System";
        testVis.visit(mockStep);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsKeywordELSECorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "ELSE: User gets back";
        testVis.visit(mockStep);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsKeywordFOREACHCorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = "FOR EACH User System sleeps";
        testVis.visit(mockStep);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsNull()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = Mockito.mock(Step.class);
        mockStep.name = null;
        testVis.visit(mockStep);
        Assertions.assertFalse(testVis.getIncorrectSteps().isEmpty());
    }
}
