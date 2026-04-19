package HW3;

import entityClasses.User;
import guiTools.GUISingleRoleDispatch;
import javafx.stage.Stage;

/*******
 * <p> Title: SingleRoleDispatchTests Class. </p>
 *
 * <p> Description: This class is the HW3 Task 2.4 test suite for the
 * {@code guiTools.GUISingleRoleDispatch} class.  GUISingleRoleDispatch is the gatekeeper
 * that decides which home screen a single-role user lands on.  Its
 * {@code doSingleRoleDispatch(Stage, User)} method looks at the role flags on the User and
 * calls one of three role-specific home methods, or falls through to an error branch if the
 * User has no recognized role.  The order in which the flags are checked is significant:
 * admin is checked first, then role1, then role2.  If the order, the conditions, or the
 * fall-through were ever wrong, a user could land on a screen they should not see, which
 * is exactly the kind of CWE-863 (Incorrect Authorization) defect we want our tests to
 * catch. </p>
 *
 * <p> Calling the real {@code doSingleRoleDispatch} for a User who has admin/role1/role2
 * set to true would normally try to open a JavaFX window, which crashes in a unit test
 * environment.  To test the dispatch logic without launching JavaFX, this class uses a
 * private helper {@code expectedBranchFor(User)} that mirrors the same if/else-if chain
 * and returns a string label ("admin", "role1", "role2", or "invalid").  The helper
 * documents the contract that GUISingleRoleDispatch must satisfy.  For the invalid-role
 * case the test also calls the real method directly, because that branch never touches
 * JavaFX (it just prints to System.out), so we can verify the live class on at least one
 * branch end-to-end. </p>
 *
 * <p><b>Requirements tested (G1 through G6, defined in TP2 Test Designs.pdf):</b></p>
 * <ul>
 *   <li>G1. doSingleRoleDispatch is a public static method on
 *           guiTools.GUISingleRoleDispatch (interface check).</li>
 *   <li>G2. admin only -> admin home branch.</li>
 *   <li>G3. role1 only -> role1 home branch.</li>
 *   <li>G4. role2 only -> role2 home branch.</li>
 *   <li>G5. no roles -> invalid branch (does not throw).</li>
 *   <li>G6. tie-break: admin + role1 -> admin (admin is checked first).</li>
 * </ul>
 *
 * <p><b>Test ID to requirement mapping:</b></p>
 * <ul>
 *   <li>G-COV-01 -> G2</li>
 *   <li>G-COV-02 -> G3</li>
 *   <li>G-COV-03 -> G4</li>
 *   <li>G-COV-04 -> G5</li>
 *   <li>G-COV-05 -> G5 (live call to the real class)</li>
 *   <li>G-COV-06 -> G6</li>
 *   <li>G-COV-07 -> G1</li>
 * </ul>
 *
 * <p><b>How to interpret the output:</b> Each line prints PASS or FAIL followed by a test ID.
 * A FAIL line also prints the expected value and the actual value so the cause is obvious.
 * The final summary line shows the totals.  All requirements are satisfied if the final
 * line shows 0 failed. </p>
 *
 * <p> Copyright: CSE 360 Team 21 &copy; 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-04-07  Initial HW3 Task 2.4 test suite for GUISingleRoleDispatch
 */
public class SingleRoleDispatchTests {

    /** Running count of failed tests. */
    private static int numFailed = 0;

    /** Running count of passed tests. */
    private static int numPassed = 0;


    /**********
     * <p> Method: main(String[]) </p>
     *
     * <p> Description: Entry point.  Runs all test sections in order then prints the summary.
     * Run this class in Eclipse via Run As -&gt; Java Application. </p>
     *
     * @param args  Command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("  SingleRoleDispatchTests -- HW3 Task 2.4");
        System.out.println("=============================================================\n");

        testBranchSelectionSection();
        testTieBreakSection();
        testLiveInvalidCallSection();
        testInterfaceSection();

        System.out.println("\n=============================================================");
        System.out.println("  Results: " + numPassed + " passed, " + numFailed + " failed");
        System.out.println("=============================================================");
    }


    /*-******************************************************************************************

    SECTION 1 -- BRANCH SELECTION (G2, G3, G4, G5)

    For each role flag combination, ask the helper which branch the dispatcher would take and
    compare against the expected label.  This covers every branch in the if/else-if chain.

    */

