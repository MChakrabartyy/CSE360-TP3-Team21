package validator;

/**
 * Manual test harness for PasswordValidator.
 * This class verifies all password validation requirements.
 * 
 * @author Ali
 */
public class PasswordValidatorTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("    PASSWORD VALIDATOR TESTS");
        System.out.println("========================================\n");

        PasswordValidator validator = new PasswordValidator();

        test("Empty password", "", false, validator);
        test("Too short", "Aa1!", false, validator);
        test("Too long", "Aa1!" + "a".repeat(100), false, validator);
        test("Missing uppercase", "aa1!aaaa", false, validator);
        test("Missing lowercase", "AA1!AAAA", false, validator);
        test("Missing digit", "Aa!aaaaa", false, validator);
        test("Missing special character", "Aa1aaaaa", false, validator);
        test("Valid password", "Aa1!aaaa", true, validator);

        System.out.println("\n========================================");
        System.out.println("    SUMMARY");
        System.out.println("========================================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total:  " + (passed + failed));
        System.out.println("========================================");
    }

    private static void test(String description, String password, boolean expected,
                             PasswordValidator validator) {

        boolean result = validator.validate(password);

        if (result == expected) {
            System.out.println("✓ PASS: " + description);
            passed++;
        } else {
            System.out.println("✗ FAIL: " + description);
            System.out.println("  Password: \"" + password + "\"");
            System.out.println(validator.getRequirementsStatus());
            failed++;
        }
    }
}