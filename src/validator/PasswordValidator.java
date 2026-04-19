package validator;

/**
 * <p>Title: PasswordValidator Class</p>
 * 
 * <p>Description: Validates password using FSM approach.</p>
 *
 * @author Ali
 * @version 1.0
 */
public class PasswordValidator {

    /** State: has uppercase letter */
    private boolean hasUppercase;
    
    /** State: has lowercase letter */
    private boolean hasLowercase;
    
    /** State: has digit */
    private boolean hasDigit;
    
    /** State: has special character */
    private boolean hasSpecialChar;
    
    /** State: meets minimum length */
    private boolean hasMinLength;
    
    /** State: within maximum length */
    private boolean hasMaxLength;

    /** Minimum password length */
    private static final int MIN_LENGTH = 8;
    
    /** Maximum password length */
    private static final int MAX_LENGTH = 100;

    /**
     * Default constructor.
     */
    public PasswordValidator() {
        resetStates();
    }

    /**
     * Validates the password.
     * 
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public boolean validate(String password) {
        resetStates();

        if (password == null) {
            return false;
        }

        int length = password.length();

        if (length >= MIN_LENGTH) {
            hasMinLength = true;
        }

        if (length > MAX_LENGTH) {
            hasMaxLength = false;
            return false;
        }

        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                hasUppercase = true;
            } else if (c >= 'a' && c <= 'z') {
                hasLowercase = true;
            } else if (c >= '0' && c <= '9') {
                hasDigit = true;
            } else if ("!@#$%^&*".indexOf(c) >= 0) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && hasMinLength && hasMaxLength;
    }

    /**
     * Resets all states.
     */
    private void resetStates() {
        hasUppercase = false;
        hasLowercase = false;
        hasDigit = false;
        hasSpecialChar = false;
        hasMinLength = false;
        hasMaxLength = true;
    }

    /**
     * Returns uppercase state.
     * @return true if has uppercase
     */
    public boolean hasUppercase() {
        return hasUppercase;
    }

    /**
     * Returns lowercase state.
     * @return true if has lowercase
     */
    public boolean hasLowercase() {
        return hasLowercase;
    }

    /**
     * Returns digit state.
     * @return true if has digit
     */
    public boolean hasDigit() {
        return hasDigit;
    }

    /**
     * Returns special character state.
     * @return true if has special char
     */
    public boolean hasSpecialCharacter() {
        return hasSpecialChar;
    }

    /**
     * Returns min length state.
     * @return true if meets min length
     */
    public boolean hasMinLength() {
        return hasMinLength;
    }

    /**
     * Returns max length state.
     * @return true if within max length
     */
    public boolean hasMaxLength() {
        return hasMaxLength;
    }

    /**
     * Returns requirements status string.
     * @return formatted status
     */
    public String getRequirementsStatus() {
        return "Uppercase: " + (hasUppercase ? "Y" : "N") + "\n"
             + "Lowercase: " + (hasLowercase ? "Y" : "N") + "\n"
             + "Digit: " + (hasDigit ? "Y" : "N") + "\n"
             + "Special: " + (hasSpecialChar ? "Y" : "N") + "\n"
             + "Min Length: " + (hasMinLength ? "Y" : "N") + "\n"
             + "Max Length: " + (hasMaxLength ? "Y" : "N");
    }
}