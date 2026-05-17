package university.tui;

import java.util.LinkedHashMap;

public final class ConsoleMenu {

    private ConsoleMenu() {
    }

    public static void printHeader(String title) {
        String line = "═".repeat(60);
        int padding = (60 - title.length() - 2) / 2;
        String paddedTitle = " ".repeat(Math.max(0, padding)) + title;
        System.out.println();
        System.out.println(line);
        System.out.printf("║ %-56s ║%n", paddedTitle);
        System.out.println(line);
        System.out.println();
    }

    public static void printSection(String title) {
        String line = "─".repeat(50);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line);
    }

    public static int showMenu(String title, LinkedHashMap<Integer, String> options, boolean showBack, boolean showExit) {
        printHeader(title);
        options.forEach((key, label) ->
                System.out.printf("  [%d]  %s%n", key, label));
        if (showBack && showExit) {
            System.out.println();
            System.out.println("  [0]  Go Back");
            System.out.println("  [9]  Logout & Exit");
        } else if (showBack) {
            System.out.println();
            System.out.println("  [0]  Go Back");
        } else if (showExit) {
            System.out.println();
            System.out.println("  [9]  Logout & Exit");
        }
        System.out.println();

        int min = 0;
        int max = options.keySet().stream().max(Integer::compareTo).orElse(0);
        if (showExit) max = Math.max(max, 9);

        return ConsoleInput.readInt("Choose an option: ", min, max);
    }

    public static void printSuccess(String message) {
        System.out.println("  [✓] " + message);
    }

    public static void printError(String message) {
        System.out.println("  [✗] " + message);
    }

    public static void printInfo(String message) {
        System.out.println("  [i] " + message);
    }

    public static boolean confirm(String message) {
        return ConsoleInput.readYesNo("\n  [?] " + message + " Are you sure?");
    }

    public static void printTable(String... columns) {
        System.out.print("  ");
        for (int i = 0; i < columns.length; i++) {
            System.out.printf("%-" + (i < columns.length - 1 ? 25 : 5) + "s", columns[i]);
        }
        System.out.println();
    }

    public static void printDivider() {
        System.out.println("  " + "-".repeat(55));
    }
}
