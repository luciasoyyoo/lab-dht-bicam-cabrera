package testutils;

import problem.definition.Problem;
import problem.definition.State;
import problem.definition.Codification;
import problem.definition.Operator;

import java.util.ArrayList;
import java.util.List;

public class MinimalProblem extends Problem {

    private Codification codification;

    public MinimalProblem(Codification codification) {
        this.codification = codification;
    }

    @Override
    public Codification getCodification() {
        return codification;
    }

    @Override
    public void setCodification(Codification codification) {
        this.codification = codification;
    }
    
    // We rely on the parent Problem's implementations for other behaviors.
}
