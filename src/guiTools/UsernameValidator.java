package guiTools;

/**
 * Utility class for validating usernames.
 *
 * Rules:
 *  - Must start with a letter
 *  - Length between 4 and 20 characters
 *  - Allowed characters: letters, digits, underscore (_)
 */
public final class UsernameValidator {

    // Prevent instantiation
    private UsernameValidator() {}

    public static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }

        int length = username.length();
        if (length < 4 || length > 20) {
            return false;
        }

        // Must start with a letter
        if (!Character.isLetter(username.charAt(0))) {
            return false;
        }

        // Allowed characters check
        for (int i = 0; i < length; i++) {
            char c = username.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return false;
            }
        }

        return true;
    }
}
