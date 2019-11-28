package pl.put.poznan.scenario;

import java.util.ArrayList;

public class ScenarioIncorrectStepListVisitor implements ScenarioElementVisitor
{
    public ArrayList<String> actors;
    public ArrayList<String> IncorrectSteps;
    public ScenarioIncorrectStepListVisitor()
    {
        IncorrectSteps = new ArrayList<String>();
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

    @Override
    public void visit (Scenario scenario)
    {
        actors = scenario.actors;
        actors.add(scenario.system);
    }

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

    public ArrayList<String> getIncorrectSteps ()
    {
        return IncorrectSteps;
    }
}
