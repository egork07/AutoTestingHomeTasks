package org.example;

public class RectangleChecker {

    public static final int X_MIN = -3;
    public static final int X_MAX =  3;
    public static final int Y_MIN = -4;
    public static final int Y_MAX =  4;

    public enum Position {
        INSIDE,
        OUTSIDE,
        CORNER,
        HORIZONTAL_BORDER,
        VERTICAL_BORDER
    }


    public static Position classify(int x, int y) {
        boolean onLeft   = (x == X_MIN);
        boolean onRight  = (x == X_MAX);
        boolean onBottom = (y == Y_MIN);
        boolean onTop    = (y == Y_MAX);

        boolean withinX  = (x >= X_MIN && x <= X_MAX);
        boolean withinY  = (y >= Y_MIN && y <= Y_MAX);

        if ((onLeft || onRight) && (onBottom || onTop)) {
            return Position.CORNER;
        }

        if ((onLeft || onRight) && withinY) {
            return Position.VERTICAL_BORDER;
        }

        if ((onBottom || onTop) && withinX) {
            return Position.HORIZONTAL_BORDER;
        }

        if (withinX && withinY) {
            return Position.INSIDE;
        }

        return Position.OUTSIDE;
    }

    public static String describe(Position pos, int x, int y) {
        switch (pos) {
            case INSIDE:
                return String.format(
                        "Point A(%d, %d) is INSIDE the rectangle.", x, y);
            case OUTSIDE:
                return String.format(
                        "Point A(%d, %d) is OUTSIDE the rectangle.", x, y);
            case CORNER:
                return String.format(
                        "Point A(%d, %d) is on the BORDER of the rectangle — specifically at a CORNER.", x, y);
            case HORIZONTAL_BORDER:
                return String.format(
                        "Point A(%d, %d) is on the BORDER of the rectangle — specifically on a HORIZONTAL border (y = %d).", x, y, y);
            case VERTICAL_BORDER:
                return String.format(
                        "Point A(%d, %d) is on the BORDER of the rectangle — specifically on a VERTICAL border (x = %d).", x, y, x);
            default:
                return "Unknown position.";
        }
    }
}
