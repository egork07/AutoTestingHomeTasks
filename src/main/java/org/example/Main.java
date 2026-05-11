package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Rectangle Position Checker ===");
        System.out.println("Rectangle I: x in [-3, 3], y in [-4, 4]");
        System.out.println();

        int x;
        int y;

        try {
            System.out.print("Enter integer x coordinate of point A: ");
            x = scanner.nextInt();

            System.out.print("Enter integer y coordinate of point A: ");
            y = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println();
            System.out.println("Error: only integer numbers are accepted (e.g. -5, 0, 3).");
            System.out.println("Characters, decimals (1.5), and empty input are not valid.");
            scanner.close();
            return;
        } finally {
            scanner.close();
        }

        RectangleChecker.Position pos = RectangleChecker.classify(x, y);
        String result = RectangleChecker.describe(pos, x, y);

        System.out.println();
        System.out.println("Result: " + result);
    }
}