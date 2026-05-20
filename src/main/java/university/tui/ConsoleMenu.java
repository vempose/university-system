package university.tui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

/// Utility methods for rendering menus, headers, dividers,
/// and pick-from-list helpers in the terminal.
public final class ConsoleMenu {

    private ConsoleMenu() {
    }

    /// Prints a header with the given title.
    public static void printHeader(String title) {
        String line = "#".repeat(55);
        System.out.println();
        System.out.println(line);
        System.out.println("  " + title);
        System.out.println(line);
        System.out.println();
    }

    /// Prints a section divider with a title.
    public static void printSection(String title) {
        String line = "#".repeat(55);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line);
    }

    /// Shows a full-screen menu with header, numbered options,
    /// optional Back and Exit buttons, and returns the user's choice.
    public static int showMenu(String title, LinkedHashMap<Integer, String> options, boolean showBack, boolean showExit) {
        printHeader(title);
        options.forEach((key, label) ->
                System.out.printf("  [%d]  %s%n", key, label));
        System.out.println();
        if (showBack) {
            System.out.println("  [0]  " + Messages.get("menu.back"));
        }
        if (showExit) {
            System.out.println("  [9]  " + Messages.get("menu.exit"));
        }
        System.out.println();

        int min = 0;
        int max = options.keySet().stream().max(Integer::compareTo).orElse(0);
        if (showExit) max = Math.max(max, 9);

        return ConsoleInput.readInt(Messages.get("menu.choose") + ": ", min, max);
    }

    /// Prints a success message prefixed with `[OK]`.
    public static void printSuccess(String message) {
        System.out.println("  " + Messages.get("msg.success") + " " + message);
    }

    /// Prints an error message prefixed with `[ERROR]`.
    public static void printError(String message) {
        System.out.println("  " + Messages.get("msg.error") + " " + message);
    }

    /// Prints an info message prefixed with `[INFO]`.
    public static void printInfo(String message) {
        System.out.println("  " + Messages.get("msg.info") + " " + message);
    }

    /// Asks for a yes/no confirmation and returns the result.
    public static boolean confirm(String message) {
        return ConsoleInput.readYesNo("\n  [?] " + message);
    }

    /// Prints a horizontal divider line.
    public static void printDivider() {
        System.out.println("  " + "#".repeat(55));
    }

    /// Shows a numbered list and lets the user pick one item.
    public static <T> T pickFromList(List<T> items, Function<T, String> formatter, String prompt) {
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, formatter.apply(items.get(i)));
        }
        int idx = ConsoleInput.readInt("\n  " + prompt + ": ", 1, items.size()) - 1;
        return items.get(idx);
    }

    /// Shows a numbered list with a cancel option and lets the user pick one item.
    public static <T> T pickFromList(List<T> items, Function<T, String> formatter, String prompt, String cancelLabel) {
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, formatter.apply(items.get(i)));
        }
        System.out.println("  [0]  " + cancelLabel);
        int idx = ConsoleInput.readInt("\n  " + prompt + ": ", 0, items.size());
        return idx == 0 ? null : items.get(idx - 1);
    }
}
