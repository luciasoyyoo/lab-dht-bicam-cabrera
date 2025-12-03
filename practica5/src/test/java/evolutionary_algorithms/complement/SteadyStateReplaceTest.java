package evolutionary_algorithms.complement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import metaheurictics.strategy.Strategy;
import problem.definition.State;
import problem.definition.Problem;
import problem.definition.Problem.ProblemType;

public class SteadyStateReplaceTest {

    private State s(double eval, Integer... codeVals) {
        State s = new State();
        ArrayList<Double> ev = new ArrayList<>(); ev.add(eval);
        s.setEvaluation(ev);
        ArrayList<Object> code = new ArrayList<>(); for(Integer c: codeVals) code.add(c); s.setCode(code);
        return s;
    }

    @Test public void t1_minValueSimple() {
        SteadyStateReplace r = new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(5)); list.add(s(3)); list.add(s(4)); assertEquals(3, r.minValue(list).getEvaluation().get(0)); }
    @Test public void t2_maxValueSimple() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(1)); list.add(s(9)); list.add(s(2)); assertEquals(9, r.maxValue(list).getEvaluation().get(0)); }
    @Test public void t3_minSingleElement() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(7)); assertEquals(7, r.minValue(list).getEvaluation().get(0)); }
    @Test public void t4_maxSingleElement() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(7)); assertEquals(7, r.maxValue(list).getEvaluation().get(0)); }
    @Test public void t5_minAllEqual() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(2)); list.add(s(2)); list.add(s(2)); assertEquals(2, r.minValue(list).getEvaluation().get(0)); }
    @Test public void t6_maxAllEqual() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(2)); list.add(s(2)); list.add(s(2)); assertEquals(2, r.maxValue(list).getEvaluation().get(0)); }
    @Test public void t7_replaceMaximizarReplacesMinWhenBetter() {
        Problem p=new Problem(); p.setTypeProblem(ProblemType.Maximizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(1); State b=s(5); State c=s(3); list.add(a); list.add(b); list.add(c);
        State candidate = s(10);
        List<State> out = r.replace(candidate, list);
        assertTrue(out.contains(candidate)); assertEquals(3, out.size()); }
    @Test public void t8_replaceMaximizarDoesNotReplaceWhenWorse() {
        Problem p=new Problem(); p.setTypeProblem(ProblemType.Maximizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(1); State b=s(5); list.add(a); list.add(b);
        State candidate = s(0);
        List<State> out = r.replace(candidate, list);
        assertFalse(out.contains(candidate)); }
    @Test public void t9_replaceMinimizarReplacesMaxWhenBetter() {
        Problem p=new Problem(); p.setTypeProblem(ProblemType.Minimizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(1); State b=s(8); list.add(a); list.add(b);
        State candidate = s(0);
        List<State> out = r.replace(candidate, list);
        assertTrue(out.contains(candidate)); }
    @Test public void t10_replaceMinimizarDoesNotReplaceWhenWorse() {
        Problem p=new Problem(); p.setTypeProblem(ProblemType.Minimizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(1); State b=s(2); list.add(a); list.add(b);
        State candidate = s(3);
        List<State> out = r.replace(candidate, list);
        assertFalse(out.contains(candidate)); }
    @Test public void t11_minValueReturnsReferenceFromList() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(4); State b=s(2); list.add(a); list.add(b); assertSame(b, r.minValue(list)); }
    @Test public void t12_maxValueReturnsReferenceFromList() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(4); State b=s(9); list.add(a); list.add(b); assertSame(b, r.maxValue(list)); }
    @Test public void t13_orderDoesNotMatterForMin() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(9)); list.add(s(1)); list.add(s(5)); assertEquals(1, r.minValue(list).getEvaluation().get(0)); }
    @Test public void t14_orderDoesNotMatterForMax() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(9)); list.add(s(1)); list.add(s(15)); assertEquals(15, r.maxValue(list).getEvaluation().get(0)); }
    @Test public void t15_replaceMaintainsSize() { Problem p=new Problem(); p.setTypeProblem(ProblemType.Maximizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(1)); list.add(s(2)); State candidate=s(10); assertEquals(2, r.replace(candidate, list).size()); }
    @Test public void t16_minWithNegativeValues() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(-1)); list.add(s(-5)); assertEquals(-5, r.minValue(list).getEvaluation().get(0)); }
    @Test public void t17_maxWithNegativeValues() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(-1)); list.add(s(-5)); assertEquals(-1, r.maxValue(list).getEvaluation().get(0)); }
    @Test public void t18_replaceOnBoundaryEqualMaximizar() { Problem p=new Problem(); p.setTypeProblem(ProblemType.Maximizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); State a=s(2); State b=s(5); list.add(a); list.add(b); State candidate=s(5); List<State> out=r.replace(candidate, list); assertTrue(out.contains(candidate)); }
    @Test public void t19_replaceOnBoundaryEqualMinimizar() { Problem p=new Problem(); p.setTypeProblem(ProblemType.Minimizar); Strategy.getStrategy().setProblem(p);
        SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(2)); list.add(s(5)); State candidate=s(5); List<State> out=r.replace(candidate, list); assertTrue(out.contains(candidate)); }
    @Test public void t20_minMaxSingleElementList() { SteadyStateReplace r=new SteadyStateReplace(); List<State> list=new ArrayList<>(); list.add(s(42)); assertEquals(42, r.minValue(list).getEvaluation().get(0)); assertEquals(42, r.maxValue(list).getEvaluation().get(0)); }
}
