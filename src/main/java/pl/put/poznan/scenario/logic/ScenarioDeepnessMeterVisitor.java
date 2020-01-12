package pl.put.poznan.scenario.logic;

import java.util.ArrayList;

public class ScenarioDeepnessMeterVisitor implements ScenarioElementVisitor
{
    public int getDeepest ()
    {
        return deepest;
    }

    int deepest;
    int actual;
    public ScenarioDeepnessMeterVisitor()
    {
        deepest = 0;
        actual = 0;
    }
    @Override
    public void visit (Scenario scenario)
    {
        deepest = 0;
        actual = 0;
    }

    @Override
    public void visit (Step step)
    {
        actual +=1;
        if (deepest < actual)
            deepest = actual;
        actual -= 1;
    }

    @Override
    public void startRecursion ()
    {
        actual+=1;
    }

    @Override
    public void endRecursion ()
    {
        actual-=1;
    }

}
