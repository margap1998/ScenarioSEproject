package pl.put.poznan.scenario.logic;

import java.util.ArrayList;

/**
 * A class that looks for incorrect steps
 */

public class ScenarioIncorrectStepListVisitor implements ScenarioElementVisitor
{
    public ArrayList<String> actors;
    public ArrayList<String> IncorrectSteps;
    public ScenarioIncorrectStepListVisitor()
    {
        IncorrectSteps = new ArrayList<String>();
		    actors = new ArrayList<String>();
    }
    private boolean checkActorsInStep(Step step)
    {
        boolean res = false;
        for (String actor : actors)//foreach loop testing, if the step starts with actor
        {
            res = step.name.startsWith(actor);
            if (res) break; //breaking the loop if found
        }
        return res;
    }

    private boolean checkKeywordAndActorInStep (Step step)
    {
        boolean resK = false;
        boolean resA = false;
        for (String keyword : Scenario.Keywords)//foreach loop testing, if the step starts with keyword
        {
            {
                resK = step.name.startsWith(keyword);
                for (String actor : actors)//foreach loop testing, if the step's next word is an actor
                {
                    // [KEYWORD]:_[ACTOR] OR [KEYWORD]:[ACTOR]
                    resA = (step.name.startsWith(actor, (keyword.length() + 1)) || step.name.startsWith(actor, keyword.length()));
                    if (resA) break; //breaking the loop if found
                }
                if (resK) break; //breaking the loop if found
            }
        }
        return (resK && resA);
    }

	/**
	 * Adding actors to look for in steps.
	 * @param scenario Scenario that is processing
	 */
    @Override
    public void visit (Scenario scenario)
    {
	for(String a:scenario.actors)
		actors.add(a);
        actors.add(scenario.system);
    }
    /**
     * Checks step if it has an actor or keyword and actor at beginning
     * @param step Step to check
     */
    @Override
    public void visit (Step step)
    {
        if (step.substeps.isEmpty())
        {
            if (!checkActorsInStep(step))
            {
                IncorrectSteps.add(step.name);
            }
        } else
        {
            if(!checkKeywordAndActorInStep(step))
            {
			    IncorrectSteps.add(step.name);
		    }
        }
    }

    @Override
    public void startRecursion ()
    {

    }

    @Override
    public void endRecursion ()
    {

    }
	/**
	 * A getter for actual state of result
	 * @return List of incorrect steps.
	 */
    public ArrayList<String> getIncorrectSteps ()
    {
        return IncorrectSteps;
    }
}
