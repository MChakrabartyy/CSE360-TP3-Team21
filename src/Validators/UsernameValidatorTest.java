package Validators;

/**
 * Test class for UsernameValidator
 * 
 * @author Manisha
 */
public class UsernameValidatorTest {
    
    private static int passed = 0;
    private static int failed = 0;
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    USERNAME VALIDATOR TESTS");
        System.out.println("========================================\n");
        
        // Should FAIL
        test("Empty string", "", false);
        test("Too short (3 chars)", "abc", false);
        test("Too long (21 chars)", "abcdefghijklmnopqrstu", false);
        test("Starts with number", "1john", false);
        test("Starts with underscore", "_john", false);
        test("Contains space", "john doe", false);
        test("Contains @", "john@doe", false);
        test("Contains hyphen", "john-doe", false);
        
        // Should PASS
        test("Valid - minimum (4 chars)", "john", true);
        test("Valid - with underscore", "john_doe", true);
        test("Valid - with numbers", "john123", true);
        test("Valid - mixed", "John_Doe_123", true);
        test("Valid - max length (20)", "abcdefghijklmnopqrst", true);
        
        // Summary
        System.out.println("\n========================================");
        System.out.println("    SUMMARY");
        System.out.println("========================================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));
        System.out.println("========================================");
    }
    
    private static void test(String testName, String input, boolean shouldPass) {
        UsernameValidator validator = new UsernameValidator();
        boolean result = validator.validate(input);
        
        boolean testPassed = (result == shouldPass);
        
        if (testPassed) {
            System.out.println("✓ PASS: " + testName);
            passed++;
        } else {
            System.out.println("✗ FAIL: " + testName);
            System.out.println("        Input: \"" + input + "\"");
            System.out.println("        Expected: " + shouldPass + ", Got: " + result);
            System.out.println("        Error: " + validator.getErrorMessage());
            failed++;
        }
    }
}