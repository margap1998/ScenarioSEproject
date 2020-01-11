package pl.put.poznan.scenario.logic;

import org.junit.jupiter.api.*;

public class IncorrectStepsTests
{
    @Test
    public void testIncorrectStepsVisitorWithCorrectSimpleStep()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "User does something on System";
        mockStep.accept(testVis);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsVisitorWithIncorrectSimpleStep()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "Andrzej does nothing on System";
        mockStep.accept(testVis);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }
    @Test
    public void testIncorrectStepsKeywordIFIncorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "IF: He does something on System";
        mockStep.accept(testVis);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }
    @Test
    public void testIncorrectStepsKeywordELSEIncorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "ELSE: Darth Vader does something on System";
        mockStep.accept(testVis);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }
    @Test
    public void testIncorrectStepsKeywordFOREACHIncorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "FOR EACH: xd lol";
        mockStep.accept(testVis);
        Assertions.assertEquals(mockStep.name,testVis.getIncorrectSteps().get(0));
    }

    @Test
    public void testIncorrectStepsKeywordIFCorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "IF: User does something on System";
        mockStep.accept(testVis);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsKeywordELSECorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "ELSE: User gets back";
        mockStep.accept(testVis);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsKeywordFOREACHCorrect2()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = "FOR EACH User System sleeps";
        mockStep.accept(testVis);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }
    @Test
    public void testIncorrectStepsNull()
    {
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        testVis.actors.add("System");
        testVis.actors.add("User");
        Step mockStep = new Step();
        mockStep.name = null;
        mockStep.accept(testVis);
        Assertions.assertFalse(testVis.getIncorrectSteps().isEmpty());
    }
}
