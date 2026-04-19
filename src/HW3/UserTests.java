package HW3;

import entityClasses.User;

/*******
 * <p> Title: UserTests Class. </p>
 *
 * <p> Description: This class is the HW3 Task 2.4 test suite for the {@code entityClasses.User}
 * class.  User was written by Lynn Robert Carter and was selected for HW3 Task 2.3 because it
 * is the data model for every authenticated user in the system: its three role flags (admin,
 * role1, role2) decide what every other screen lets the user do, and its getNumRoles method is
 * used by Database and by the login flow to decide whether to dispatch a user to a single-role
 * home page or to the multiple-role dispatch page.  A bug in any of these methods quietly
 * changes the system's authorization decisions. </p>
 *
 * <p> The structure follows {@code StudentPostTests} from TP2: a single {@code main} method
 * drives the test sections, each test calls {@code performTest} which prints PASS/FAIL and
 * updates running counters, and a final summary line shows the totals.  No external test
 * framework is used. </p>
 *
 * <p><b>Requirements tested (U1 through U12, defined in TP2 Test Designs.pdf):</b></p>
 * <ul>
 *   <li>U1.  The full constructor stores all 10 fields in the matching instance variables.</li>
 *   <li>U2.  The default constructor produces null strings and false role flags.</li>
 *   <li>U3.  getNumRoles returns 0 when no role flags are set.</li>
 *   <li>U4.  getNumRoles returns 1 when exactly one role flag is set (test all three).</li>
 *   <li>U5.  getNumRoles returns 2 when exactly two role flags are set (test all three pairs).</li>
 *   <li>U6.  getNumRoles returns 3 when all three role flags are set.</li>
 *   <li>U7.  setAdminRole and getAdminRole are consistent.</li>
 *   <li>U8.  setRole1User and getNewRole1 are consistent.</li>
 *   <li>U9.  setRole2User and getNewRole2 are consistent.</li>
 *   <li>U10. After construction every getter returns the constructor argument.</li>
 *   <li>U11. Each string setter updates the matching field.</li>
 *   <li>U12. User accepts and stores empty strings and null without throwing.</li>
 * </ul>
 *
 * <p><b>Test ID to requirement mapping:</b></p>
 * <ul>
 *   <li>U-BV-01..08 -> U3, U4, U5, U6 (getNumRoles boundary values)</li>
 *   <li>U-COV-01    -> U1, U10</li>
 *   <li>U-COV-02    -> U2</li>
 *   <li>U-COV-03    -> U7</li>
 *   <li>U-COV-04    -> U8</li>
 *   <li>U-COV-05    -> U9</li>
 *   <li>U-COV-06    -> U11</li>
 *   <li>U-COV-07    -> U12</li>
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
 * @version 1.00    2026-04-07  Initial HW3 Task 2.4 test suite for User
 */
public class UserTests {

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
        System.out.println("  UserTests -- HW3 Task 2.4");
        System.out.println("=============================================================\n");

        testGetNumRolesBoundarySection();
        testConstructorSection();
        testRoleSetterSection();
        testStringSetterSection();
        testNullAndEmptySection();

