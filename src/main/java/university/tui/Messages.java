package university.tui;

import university.enums.Language;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {

    private static Language currentLanguage = Language.EN;

    private static final String BASE_NAME = "university.tui.messages";

    private Messages() {
    }

    public static void setLanguage(Language lang) {
        if (lang != null) currentLanguage = lang;
    }

    public static Language getCurrentLanguage() {
        return currentLanguage;
    }

    public static String get(String key, Object... args) {
        Locale locale = switch (currentLanguage) {
            case RU -> Locale.of("ru");
            case KZ -> Locale.of("kz");
            default -> Locale.ENGLISH;
        };
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        String pattern;
        try {
            pattern = bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
        if (args.length > 0) {
            return MessageFormat.format(pattern, args);
        }
        return pattern;
    }
}
