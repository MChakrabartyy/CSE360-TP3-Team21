package guiNewAccount;

import validator.PasswordValidator;

/**
 * <p>Title: ModelNewAccount Class</p>
 * 
 * <p>Description: Model for new account password validation.</p>
 *
 * @author Ali
 * @version 1.0
 */
public class ModelNewAccount {
    
    /** Password validator instance */
    private static PasswordValidator passwordValidator = new PasswordValidator();

    /**
     * Default constructor.
     */
    public ModelNewAccount() {
    }

    /**
     * Validates password and returns error message.
     * 
     * @param password the password to validate
     * @return empty string if valid, error message otherwise
     */
    public static String validatePassword(String password) {
        boolean isValid = passwordValidator.validate(password);
        if (isValid) {
            return "";
        }
        return passwordValidator.getRequirementsStatus();
    }
    
    /**
     * Checks if password has uppercase.
     * @return true if has uppercase
     */
    public static boolean hasUppercase() {
        return passwordValidator.hasUppercase();
    }

    /**
     * Checks if password has lowercase.
     * @return true if has lowercase
     */
    public static boolean hasLowercase() {
        return passwordValidator.hasLowercase();
    }

    /**
     * Checks if password has digit.
     * @return true if has digit
     */
    public static boolean hasDigit() {
        return passwordValidator.hasDigit();
    }

    /**
     * Checks if password has special char.
     * @return true if has special char
     */
    public static boolean hasSpecialChar() {
        return passwordValidator.hasSpecialCharacter();
    }

    /**
     * Checks if password meets min length.
     * @return true if meets min length
     */
    public static boolean hasMinLength() {
        return passwordValidator.hasMinLength();
    }

    /**
     * Checks if password within max length.
     * @return true if within max length
     */
    public static boolean hasMaxLength() {
        return passwordValidator.hasMaxLength();
    }
}