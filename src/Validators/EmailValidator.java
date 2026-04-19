package Validators;

/**
 * <p>Title: EmailValidator Class</p>
 * 
 * <p>Description: Validates email address format using standard rules.</p>
 *
 * @author Manisha Chakrabarty
 * @version 1.0
 */
public class EmailValidator {

    /** The email being validated */
    private String email;
    
    /** The error message if validation fails */
    private String errorMessage;
    
    /** Whether the last validation was successful */
    private boolean isValid;

    /**
     * Default constructor. Initializes validator with empty values.
     */
    public EmailValidator() {
        this.email = "";
        this.errorMessage = "";
        this.isValid = false;
    }

    /**
     * Validates the given email address.
     * 
     * @param email the email address to validate
     * @return true if valid, false otherwise
     */
    public boolean validate(String email) {
        this.email = email;
        this.errorMessage = "";
        this.isValid = false;

        if (email == null || email.isEmpty()) {
            this.errorMessage = "Email cannot be empty";
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            this.errorMessage = "Email must contain @ symbol";
            return false;
        }

        if (atIndex == 0) {
            this.errorMessage = "Email must have text before @ symbol";
            return false;
        }

        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty()) {
            this.errorMessage = "Email must have domain after @ symbol";
            return false;
        }

        if (!domain.contains(".")) {
            this.errorMessage = "Email domain must contain a dot";
            return false;
        }

        this.isValid = true;
        return true;
    }

    /**
     * Returns whether the last validation was successful.
     * 
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return this.isValid;
    }

    /**
     * Returns the error message from last validation.
     * 
     * @return the error message or empty string
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Returns the last validated email.
     * 
     * @return the email string
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Static method to validate email and return error message.
     * 
     * @param email the email to validate
     * @return error message or empty string if valid
     */
    public static String checkEmail(String email) {
        EmailValidator validator = new EmailValidator();
        validator.validate(email);
        return validator.getErrorMessage();
    }
}