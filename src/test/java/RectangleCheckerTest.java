import org.example.RectangleChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RectangleChecker Tests")
class RectangleCheckerTest {
    @ParameterizedTest(name = "INSIDE: A({0}, {1})")
    @CsvSource({
            " 0,  0",
            " 1,  2",
            "-1, -2",
            " 2,  3",
            "-2, -3"
    })
    @DisplayName("Points strictly inside the rectangle")
    void testInside(int x, int y) {
        assertEquals(RectangleChecker.Position.INSIDE, RectangleChecker.classify(x, y));
    }

    @ParameterizedTest(name = "OUTSIDE: A({0}, {1})")
    @CsvSource({
            "-5,  0",
            " 5,  0",
            " 0, -6",
            " 0,  6",
            "-5, -6",
            " 5,  6",
            "-4,  0",
            " 4,  0",
            " 0, -5",
            " 0,  5"
    })
    @DisplayName("Points strictly outside the rectangle")
    void testOutside(int x, int y) {
        assertEquals(RectangleChecker.Position.OUTSIDE, RectangleChecker.classify(x, y));
    }


    @ParameterizedTest(name = "CORNER: A({0}, {1})")
    @CsvSource({
            "-3, -4",
            " 3, -4",
            "-3,  4",
            " 3,  4"
    })
    @DisplayName("Points at rectangle corners")
    void testCorner(int x, int y) {
        assertEquals(RectangleChecker.Position.CORNER, RectangleChecker.classify(x, y));
    }



    @ParameterizedTest(name = "HORIZONTAL_BORDER: A({0}, {1})")
    @CsvSource({
            " 0, -4",
            " 0,  4",
            "-2, -4",
            " 2,  4",
            " 1, -4",
            "-1,  4"
    })
    @DisplayName("Points on horizontal borders (y = ±4, x strictly in (-3,3))")
    void testHorizontalBorder(int x, int y) {
        assertEquals(RectangleChecker.Position.HORIZONTAL_BORDER, RectangleChecker.classify(x, y));
    }


    @ParameterizedTest(name = "VERTICAL_BORDER: A({0}, {1})")
    @CsvSource({
            "-3,  0",
            " 3,  0",
            "-3,  2",
            " 3, -2",
            "-3, -1",
            " 3,  1"
    })
    @DisplayName("Points on vertical borders (x = ±3, y strictly in (-4,4))")
    void testVerticalBorder(int x, int y) {
        assertEquals(RectangleChecker.Position.VERTICAL_BORDER, RectangleChecker.classify(x, y));
    }


    @Test
    @DisplayName("Origin (0,0) is INSIDE")
    void testOriginIsInside() {
        assertEquals(RectangleChecker.Position.INSIDE, RectangleChecker.classify(0, 0));
    }

    @Test
    @DisplayName("x just inside left border: (-2, 0) → INSIDE")
    void testJustInsideLeftBorder() {
        assertEquals(RectangleChecker.Position.INSIDE, RectangleChecker.classify(-2, 0));
    }

    @Test
    @DisplayName("x just outside right border: (4, 0) → OUTSIDE")
    void testJustOutsideRightBorder() {
        assertEquals(RectangleChecker.Position.OUTSIDE, RectangleChecker.classify(4, 0));
    }

    @Test
    @DisplayName("y just outside top border: (0, 5) → OUTSIDE")
    void testJustOutsideTopBorder() {
        assertEquals(RectangleChecker.Position.OUTSIDE, RectangleChecker.classify(0, 5));
    }

    @Test
    @DisplayName("y just inside bottom border: (0, -3) → INSIDE")
    void testJustInsideBottomBorder() {
        assertEquals(RectangleChecker.Position.INSIDE, RectangleChecker.classify(0, -3));
    }


    @Test
    @DisplayName("describe() for INSIDE contains 'INSIDE'")
    void testDescribeInside() {
        String msg = RectangleChecker.describe(RectangleChecker.Position.INSIDE, 0, 0);
        assertTrue(msg.contains("INSIDE"), "Expected 'INSIDE' in: " + msg);
    }

    @Test
    @DisplayName("describe() for OUTSIDE contains 'OUTSIDE'")
    void testDescribeOutside() {
        String msg = RectangleChecker.describe(RectangleChecker.Position.OUTSIDE, 5, 5);
        assertTrue(msg.contains("OUTSIDE"), "Expected 'OUTSIDE' in: " + msg);
    }

    @Test
    @DisplayName("describe() for CORNER contains 'CORNER'")
    void testDescribeCorner() {
        String msg = RectangleChecker.describe(RectangleChecker.Position.CORNER, -3, -4);
        assertTrue(msg.contains("CORNER"), "Expected 'CORNER' in: " + msg);
    }

    @Test
    @DisplayName("describe() for HORIZONTAL_BORDER contains 'HORIZONTAL'")
    void testDescribeHorizontalBorder() {
        String msg = RectangleChecker.describe(RectangleChecker.Position.HORIZONTAL_BORDER, 0, 4);
        assertTrue(msg.contains("HORIZONTAL"), "Expected 'HORIZONTAL' in: " + msg);
    }

    @Test
    @DisplayName("describe() for VERTICAL_BORDER contains 'VERTICAL'")
    void testDescribeVerticalBorder() {
        String msg = RectangleChecker.describe(RectangleChecker.Position.VERTICAL_BORDER, 3, 0);
        assertTrue(msg.contains("VERTICAL"), "Expected 'VERTICAL' in: " + msg);
    }

    @Test
    @DisplayName("describe() for INSIDE includes the coordinates")
    void testDescribeIncludesCoordinates() {
        String msg = RectangleChecker.describe(RectangleChecker.Position.INSIDE, 1, 2);
        assertTrue(msg.contains("1") && msg.contains("2"),
                "Expected coordinates in: " + msg);
    }


    @ParameterizedTest(name = "describe keyword for ({0},{1}) → expects '{2}'")
    @CsvSource({
            " 0,  0, INSIDE",
            " 5,  5, OUTSIDE",
            "-3, -4, CORNER",
            " 0,  4, HORIZONTAL",
            " 3,  0, VERTICAL"
    })
    @DisplayName("describe() output contains expected keyword")
    void testDescribeKeyword(int x, int y, String keyword) {
        RectangleChecker.Position pos = RectangleChecker.classify(x, y);
        String msg = RectangleChecker.describe(pos, x, y);
        assertTrue(msg.contains(keyword),
                String.format("Expected '%s' in describe output: %s", keyword, msg));
    }
}
