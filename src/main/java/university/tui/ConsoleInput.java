package university.tui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

/// Reads user input from the terminal via `Scanner(System.in)`.
///
/// Provides typed methods for ints, doubles, strings, emails,
/// passwords, dates, and yes/no prompts.
public final class ConsoleInput {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleInput() {
    }

    /// Reads an int within [min, max], retrying on invalid input.
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();
                if (value < min || value > max) {
                    System.out.println(
                            "  " + Messages.get("msg.invalid_range", min, max));
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  " + Messages.get("msg.invalid_int"));
            }
        }
    }

    /// Reads an int without bounds checking.
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  " + Messages.get("msg.invalid_int"));
            }
        }
    }

    /// Reads a double within [min, max], retrying on invalid input.
    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = SCANNER.nextDouble();
                SCANNER.nextLine();
                if (value < min || value > max) {
                    System.out.println("  " + Messages.get("msg.invalid_range", min, max));
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  " + Messages.get("msg.invalid_double"));
            }
        }
    }

    /// Reads a non-empty trimmed line.
    public static String readLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("  " + Messages.get("msg.empty"));
        }
    }

    /// Reads a line, allowing empty input.
    public static String readLineOrBlank(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    /// Reads a yes/no answer and returns the boolean.
    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " " + Messages.get("msg.yes_no") + ": ");
            String line = SCANNER.nextLine().trim().toLowerCase();
            String yes = Messages.get("msg.yes").toLowerCase();
            String no = Messages.get("msg.no").toLowerCase();
            if (line.equals(yes)) return true;
            if (line.equals(no)) return false;
            System.out.println("  " + Messages.get("msg.invalid_range", yes, no));
        }
    }

    /// Reads an email address, validating the `@` symbol.
    public static String readEmail(String prompt) {
        while (true) {
            System.out.print(prompt);
            String email = SCANNER.nextLine().trim();
            if (email.isEmpty()) {
                System.out.println("  " + Messages.get("msg.empty_email"));
                continue;
            }
            if (!email.contains("@")) {
                System.out.println("  " + Messages.get("msg.invalid_email"));
                continue;
            }
            return email;
        }
    }

    /// Reads a non-empty password string.
    public static String readPassword(String prompt) {
        while (true) {
            System.out.print(prompt);
            String password = SCANNER.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("  " + Messages.get("msg.empty_password"));
                continue;
            }
            return password;
        }
    }

    /// Reads a date in yyyy-MM-dd format, retrying on parse errors.
    public static LocalDate readDate(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String line = SCANNER.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("  " + Messages.get("msg.empty"));
                continue;
            }
            try {
                return LocalDate.parse(line, fmt);
            } catch (DateTimeParseException e) {
                System.out.println("  " + Messages.get("msg.invalid_date"));
            }
        }
    }

    /// Pauses until the user presses Enter.
    public static void waitForEnter() {
        System.out.print("\n" + Messages.get("msg.press_enter") + "...");
        SCANNER.nextLine();
    }
}
