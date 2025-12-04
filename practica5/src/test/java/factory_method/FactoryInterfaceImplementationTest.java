package factory_method;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import evolutionary_algorithms.complement.CrossoverType;
import evolutionary_algorithms.complement.DistributionType;
import evolutionary_algorithms.complement.MutationType;
import evolutionary_algorithms.complement.ReplaceType;
import evolutionary_algorithms.complement.SamplingType;
import evolutionary_algorithms.complement.SelectionType;
import local_search.acceptation_type.AcceptType;
import local_search.candidate_type.CandidateType;
import metaheuristics.generators.GeneratorType;
import problem.extension.TypeSolutionMethod;

public class FactoryInterfaceImplementationTest {

    @Test
    public void factoryGeneratorCreatesRandomSearch() throws Exception {
        FactoryGenerator fg = new FactoryGenerator();
        metaheuristics.generators.Generator g = fg.createGenerator(GeneratorType.RandomSearch);
        assertNotNull(g);
        assertTrue(g instanceof metaheuristics.generators.RandomSearch);
    }

    @Test
    public void factoryAcceptCandidateCreates() throws Exception {
        FactoryAcceptCandidate fac = new FactoryAcceptCandidate();
        local_search.acceptation_type.AcceptableCandidate ac = fac.createAcceptCandidate(AcceptType.AcceptNotDominated);
        assertNotNull(ac);
        assertTrue(ac instanceof local_search.acceptation_type.AcceptableCandidate);
    }

    @Test
    public void factoryCrossoverCreatesOnePoint() throws Exception {
        FactoryCrossover fc = new FactoryCrossover();
        evolutionary_algorithms.complement.Crossover c = fc.createCrossover(CrossoverType.OnePointCrossover);
        assertNotNull(c);
    }

    @Test
    public void factoryDistributionCreatesUnivariate() throws Exception {
        FactoryDistribution fd = new FactoryDistribution();
        evolutionary_algorithms.complement.Distribution d = fd.createDistribution(DistributionType.Univariate);
        assertNotNull(d);
    }

    @Test
    public void factoryMutationCreatesOnePoint() throws Exception {
        FactoryMutation fm = new FactoryMutation();
        evolutionary_algorithms.complement.Mutation m = fm.createMutation(MutationType.OnePointMutation);
        assertNotNull(m);
    }

    @Test
    public void factoryReplaceCreatesSteadyState() throws Exception {
        FactoryReplace fr = new FactoryReplace();
        evolutionary_algorithms.complement.Replace r = fr.createReplace(ReplaceType.SteadyStateReplace);
        assertNotNull(r);
    }

    @Test
    public void factoryCandidateCreatesRandomCandidate() throws Exception {
        FactoryCandidate fcand = new FactoryCandidate();
        local_search.candidate_type.SearchCandidate sc = fcand.createSearchCandidate(CandidateType.RandomCandidate);
        assertNotNull(sc);
    }

    @Test
    public void factorySamplingCreatesProbabilistic() throws Exception {
        FactorySampling fs = new FactorySampling();
        evolutionary_algorithms.complement.Sampling s = fs.createSampling(SamplingType.ProbabilisticSampling);
        assertNotNull(s);
    }

    @Test
    public void factoryFatherSelectionCreatesRoulette() throws Exception {
        FactoryFatherSelection fsel = new FactoryFatherSelection();
        evolutionary_algorithms.complement.FatherSelection fs = fsel.createSelectFather(SelectionType.RouletteSelection);
        assertNotNull(fs);
    }

    @Test
    public void factorySolutionMethodCreates() throws Exception {
        FactorySolutionMethod fsm = new FactorySolutionMethod();
        problem.extension.SolutionMethod sm = fsm.createdSolutionMethod(TypeSolutionMethod.FactoresPonderados);
        assertNotNull(sm);
    }
}
