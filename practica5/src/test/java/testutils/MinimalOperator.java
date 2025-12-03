package testutils;

import problem.definition.Operator;
import problem.definition.State;
import java.util.ArrayList;
import java.util.List;

public class MinimalOperator extends Operator {

    @Override
    public List<State> generatedNewState(State stateCurrent, Integer operatornumber) {
        return new ArrayList<State>();
    }

    @Override
    public List<State> generateRandomState(Integer operatornumber) {
        return new ArrayList<State>();
    }
}
