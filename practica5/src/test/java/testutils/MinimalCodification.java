package testutils;

import problem.definition.Codification;
import problem.definition.State;

public class MinimalCodification extends Codification {

    private int variableCount;

    public MinimalCodification(int variableCount) {
        this.variableCount = variableCount;
    }

    @Override
    public boolean validState(State state) {
        return true;
    }

    @Override
    public Object getVariableAleatoryValue(int key) {
        return 0;
    }

    @Override
    public int getAleatoryKey() {
        return 0;
    }

    @Override
    public int getVariableCount() {
        return variableCount;
    }
}