        System.out.println("\n=============================================================");
        System.out.println("  Results: " + numPassed + " passed, " + numFailed + " failed");
        System.out.println("=============================================================");
    }


    /*-******************************************************************************************

    SECTION 1 -- BOUNDARY VALUE TESTS FOR getNumRoles (U3, U4, U5, U6)

    getNumRoles can return 0, 1, 2, or 3.  We test all four return values, and for the 1 and
    2 cases we test every subset of flags so we are sure the count does not depend on which
    particular flag is set.

    */

    private static void testGetNumRolesBoundarySection() {
        System.out.println("--- Section 1: getNumRoles Boundary Values (U3-U6) ---\n");
        testU_BV_01_noRoles();
        testU_BV_02_adminOnly();
        testU_BV_03_role1Only();
        testU_BV_04_role2Only();
        testU_BV_05_adminAndRole1();
        testU_BV_06_adminAndRole2();
        testU_BV_07_role1AndRole2();
        testU_BV_08_allThreeRoles();
    }

    /**********
     * <p> Method: testU_BV_01_noRoles() </p>
     *
     * <p> Tests U3: a user with no role flags set returns 0 from getNumRoles. </p>
     */
    private static void testU_BV_01_noRoles() {
        User u = makeUser(false, false, false);
        performTest("U-BV-01", "getNumRoles for no roles is 0",
                String.valueOf(u.getNumRoles()), "0");
    }

    /**********
     * <p> Method: testU_BV_02_adminOnly() </p>
     *
     * <p> Tests U4: a user with only the admin flag set returns 1. </p>
     */
    private static void testU_BV_02_adminOnly() {
        User u = makeUser(true, false, false);
        performTest("U-BV-02", "getNumRoles for admin only is 1",
                String.valueOf(u.getNumRoles()), "1");
    }

    /**********
     * <p> Method: testU_BV_03_role1Only() </p>
     *
     * <p> Tests U4: a user with only role1 set returns 1. </p>
     */
    private static void testU_BV_03_role1Only() {
        User u = makeUser(false, true, false);
        performTest("U-BV-03", "getNumRoles for role1 only is 1",
                String.valueOf(u.getNumRoles()), "1");
    }

    /**********
     * <p> Method: testU_BV_04_role2Only() </p>
     *
     * <p> Tests U4: a user with only role2 set returns 1. </p>
     */
    private static void testU_BV_04_role2Only() {
        User u = makeUser(false, false, true);
        performTest("U-BV-04", "getNumRoles for role2 only is 1",
                String.valueOf(u.getNumRoles()), "1");
    }

    /**********
     * <p> Method: testU_BV_05_adminAndRole1() </p>
     *
     * <p> Tests U5: a user with admin and role1 returns 2. </p>
     */
    private static void testU_BV_05_adminAndRole1() {
        User u = makeUser(true, true, false);
        performTest("U-BV-05", "getNumRoles for admin + role1 is 2",
                String.valueOf(u.getNumRoles()), "2");
    }

    /**********
     * <p> Method: testU_BV_06_adminAndRole2() </p>
     *
     * <p> Tests U5: a user with admin and role2 returns 2. </p>
     */
    private static void testU_BV_06_adminAndRole2() {
        User u = makeUser(true, false, true);
        performTest("U-BV-06", "getNumRoles for admin + role2 is 2",
                String.valueOf(u.getNumRoles()), "2");
    }

    /**********
     * <p> Method: testU_BV_07_role1AndRole2() </p>
     *
     * <p> Tests U5: a user with role1 and role2 returns 2. </p>
     */
    private static void testU_BV_07_role1AndRole2() {
        User u = makeUser(false, true, true);
        performTest("U-BV-07", "getNumRoles for role1 + role2 is 2",
                String.valueOf(u.getNumRoles()), "2");
    }

    /**********
     * <p> Method: testU_BV_08_allThreeRoles() </p>
     *
     * <p> Tests U6: a user with all three role flags set returns 3. </p>
     */
    private static void testU_BV_08_allThreeRoles() {
        User u = makeUser(true, true, true);
        performTest("U-BV-08", "getNumRoles for all three roles is 3",
                String.valueOf(u.getNumRoles()), "3");
    }


    /*-******************************************************************************************

    SECTION 2 -- CONSTRUCTOR COVERAGE (U1, U2, U10)

    */

    private static void testConstructorSection() {
        System.out.println("\n--- Section 2: Constructors (U1, U2, U10) ---\n");
        testU_COV_01_fullConstructor();
        testU_COV_02_defaultConstructor();
    }

    /**********
     * <p> Method: testU_COV_01_fullConstructor() </p>
     *
     * <p> Tests U1 and U10: the full constructor stores every argument and every getter
     * returns the matching value. </p>
     */
    private static void testU_COV_01_fullConstructor() {
        User u = new User("alice", "Pa$$w0rd!", "Alice", "M", "Smith", "Ali",
                "alice@asu.edu", true, true, false);
        performTest("U-COV-01a", "userName stored",       u.getUserName(), "alice");
        performTest("U-COV-01b", "password stored",       u.getPassword(), "Pa$$w0rd!");
        performTest("U-COV-01c", "firstName stored",      u.getFirstName(), "Alice");
        performTest("U-COV-01d", "middleName stored",     u.getMiddleName(), "M");
        performTest("U-COV-01e", "lastName stored",       u.getLastName(), "Smith");
        performTest("U-COV-01f", "preferredFirstName stored",
                u.getPreferredFirstName(), "Ali");
        performTest("U-COV-01g", "emailAddress stored",   u.getEmailAddress(), "alice@asu.edu");
        performTest("U-COV-01h", "adminRole stored",
                String.valueOf(u.getAdminRole()), "true");
        performTest("U-COV-01i", "role1 stored",
                String.valueOf(u.getNewRole1()), "true");
        performTest("U-COV-01j", "role2 stored",
                String.valueOf(u.getNewRole2()), "false");
    }

    /**********
     * <p> Method: testU_COV_02_defaultConstructor() </p>
     *
     * <p> Tests U2: after the default constructor, all string fields are null and all role
     * flags are false. </p>
     */
    private static void testU_COV_02_defaultConstructor() {
        User u = new User();
        performTest("U-COV-02a", "default userName is null",
                u.getUserName() == null ? "null" : u.getUserName(), "null");
        performTest("U-COV-02b", "default password is null",
                u.getPassword() == null ? "null" : u.getPassword(), "null");
        performTest("U-COV-02c", "default emailAddress is null",
                u.getEmailAddress() == null ? "null" : u.getEmailAddress(), "null");
        performTest("U-COV-02d", "default adminRole is false",
                String.valueOf(u.getAdminRole()), "false");
        performTest("U-COV-02e", "default role1 is false",
                String.valueOf(u.getNewRole1()), "false");
        performTest("U-COV-02f", "default role2 is false",
                String.valueOf(u.getNewRole2()), "false");
    }


    /*-******************************************************************************************

    SECTION 3 -- ROLE SETTER COVERAGE (U7, U8, U9)

    Each role setter is exercised with both true and false to make sure the toggle works.

    */

    private static void testRoleSetterSection() {
        System.out.println("\n--- Section 3: Role Setters (U7, U8, U9) ---\n");
        testU_COV_03_setAdminRole();
        testU_COV_04_setRole1User();
        testU_COV_05_setRole2User();
    }

    /**********
     * <p> Method: testU_COV_03_setAdminRole() </p>
     *
     * <p> Tests U7: setAdminRole(true) makes getAdminRole return true; setAdminRole(false)
     * makes it return false. </p>
     */
    private static void testU_COV_03_setAdminRole() {
        User u = new User();
        u.setAdminRole(true);
        performTest("U-COV-03a", "setAdminRole(true) -> getAdminRole true",
                String.valueOf(u.getAdminRole()), "true");
        u.setAdminRole(false);
        performTest("U-COV-03b", "setAdminRole(false) -> getAdminRole false",
                String.valueOf(u.getAdminRole()), "false");
    }

    /**********
     * <p> Method: testU_COV_04_setRole1User() </p>
     *
     * <p> Tests U8: setRole1User(true/false) is reflected in getNewRole1. </p>
     */
    private static void testU_COV_04_setRole1User() {
        User u = new User();
        u.setRole1User(true);
        performTest("U-COV-04a", "setRole1User(true) -> getNewRole1 true",
                String.valueOf(u.getNewRole1()), "true");
        u.setRole1User(false);
        performTest("U-COV-04b", "setRole1User(false) -> getNewRole1 false",
                String.valueOf(u.getNewRole1()), "false");
    }

    /**********
     * <p> Method: testU_COV_05_setRole2User() </p>
     *
     * <p> Tests U9: setRole2User(true/false) is reflected in getNewRole2. </p>
     */
    private static void testU_COV_05_setRole2User() {
        User u = new User();
        u.setRole2User(true);
        performTest("U-COV-05a", "setRole2User(true) -> getNewRole2 true",
                String.valueOf(u.getNewRole2()), "true");
        u.setRole2User(false);
        performTest("U-COV-05b", "setRole2User(false) -> getNewRole2 false",
                String.valueOf(u.getNewRole2()), "false");
    }


    /*-******************************************************************************************

    SECTION 4 -- STRING SETTER COVERAGE (U11)

    Each string setter must be reflected in the matching getter.

    */

    private static void testStringSetterSection() {
        System.out.println("\n--- Section 4: String Setters (U11) ---\n");
        testU_COV_06_allStringSetters();
    }

    /**********
     * <p> Method: testU_COV_06_allStringSetters() </p>
     *
     * <p> Tests U11: every string setter (setUserName, setPassword, setFirstName,
     * setMiddleName, setLastName, setPreferredFirstName, setEmailAddress) updates the
     * matching field and the matching getter returns the new value. </p>
     */
    private static void testU_COV_06_allStringSetters() {
        User u = new User();
        u.setUserName("bob");
        performTest("U-COV-06a", "setUserName / getUserName", u.getUserName(), "bob");
        u.setPassword("Secret1!");
        performTest("U-COV-06b", "setPassword / getPassword", u.getPassword(), "Secret1!");
        u.setFirstName("Bob");
        performTest("U-COV-06c", "setFirstName / getFirstName", u.getFirstName(), "Bob");
        u.setMiddleName("J");
        performTest("U-COV-06d", "setMiddleName / getMiddleName", u.getMiddleName(), "J");
        u.setLastName("Jones");
        performTest("U-COV-06e", "setLastName / getLastName", u.getLastName(), "Jones");
        u.setPreferredFirstName("Bobby");
        performTest("U-COV-06f", "setPreferredFirstName / getPreferredFirstName",
                u.getPreferredFirstName(), "Bobby");
        u.setEmailAddress("bob@asu.edu");
        performTest("U-COV-06g", "setEmailAddress / getEmailAddress",
                u.getEmailAddress(), "bob@asu.edu");
    }


    /*-******************************************************************************************

    SECTION 5 -- NULL AND EMPTY ACCEPTANCE (U12)

    User does not validate strings; it stores whatever it is given.  This documents the
    current behavior so callers know they must validate before constructing.  This is
    a CWE-20 (Improper Input Validation) finding for callers, not for User itself.

    */

    private static void testNullAndEmptySection() {
        System.out.println("\n--- Section 5: Null and Empty Strings (U12) ---\n");
        testU_COV_07_nullAndEmpty();
    }

    /**********
     * <p> Method: testU_COV_07_nullAndEmpty() </p>
     *
     * <p> Tests U12: User accepts and stores nulls and empty strings without throwing.
     * Documents the current behavior. </p>
     */
    private static void testU_COV_07_nullAndEmpty() {
        boolean threw = false;
        User u = null;
        try {
            u = new User(null, "", "", null, "", "", null, false, false, false);
        } catch (Exception e) {
            threw = true;
        }
        performTest("U-COV-07a", "Constructor with null and empty strings does not throw",
                String.valueOf(threw), "false");
        if (u != null) {
            performTest("U-COV-07b", "null userName stored as null",
                    u.getUserName() == null ? "null" : "not null", "null");
            performTest("U-COV-07c", "empty password stored as empty",
                    u.getPassword(), "");
        }
    }


    /*-******************************************************************************************

    Helpers

    */

    /**********
     * <p> Method: makeUser(boolean, boolean, boolean) </p>
     *
     * <p> Description: Builds a User with the requested role flag combination.  String fields
     * are filled in with reasonable defaults so the test does not have to repeat them. </p>
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
     * @param testID        Test ID string (for example "U-BV-01")
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
