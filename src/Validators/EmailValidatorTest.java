package Validators;

/**
 * <p>Title: EmailValidatorTest Class</p>
 * 
 * <p>Description: Test cases for EmailValidator.</p>
 *
 * @author Manisha Chakrabarty
 * @version 1.0
 */
public class EmailValidatorTest {

    /** Count of passed tests */
    private static int passed = 0;
    
    /** Count of failed tests */
    private static int failed = 0;

    /**
     * Main method to run all tests.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=== EMAIL VALIDATOR TESTS ===\n");

        test("Empty email", "", false);
        test("No @ symbol", "notemail", false);
        test("Nothing before @", "@domain.com", false);
        test("Nothing after @", "user@", false);
        test("No dot in domain", "user@domain", false);
        test("Valid email", "user@domain.com", true);

        System.out.println("\n=== RESULTS ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }

    /**
     * Runs a single test case.
     * 
     * @param testName description of the test
     * @param input the email to test
     * @param shouldPass expected result
     */
    private static void test(String testName, String input, boolean shouldPass) {
        EmailValidator validator = new EmailValidator();
        boolean result = validator.validate(input);
        
        if (result == shouldPass) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }
}