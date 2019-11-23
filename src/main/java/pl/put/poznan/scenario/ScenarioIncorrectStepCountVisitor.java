package pl.put.poznan.scenario;

import java.util.ArrayList;

public class ScenarioIncorrectStepCountVisitor implements ScenarioElementVisitor
{

    public ArrayList<String> actors;
    int result;
    public ScenerioIncorrectStepCountVisitor()
    {
        result = 0;
    }
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

    private boolean checkKeywordsInStep(Step step)
    {
        boolean res = false;
        for (String keyword: Scenario.keyWords)//foreach loop testing, if the step starts with keyword
        {
            res = step.name.startsWith(keyword);
            if (res) break; //breaking the loop if found
        }
        return res;
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
            if(!checkKeywordsInStep(step))
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
}