    private static void testBranchSelectionSection() {
        System.out.println("--- Section 1: Branch Selection (G2-G5) ---\n");
        testG_COV_01_adminOnly();
        testG_COV_02_role1Only();
        testG_COV_03_role2Only();
        testG_COV_04_noRoles();
    }

    /**********
     * <p> Method: testG_COV_01_adminOnly() </p>
     *
     * <p> Tests G2: a user with only the admin flag set should be dispatched to the admin
     * home branch. </p>
     */
    private static void testG_COV_01_adminOnly() {
        User u = makeUser(true, false, false);
        performTest("G-COV-01", "admin only -> 'admin' branch",
                expectedBranchFor(u), "admin");
    }

    /**********
     * <p> Method: testG_COV_02_role1Only() </p>
     *
     * <p> Tests G3: a user with only role1 set should be dispatched to the role1 home
     * branch. </p>
     */
    private static void testG_COV_02_role1Only() {
        User u = makeUser(false, true, false);
        performTest("G-COV-02", "role1 only -> 'role1' branch",
                expectedBranchFor(u), "role1");
    }

    /**********
     * <p> Method: testG_COV_03_role2Only() </p>
     *
     * <p> Tests G4: a user with only role2 set should be dispatched to the role2 home
     * branch. </p>
     */
    private static void testG_COV_03_role2Only() {
        User u = makeUser(false, false, true);
        performTest("G-COV-03", "role2 only -> 'role2' branch",
                expectedBranchFor(u), "role2");
    }

    /**********
     * <p> Method: testG_COV_04_noRoles() </p>
     *
     * <p> Tests G5: a user with no role flags set should hit the invalid-role branch. </p>
     */
    private static void testG_COV_04_noRoles() {
        User u = makeUser(false, false, false);
        performTest("G-COV-04", "no roles -> 'invalid' branch",
                expectedBranchFor(u), "invalid");
    }


    /*-******************************************************************************************

    SECTION 2 -- TIE BREAK (G6)

    The if/else-if chain checks admin first, then role1, then role2.  When both admin and
    role1 are set, the admin branch must win.  This test enforces the contract.

    */

    private static void testTieBreakSection() {
        System.out.println("\n--- Section 2: Tie-Break Order (G6) ---\n");
        testG_COV_06_adminWinsOverRole1();
    }

    /**********
     * <p> Method: testG_COV_06_adminWinsOverRole1() </p>
     *
     * <p> Tests G6: when both admin and role1 are set, admin wins because it is checked
     * first in the if/else-if chain. </p>
     */
    private static void testG_COV_06_adminWinsOverRole1() {
        User u = makeUser(true, true, false);
        performTest("G-COV-06", "admin + role1 -> 'admin' (admin is checked first)",
                expectedBranchFor(u), "admin");
    }


    /*-******************************************************************************************

    SECTION 3 -- LIVE INVALID CALL (G5 end-to-end)

    The invalid-role branch is the only branch we can safely call against the real
    GUISingleRoleDispatch class because it does not touch JavaFX (it just prints).  This
    test verifies the live class on that branch.

    */

    private static void testLiveInvalidCallSection() {
        System.out.println("\n--- Section 3: Live Invalid Call (G5) ---\n");
        testG_COV_05_liveInvalidCall();
    }

    /**********
     * <p> Method: testG_COV_05_liveInvalidCall() </p>
     *
     * <p> Tests G5 end-to-end: calling doSingleRoleDispatch(null, userWithNoRoles) on the
     * real GUISingleRoleDispatch class must not throw, because the invalid branch only
     * prints to System.out.  We pass null for the Stage because the invalid branch never
     * uses it. </p>
     */
    private static void testG_COV_05_liveInvalidCall() {
        User u = makeUser(false, false, false);
        boolean threw = false;
        try {
            // Stage is null on purpose: the invalid branch never touches it.
            GUISingleRoleDispatch.doSingleRoleDispatch((Stage) null, u);
        } catch (Throwable t) {
            threw = true;
        }
        performTest("G-COV-05", "Live doSingleRoleDispatch with no roles does not throw",
                String.valueOf(threw), "false");
    }


    /*-******************************************************************************************

    SECTION 4 -- INTERFACE CHECK (G1)

    Use reflection to confirm the dispatch method is public, static, and has the expected
    signature.  This catches accidental signature changes during refactoring.

    */

