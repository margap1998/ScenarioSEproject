package pl.put.poznan.scenario.logic;

import org.junit.jupiter.api.*;

/**
 * Unit tests class for ScenarioIncorrectStepListVisitor.
 */
public class IncorrectStepsTests
{
    /**
     * This test method checks reaction for 1 Step with correct name, that means, it starts with Actor{System or actors defined in Scenario}
     */
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

    /**
     * This test method checks reaction for 1 Step with non-defined actor at beginning.
     */
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

    /**
     * This method checks the behaviour of step starting with keyword "IF:" and incorrect actor.
     */
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

    /**
     * This method checks the behaviour of step starting with keyword "ELSE:" and incorrect actor.
     */
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

    /**
     * This method checks the behaviour of step starting with keyword "FOR EACH" and incorrect actor.
     */
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

    /**
     * This method checks the behaviour of step starting with keyword "IF:" and correct actor.
     */
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

    /**
     * This method checks the behaviour of step starting with keyword "ELSE:" and correct actor.
     */
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

    /**
     * This method checks the behaviour of step starting with keyword "FOR EACH" and correct actor.
     */
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

    /**
     * This method checks the behaviour of step with null instead of empty string as name
     */
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

    /**
     * Tests behaviour for made-up scenario with correct steps
     */
    @Test
    public void testCorrectScenario()
    {
        Scenario a = new Scenario();
        a.title = "Bakery";
        a.system = "System";
        a.actors.add("User");
        a.steps.add(new Step("User ..."));
        a.steps.add(new Step("User ..."));
        a.steps.add(new Step("System ..."));
        a.steps.add(new Step("IF: User..."));
        a.steps.get(3).substeps.add(new Step("System ..."));
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        a.accept(testVis);
        Assertions.assertTrue(testVis.getIncorrectSteps().isEmpty());
    }

    /**
     * Tests behaviour for mede-up Scenario with 2 incorrect steps
     */
    @Test
    public void testIncorrectScenario()
    {
        Scenario a = new Scenario();
        a.title = "Bakery";
        a.system = "System";
        a.actors.add("User");
        a.steps.add(new Step("User ..."));
        a.steps.add(new Step("User ..."));
        a.steps.add(new Step("The bread ..."));
        a.steps.add(new Step("IF: Cakes..."));
        a.steps.get(3).substeps.add(new Step("System ..."));
        ScenarioIncorrectStepListVisitor testVis = new ScenarioIncorrectStepListVisitor();
        a.accept(testVis);
        Assertions.assertTrue(testVis.IncorrectSteps.contains("The bread ...")&& testVis.IncorrectSteps.contains("IF: Cakes..."));
    }
}
