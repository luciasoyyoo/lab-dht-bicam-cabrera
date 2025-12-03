package testutils;

import problem.definition.Operator;
import problem.definition.State;
import java.util.ArrayList;
import java.util.List;

public class MinimalOperator extends Operator {

    @Override
    public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
        ArrayList<State> l = new ArrayList<>();
        State s = new State();
        s.setCode(stateCurrent == null ? new ArrayList<Object>() {{ add(0); }} : stateCurrent.getCode());
        ArrayList<Double> ev = new ArrayList<>();
        ev.add(0.0);
        s.setEvaluation(ev);
        l.add(s);
        return l;
    }

    @Override
    public List<State> generateRandomState(Integer operatornumber) {
        ArrayList<State> l = new ArrayList<>();
        State s = new State();
        s.setCode(new ArrayList<Object>() {{ add(0); }});
        ArrayList<Double> ev = new ArrayList<>();
        ev.add(0.0);
        s.setEvaluation(ev);
        l.add(s);
        return l;
    }
}
