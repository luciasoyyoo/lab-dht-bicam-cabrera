package local_search.complement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UpdateParameterTest {

    @Test
    public void updateParameterIncrements() throws Exception {
        Integer start = 0;
        Integer next = UpdateParameter.updateParameter(start);
        assertEquals(Integer.valueOf(1), next);
        Integer next2 = UpdateParameter.updateParameter(next);
        assertEquals(Integer.valueOf(2), next2);
    }
}
