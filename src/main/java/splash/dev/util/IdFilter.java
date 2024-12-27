package splash.dev.util;

public class IdFilter {

    public static String getContentAfter(String input) {
        String[] parts = input.split("::");
        if (parts.length > 1) {
            return parts[1].trim();
        }
        return "";
    }

    public static String getContentBefore(String input) {
        String[] parts = input.split("::");
        if (parts.length > 0) {
            return parts[0].trim();
        }
        return "";
    }

}
