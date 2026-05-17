package university.tui;

import java.util.LinkedHashMap;

public final class ConsoleMenu {

    private ConsoleMenu() {
    }

    public static void printHeader(String title) {
        String line = "═".repeat(55);
        int padding = Math.max(0, (55 - title.length() - 2) / 2);
        String paddedTitle = " ".repeat(padding) + title;
        System.out.println();
        System.out.println(line);
        System.out.printf("║ %-51s ║%n", paddedTitle);
        System.out.println(line);
        System.out.println();
    }

    public static void printSection(String title) {
        String line = "─".repeat(48);
        System.out.println("\n" + line);
        System.out.println("  " + title);
        System.out.println(line);
    }

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

    public static void printSuccess(String message) {
        System.out.println("  " + Messages.get("msg.success") + " " + message);
    }

    public static void printError(String message) {
        System.out.println("  " + Messages.get("msg.error") + " " + message);
    }

    public static void printInfo(String message) {
        System.out.println("  " + Messages.get("msg.info") + " " + message);
    }

    public static boolean confirm(String message) {
        return ConsoleInput.readYesNo(
                "\n  [?] " + message + " " + Messages.get("menu.confirm") + "?"
        );
    }

    public static void printDivider() {
        System.out.println("  " + "-".repeat(50));
    }
}
