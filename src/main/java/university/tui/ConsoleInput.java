package university.tui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public final class ConsoleInput {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleInput() {
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();
                if (value < min || value > max) {
                    System.out.printf(
                            "  [!] Please enter a number between %d and %d.%n",
                            min,
                            max
                    );
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  [!] Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  [!] Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = SCANNER.nextDouble();
                SCANNER.nextLine();
                if (value < min || value > max) {
                    System.out.printf(
                            "  [!] Please enter a number between %.1f and %.1f.%n",
                            min,
                            max
                    );
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  [!] Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("  [!] Input cannot be empty. Please try again.");
        }
    }

    public static String readLineOrBlank(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String line = SCANNER.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("  [!] Please enter 'y' or 'n'.");
        }
    }

    public static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = SCANNER.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("  [!] Email cannot be empty.");
                continue;
            }
            if (!email.contains("@")) {
                System.out.println("  [!] Invalid email format. Please include '@'.");
                continue;
            }
            return email;
        }
    }

    public static String readPassword(String prompt) {
        while (true) {
            System.out.print(prompt);
            String password = SCANNER.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("  [!] Password cannot be empty.");
                continue;
            }
            return password;
        }
    }

    public static LocalDate readDate(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String line = SCANNER.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("  [!] Date cannot be empty.");
                continue;
            }
            try {
                return LocalDate.parse(line, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("  [!] Invalid date format. Use yyyy-MM-dd.");
            }
        }
    }

    public static void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        SCANNER.nextLine();
    }
}
