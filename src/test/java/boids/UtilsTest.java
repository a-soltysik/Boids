package boids;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilsTest {

    //https://embeddeduse.com/2019/08/26/qt-compare-two-floats/
    @Test
    public void isEqualTest() {
        final int total = 10000;
        for (int i=0; i<total; i++) {
            float expected = i / 10.0f;
            float actual = calculate(9.0f + expected, 0.2f, 45);
            assertTrue(Utils.isEqual(expected, actual));
        }
    }

    private float calculate(float start, float tick, int multiplier) {
        for (int i=0; i<multiplier; i++) {
            start -= tick;
        }
        return start;
    }
}
