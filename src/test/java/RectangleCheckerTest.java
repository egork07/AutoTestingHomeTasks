
import org.example.RectangleChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RectangleChecker — full domain coverage")
class RectangleCheckerTest {

    @Nested
    @DisplayName("Invalid input — non-integer types")
    class InvalidInputTests {

        @Test
        @DisplayName("Decimal number '1.5' throws InputMismatchException")
        void decimalThrows() {
            assertThrows(InputMismatchException.class,
                    () -> new Scanner("1.5").nextInt());
        }

        @Test
        @DisplayName("Alphabetic string 'abc' throws InputMismatchException")
        void alphabeticThrows() {
            assertThrows(InputMismatchException.class,
                    () -> new Scanner("abc").nextInt());
        }

        @Test
        @DisplayName("Single char 'x' throws InputMismatchException")
        void singleCharThrows() {
            assertThrows(InputMismatchException.class,
                    () -> new Scanner("x").nextInt());
        }

        @Test
        @DisplayName("Special chars '@#!' throw InputMismatchException")
        void specialCharsThrow() {
            assertThrows(InputMismatchException.class,
                    () -> new Scanner("@#!").nextInt());
        }

        @Test
        @DisplayName("Empty string throws NoSuchElementException")
        void emptyStringThrows() {
            assertThrows(NoSuchElementException.class,
                    () -> new Scanner("").nextInt());
        }

        @Test
        @DisplayName("Whitespace-only string throws NoSuchElementException")
        void whitespaceThrows() {
            assertThrows(NoSuchElementException.class,
                    () -> new Scanner("   ").nextInt());
        }
    }

    @Nested
    @DisplayName("Position: OUTSIDE")
    class OutsideTests {


