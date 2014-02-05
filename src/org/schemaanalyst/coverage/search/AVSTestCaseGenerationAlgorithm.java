package org.schemaanalyst.coverage.search;

import org.schemaanalyst.coverage.criterion.Predicate;
import org.schemaanalyst.coverage.search.objectivefunction.PredicateObjectiveFunction;
import org.schemaanalyst.coverage.testgeneration.TestCase;
import org.schemaanalyst.coverage.testgeneration.TestCaseGenerationAlgorithm;
import org.schemaanalyst.coverage.testgeneration.TestSuite;
import org.schemaanalyst.data.Data;
import org.schemaanalyst.datageneration.cellrandomisation.CellRandomiser;
import org.schemaanalyst.datageneration.cellrandomisation.CellRandomiserFactory;
import org.schemaanalyst.datageneration.search.AlternatingValueSearch;
import org.schemaanalyst.datageneration.search.datainitialization.NoDataInitialization;
import org.schemaanalyst.datageneration.search.datainitialization.RandomDataInitializer;
import org.schemaanalyst.datageneration.search.objective.ObjectiveValue;
import org.schemaanalyst.datageneration.search.termination.CombinedTerminationCriterion;
import org.schemaanalyst.datageneration.search.termination.CounterTerminationCriterion;
import org.schemaanalyst.datageneration.search.termination.OptimumTerminationCriterion;
import org.schemaanalyst.datageneration.search.termination.TerminationCriterion;
import org.schemaanalyst.util.random.Random;
import org.schemaanalyst.util.random.SimpleRandom;

/**
 * Created by phil on 03/02/2014.
 */
public class AVSTestCaseGenerationAlgorithm extends TestCaseGenerationAlgorithm {

    private AlternatingValueSearch avs;

    public AVSTestCaseGenerationAlgorithm() {

        // TODO: parameterise this stuff

        Random random = new SimpleRandom(0);
        CellRandomiser cellRandomiser = CellRandomiserFactory.small(random);

        avs = new AlternatingValueSearch(random,
                new NoDataInitialization(),
                new RandomDataInitializer(cellRandomiser));

        TerminationCriterion terminationCriterion = new CombinedTerminationCriterion(
                new CounterTerminationCriterion(avs.getEvaluationsCounter(), 100000),
                new OptimumTerminationCriterion<>(avs));

        avs.setTerminationCriterion(terminationCriterion);
    }

    @Override
    public TestCase generateTestCase(Data data, Data state, Predicate predicate) {

        avs.setObjectiveFunction(new PredicateObjectiveFunction(predicate, state));
        avs.initialize();
        avs.search(data);

        ObjectiveValue bestObjectiveValue = avs.getBestObjectiveValue();
        boolean success = bestObjectiveValue.isOptimal();

        TestCase testCase = new TestCase(data, state, predicate, success);
        testCase.addInfo("objval", bestObjectiveValue);

        return testCase;
    }

    @Override
    public TestCase checkIfTestCaseExists(Predicate predicate, TestSuite testSuite) {

        for (TestCase testCase : testSuite.getAllTestCases()) {
            PredicateObjectiveFunction objFun = new PredicateObjectiveFunction(predicate, testCase.getState());
            ObjectiveValue objVal = objFun.evaluate(testCase.getData());
            if (objVal.isOptimal()) {
                return testCase;
            }
        }

        return null;
    }
}
