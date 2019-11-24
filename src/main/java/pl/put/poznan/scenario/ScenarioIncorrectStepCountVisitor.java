package pl.put.poznan.scenario;

import java.util.ArrayList;

public class ScenarioIncorrectStepCountVisitor implements ScenarioElementVisitor
{

    public ArrayList<String> actors;
    int result;
    private boolean checkActorsInStep(Step step)
    {
        boolean res = false;
        for (String actor: actors)//foreach loop testing, if the step starts with actor
        {
            res = step.name.startsWith(actor);
            if (res) break; //breaking the loop if found
        }
        return res;
    }

    private boolean checkKeywordAndActorInStep(Step step)
    {
        boolean resK = false;
        boolean resA = false;
        for (String keyword: Scenario.keyWords)//foreach loop testing, if the step starts with keyword
        {
            resK = step.name.startsWith(keyword);
            for (String actor: actors)//foreach loop testing, if the step's next word is an actor
            {
                // [KEYWORD]:_[ACTOR] OR [KEYWORD]:[ACTOR]
                resA = (step.name.startsWith(actor, (keyword.length()+1)) || step.name.startsWith(actor, keyword.length()));
                if (resA) break; //breaking the loop if found
            }
            if (resK) break; //breaking the loop if found
        }
        return (resK && resA);
    }

    @Override
    public void visit (Scenario scenario)
    {
        actors = scenario.actors;
    }

    @Override
    public void visit (Step step)
    {
        if (step.substeps.isEmpty())
        {
            if(!checkActorsInStep(step))
                result++;
        }
        else
        {
            if(!checkKeywordAndActorInStep(step))
                result++;
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

    public int getResult ()
    {
        return result;
    }
}