        @ParameterizedTest(name = "OUTSIDE: A({0}, {1}) — {2}")
        @CsvSource({
                "-99,           0, far outside left",
                " 99,           0, far outside right",
                "  0,         -99, far outside below",
                "  0,          99, far outside above",
                " -4,           0, just outside left border (X_MIN-1)",
                "  4,           0, just outside right border (X_MAX+1)",
                "  0,          -5, just outside bottom border (Y_MIN-1)",
                "  0,           5, just outside top border (Y_MAX+1)",
                "-2147483648,   0, Integer.MIN_VALUE on X",
                " 2147483647,   0, Integer.MAX_VALUE on X",
                "  0, -2147483648, Integer.MIN_VALUE on Y",
                "  0,  2147483647, Integer.MAX_VALUE on Y",
                " -5,          -6, both axes outside (negative quadrant)",
                "  5,           6, both axes outside (positive quadrant)"
        })
        void testOutside(int x, int y, String label) {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(x, y),
                    "Expected OUTSIDE for " + label + " A(" + x + "," + y + ")");
        }
    }

    @Nested
    @DisplayName("Position: INSIDE")
    class InsideTests {


        @ParameterizedTest(name = "INSIDE: A({0}, {1}) — {2}")
        @CsvSource({
                " 0,  0, center of rectangle",
                "-2,  0, just inside left border (X_MIN+1)",
                " 2,  0, just inside right border (X_MAX-1)",
                " 0, -3, just inside bottom border (Y_MIN+1)",
                " 0,  3, just inside top border (Y_MAX-1)",
                " 1,  2, general interior positive quadrant",
                "-1, -2, general interior negative quadrant",
                " 1, -1, general interior mixed quadrant"
        })
        void testInside(int x, int y, String label) {
            assertEquals(RectangleChecker.Position.INSIDE,
                    RectangleChecker.classify(x, y),
                    "Expected INSIDE for " + label + " A(" + x + "," + y + ")");
        }
    }

    @Nested
    @DisplayName("Position: CORNER")
    class CornerTests {

        @ParameterizedTest(name = "CORNER: A({0}, {1}) — {2}")
        @CsvSource({
                "-3, -4, bottom-left corner  (X_MIN, Y_MIN)",
                " 3, -4, bottom-right corner (X_MAX, Y_MIN)",
                "-3,  4, top-left corner     (X_MIN, Y_MAX)",
                " 3,  4, top-right corner    (X_MAX, Y_MAX)"
        })
        void testCorner(int x, int y, String label) {
            assertEquals(RectangleChecker.Position.CORNER,
                    RectangleChecker.classify(x, y),
                    "Expected CORNER for " + label);
        }
    }


    @Nested
    @DisplayName("Position: HORIZONTAL_BORDER")
    class HorizontalBorderTests {

        @ParameterizedTest(name = "HORIZONTAL_BORDER: A({0}, {1}) — {2}")
        @CsvSource({
                " 0, -4, bottom border center (x=0, y=Y_MIN)",
                "-2, -4, bottom border left side (x=X_MIN+1, y=Y_MIN)",
                " 2, -4, bottom border right side (x=X_MAX-1, y=Y_MIN)",
                " 0,  4, top border center (x=0, y=Y_MAX)",
                "-1,  4, top border left side (x=-1, y=Y_MAX)",
                " 1,  4, top border right side (x=1, y=Y_MAX)"
        })
        void testHorizontalBorder(int x, int y, String label) {
            assertEquals(RectangleChecker.Position.HORIZONTAL_BORDER,
                    RectangleChecker.classify(x, y),
                    "Expected HORIZONTAL_BORDER for " + label);
        }
    }

    @Nested
    @DisplayName("Position: VERTICAL_BORDER")
    class VerticalBorderTests {

        @ParameterizedTest(name = "VERTICAL_BORDER: A({0}, {1}) — {2}")
        @CsvSource({
                "-3,  0, left border center (x=X_MIN, y=0)",
                "-3,  2, left border upper (x=X_MIN, y=Y_MAX-2)",
                "-3, -2, left border lower (x=X_MIN, y=Y_MIN+2)",
                " 3,  0, right border center (x=X_MAX, y=0)",
                " 3,  1, right border upper (x=X_MAX, y=1)",
                " 3, -1, right border lower (x=X_MAX, y=-1)"
        })
        void testVerticalBorder(int x, int y, String label) {
            assertEquals(RectangleChecker.Position.VERTICAL_BORDER,
                    RectangleChecker.classify(x, y),
                    "Expected VERTICAL_BORDER for " + label);
        }
    }

    @Nested
    @DisplayName("Boundary-value analysis (BVA) — X axis")
    class BoundaryValueXTests {

        @Test @DisplayName("x = X_MIN-1 = -4  → OUTSIDE")
        void xJustBelowMin()  { assertEquals(RectangleChecker.Position.OUTSIDE,          RectangleChecker.classify(-4, 0)); }

        @Test @DisplayName("x = X_MIN   = -3  → VERTICAL_BORDER")
        void xAtMin()         { assertEquals(RectangleChecker.Position.VERTICAL_BORDER,   RectangleChecker.classify(-3, 0)); }

        @Test @DisplayName("x = X_MIN+1 = -2  → INSIDE")
        void xJustAboveMin()  { assertEquals(RectangleChecker.Position.INSIDE,            RectangleChecker.classify(-2, 0)); }

        @Test @DisplayName("x = X_MAX-1 =  2  → INSIDE")
        void xJustBelowMax()  { assertEquals(RectangleChecker.Position.INSIDE,            RectangleChecker.classify(2, 0)); }

        @Test @DisplayName("x = X_MAX   =  3  → VERTICAL_BORDER")
        void xAtMax()         { assertEquals(RectangleChecker.Position.VERTICAL_BORDER,   RectangleChecker.classify(3, 0)); }

        @Test @DisplayName("x = X_MAX+1 =  4  → OUTSIDE")
        void xJustAboveMax()  { assertEquals(RectangleChecker.Position.OUTSIDE,           RectangleChecker.classify(4, 0)); }
    }

    @Nested
    @DisplayName("Boundary-value analysis (BVA) — Y axis")
    class BoundaryValueYTests {

        @Test @DisplayName("y = Y_MIN-1 = -5  → OUTSIDE")
        void yJustBelowMin()  { assertEquals(RectangleChecker.Position.OUTSIDE,           RectangleChecker.classify(0, -5)); }

        @Test @DisplayName("y = Y_MIN   = -4  → HORIZONTAL_BORDER")
        void yAtMin()         { assertEquals(RectangleChecker.Position.HORIZONTAL_BORDER, RectangleChecker.classify(0, -4)); }

        @Test @DisplayName("y = Y_MIN+1 = -3  → INSIDE")
        void yJustAboveMin()  { assertEquals(RectangleChecker.Position.INSIDE,            RectangleChecker.classify(0, -3)); }

        @Test @DisplayName("y = Y_MAX-1 =  3  → INSIDE")
        void yJustBelowMax()  { assertEquals(RectangleChecker.Position.INSIDE,            RectangleChecker.classify(0, 3)); }

        @Test @DisplayName("y = Y_MAX   =  4  → HORIZONTAL_BORDER")
        void yAtMax()         { assertEquals(RectangleChecker.Position.HORIZONTAL_BORDER, RectangleChecker.classify(0, 4)); }

        @Test @DisplayName("y = Y_MAX+1 =  5  → OUTSIDE")
        void yJustAboveMax()  { assertEquals(RectangleChecker.Position.OUTSIDE,           RectangleChecker.classify(0, 5)); }
    }


    @Nested
    @DisplayName("Extreme integer values — type boundary")
    class ExtremeIntTests {

        @Test @DisplayName("Integer.MAX_VALUE on X → OUTSIDE")
        void maxIntX() {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(Integer.MAX_VALUE, 0));
        }

        @Test @DisplayName("Integer.MIN_VALUE on X → OUTSIDE")
        void minIntX() {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(Integer.MIN_VALUE, 0));
        }

        @Test @DisplayName("Integer.MAX_VALUE on Y → OUTSIDE")
        void maxIntY() {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(0, Integer.MAX_VALUE));
        }

        @Test @DisplayName("Integer.MIN_VALUE on Y → OUTSIDE")
        void minIntY() {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(0, Integer.MIN_VALUE));
        }

        @Test @DisplayName("Both axes Integer.MAX_VALUE → OUTSIDE")
        void maxIntBoth() {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(Integer.MAX_VALUE, Integer.MAX_VALUE));
        }

        @Test @DisplayName("Both axes Integer.MIN_VALUE → OUTSIDE")
        void minIntBoth() {
            assertEquals(RectangleChecker.Position.OUTSIDE,
                    RectangleChecker.classify(Integer.MIN_VALUE, Integer.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("describe() — output keyword verification")
    class DescribeTests {

        @ParameterizedTest(name = "describe for ({0},{1}) contains keyword '{2}'")
        @CsvSource({
                " 0,  0, INSIDE",
                " 5,  5, OUTSIDE",
                "-3, -4, CORNER",
                " 0,  4, HORIZONTAL",
                " 3,  0, VERTICAL"
        })
        void describeContainsKeyword(int x, int y, String keyword) {
            RectangleChecker.Position pos = RectangleChecker.classify(x, y);
            String msg = RectangleChecker.describe(pos, x, y);
            assertTrue(msg.contains(keyword),
                    "Expected keyword '" + keyword + "' in: " + msg);
        }

        @Test @DisplayName("describe() includes the x coordinate in output")
        void describeContainsX() {
            String msg = RectangleChecker.describe(RectangleChecker.Position.INSIDE, 2, 1);
            assertTrue(msg.contains("2"), "x=2 should appear in: " + msg);
        }

        @Test @DisplayName("describe() includes the y coordinate in output")
        void describeContainsY() {
            String msg = RectangleChecker.describe(RectangleChecker.Position.INSIDE, 1, 3);
            assertTrue(msg.contains("3"), "y=3 should appear in: " + msg);
        }

        @Test @DisplayName("CORNER describe() mentions both x and y border values")
        void describeCornerMentionsBothBorders() {
            String msg = RectangleChecker.describe(RectangleChecker.Position.CORNER, -3, -4);
            assertTrue(msg.contains("CORNER"), "Should mention CORNER: " + msg);
            assertTrue(msg.contains("-3"),     "Should mention x=-3: " + msg);
            assertTrue(msg.contains("-4"),     "Should mention y=-4: " + msg);
        }

        @Test @DisplayName("HORIZONTAL_BORDER describe() mentions the y value")
        void describeHorizontalMentionsY() {
            String msg = RectangleChecker.describe(RectangleChecker.Position.HORIZONTAL_BORDER, 1, 4);
            assertTrue(msg.contains("HORIZONTAL"), "Should mention HORIZONTAL: " + msg);
            assertTrue(msg.contains("4"),          "Should mention y=4: " + msg);
        }

        @Test @DisplayName("VERTICAL_BORDER describe() mentions the x value")
        void describeVerticalMentionsX() {
            String msg = RectangleChecker.describe(RectangleChecker.Position.VERTICAL_BORDER, 3, 1);
            assertTrue(msg.contains("VERTICAL"), "Should mention VERTICAL: " + msg);
            assertTrue(msg.contains("3"),        "Should mention x=3: " + msg);
        }
    }
}