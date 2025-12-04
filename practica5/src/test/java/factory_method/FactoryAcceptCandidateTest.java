package factory_method;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import local_search.acceptation_type.AcceptType;
import local_search.acceptation_type.AcceptableCandidate;

public class FactoryAcceptCandidateTest {

    @Test
    public void createAllAcceptTypes_shouldReturnConcreteInstances() throws Exception {
        FactoryAcceptCandidate factory = new FactoryAcceptCandidate();
        for (AcceptType type : AcceptType.values()) {
            AcceptableCandidate ac = factory.createAcceptCandidate(type);
            assertNotNull(ac, "Factory must return non-null for " + type);
            // The created instance class simple name should match the enum name (convention used in Factory)
            assertEquals(type.toString(), ac.getClass().getSimpleName(), "Returned class name should match AcceptType");
        }
    }

}
