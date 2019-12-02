package pl.put.poznan.scenario.logic;

/**
 * A class that counts steps beginning with a keyword
 */

public class ScenarioKeywordCountVisitor implements ScenarioElementVisitor
{
    int result;

    private boolean checkKeyword (Step step)
    {
        for (String keyword : Scenario.Keywords)
            if (step.name.startsWith(keyword))
                return true;

        return false;
    }

    /**
     * Inits the result before processing steps
     * @param scenario Scenario to process
     */
    @Override
    public void visit (Scenario scenario)
    {
        result = 0;
    }

    /**
     * Checks whether the step starts with a keyword and increments the result if so
     * @param step Step to check
     */
    @Override
    public void visit (Step step)
    {
        if(checkKeyword(step))
        {
            result += 1;
        }
    }

    @Override
    public void startRecursion () {}

    @Override
    public void endRecursion () {}

    /**
     * A getter for actual state of result
     * @return number of steps starting with a keyword in the processed scenario
     */
    public int getResult()
    {
        return result;
    }
}
