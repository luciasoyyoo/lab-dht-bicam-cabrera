package factory_method;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import es.ull.App;

public class FactoryLoaderInstanceTest {

    @Test
    public void testGetInstanceCreatesApp() throws Exception {
        Object o = FactoryLoader.getInstance("es.ull.App");
        assertNotNull(o);
        assertTrue(o instanceof App);
    }
}
