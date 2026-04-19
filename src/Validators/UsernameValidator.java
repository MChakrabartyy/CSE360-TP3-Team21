package Validators;

/**
 * UsernameValidator - Validates username format
 * 
 * Rules:
 * - Cannot be empty
 * - Must start with a letter (a-z or A-Z)
 * - Only letters, numbers, and underscores allowed
 * - Length: 4-20 characters
 * 
 * @author Manisha
 */
public class UsernameValidator {
    
    private String username;
    private String errorMessage;
    private boolean isValid;
    
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 20;
    
    public UsernameValidator() {
        this.username = "";
        this.errorMessage = "";
        this.isValid = false;
    }
    
    public boolean validate(String username) {
        this.username = username;
        this.errorMessage = "";
        this.isValid = false;
        
        // Check null or empty
        if (username == null || username.isEmpty()) {
            this.errorMessage = "Username cannot be empty";
            return false;
        }
        
        // Check minimum length
        if (username.length() < MIN_LENGTH) {
            this.errorMessage = "Username must be at least " + MIN_LENGTH + " characters";
            return false;
        }
        
        // Check maximum length
        if (username.length() > MAX_LENGTH) {
            this.errorMessage = "Username cannot be more than " + MAX_LENGTH + " characters";
            return false;
        }
        
        // Check first character is a letter
        if (!Character.isLetter(username.charAt(0))) {
            this.errorMessage = "Username must start with a letter";
            return false;
        }
        
        // Check all characters are valid
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (!isValidCharacter(c)) {
                this.errorMessage = "Username can only contain letters, numbers, and underscores";
                return false;
            }
        }
        
        this.isValid = true;
        return true;
    }
    
    private boolean isValidCharacter(char c) {
        return Character.isLetter(c) || Character.isDigit(c) || c == '_';
    }
    
    public boolean isValid() {
        return this.isValid;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public static String checkUsername(String username) {
        UsernameValidator validator = new UsernameValidator();
        validator.validate(username);
        return validator.getErrorMessage();
    }
}