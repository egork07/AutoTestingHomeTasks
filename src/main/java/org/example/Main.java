package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Rectangle Position Checker ===");
        System.out.println("Rectangle I: x in [-3, 3], y in [-4, 4]");
        System.out.println();

        System.out.print("Enter integer x coordinate of point A: ");
        int x = scanner.nextInt();

        System.out.print("Enter integer y coordinate of point A: ");
        int y = scanner.nextInt();

        RectangleChecker.Position pos = RectangleChecker.classify(x, y);
        String result = RectangleChecker.describe(pos, x, y);

        System.out.println();
        System.out.println("Result: " + result);

        scanner.close();
    }
}