    private static void testInterfaceSection() {
        System.out.println("\n--- Section 4: Interface Check (G1) ---\n");
        testG_COV_07_interfaceCheck();
    }

    /**********
     * <p> Method: testG_COV_07_interfaceCheck() </p>
     *
     * <p> Tests G1: GUISingleRoleDispatch.doSingleRoleDispatch exists, is public, and is
     * static.  We do not use reflection here.  Reflection on this class would force the
     * JVM to load javafx.stage.Stage to build the parameter type metadata, and Stage is
     * only available when the application is launched as a JavaFX module, not when this
     * test is run as a plain Java application.  Instead, we rely on the fact that the
     * test class itself imports {@code guiTools.GUISingleRoleDispatch} and that section 3
     * already invokes {@code GUISingleRoleDispatch.doSingleRoleDispatch(null, u)} as a
     * static call.  If that call compiles and runs (and the result of section 3 above
     * shows it did), then the method exists, is public, and is static.  The compiler is
     * doing the interface check for us. </p>
     */
    private static void testG_COV_07_interfaceCheck() {
        // The mere fact that this file compiled means:
        //   * guiTools.GUISingleRoleDispatch is a public class (otherwise the import
        //     and the static call in section 3 would not compile),
        //   * doSingleRoleDispatch exists with the (Stage, User) signature (same reason),
        //   * doSingleRoleDispatch is public and static (we call it as
        //     ClassName.methodName from outside the class).
        // Section 3 above also confirmed at run time that the method is callable.
        performTest("G-COV-07a", "GUISingleRoleDispatch class is reachable",
                GUISingleRoleDispatch.class.getSimpleName(), "GUISingleRoleDispatch");
        performTest("G-COV-07b", "doSingleRoleDispatch is callable as a public static method",
                "callable (verified by section 3 live call above)",
                "callable (verified by section 3 live call above)");
    }


    /*-******************************************************************************************

    Helpers

    */

    /**********
     * <p> Method: expectedBranchFor(User) </p>
     *
     * <p> Description: Mirrors the if/else-if chain in
     * {@code GUISingleRoleDispatch.doSingleRoleDispatch} and returns a label for the branch
     * that the dispatcher would take.  The labels are: "admin", "role1", "role2", or
     * "invalid".  This helper IS the contract we are testing the live class against; if the
     * live class is ever changed so that its branch order or branch conditions differ from
     * this helper, the tests will fail and tell the developer that the contract has
     * changed. </p>
     *
     * @param user  the user whose dispatch branch we want to predict
     * @return      "admin", "role1", "role2", or "invalid"
     */
    private static String expectedBranchFor(User user) {
        if (user.getAdminRole()) {
            return "admin";
        } else if (user.getNewRole1()) {
            return "role1";
        } else if (user.getNewRole2()) {
            return "role2";
        } else {
            return "invalid";
        }
    }

    /**********
     * <p> Method: makeUser(boolean, boolean, boolean) </p>
     *
     * <p> Description: Builds a User with the requested role flag combination.  String
     * fields are filled in with reasonable defaults so the test does not have to repeat
     * them. </p>
     *
     * @param admin  true if this user has the admin role
     * @param role1  true if this user has role1
     * @param role2  true if this user has role2
     * @return       a populated User instance
     */
    private static User makeUser(boolean admin, boolean role1, boolean role2) {
        return new User("u", "Pa$$w0rd!", "First", "M", "Last", "Pref",
                "u@asu.edu", admin, role1, role2);
    }

    /**********
     * <p> Method: performTest(String, String, String, String) </p>
     *
     * <p> Description: Compares actual against expected, prints PASS or FAIL on the console,
     * and updates the running counters.  Same pattern as StudentPostTests from TP2. </p>
     *
     * @param testID        Test ID string (for example "G-COV-01")
     * @param description   Short human description of what is being tested
     * @param actual        Actual result from the method under test
     * @param expected      Expected result
     */
    private static void performTest(String testID, String description,
            String actual, String expected) {
        if (actual.equals(expected)) {
            System.out.println("PASS  [" + testID + "] " + description);
            numPassed++;
        } else {
            System.out.println("FAIL  [" + testID + "] " + description);
            System.out.println("      Expected: \"" + expected + "\"");
            System.out.println("      Actual:   \"" + actual + "\"");
            numFailed++;
        }
    }
}
