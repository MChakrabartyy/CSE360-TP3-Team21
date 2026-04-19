package HW3;

import HW3.CoverageChecker.Result;
import HW3.CoverageChecker.StudentCoverage;
import entityClasses.PostList;
import entityClasses.ReplyList;

/*******
 * <p> Title: CoverageCheckerTests Class. </p>
 *
 * <p> Description: This class is the HW3 Task 5 test suite for {@link CoverageChecker}.  It
 * implements the test cases CC-T1 through CC-T13 from the Task 4.2 document
 * (TP3 Testing Details and Rationale.docx) and verifies every requirement CC-R1 through
 * CC-R14 from the Task 4.1 document (TP3 Testing Requirements.docx). </p>
 *
 * <p> The structure follows {@code StudentPostTests} from TP2 and the other HW3 test suites:
 * a single {@code main} method drives test sections, each test calls {@code performTest}
 * which prints PASS/FAIL and updates running counters, and a final summary line shows the
 * totals.  No external test framework is used. </p>
 *
 * <p><b>Test ID to requirement mapping (from Task 4.2):</b></p>
 * <ul>
 *   <li>CC-T1  -> CC-R3, CC-R10  (3 distinct askers, threshold 3 -> PASS)</li>
 *   <li>CC-T2  -> CC-R5          (4 distinct askers, threshold 3 -> PASS)</li>
 *   <li>CC-T3  -> CC-R9          (configurable threshold)</li>
 *   <li>CC-T4  -> CC-R13         (post-only student appears in result)</li>
 *   <li>CC-T5  -> CC-R12         (deterministic)</li>
 *   <li>CC-T6  -> CC-R8          (reply to deleted post still counts)</li>
 *   <li>CC-T7  -> CC-R14         (human-readable rendering)</li>
 *   <li>CC-T8  -> CC-R1, CC-R11  (empty input)</li>
 *   <li>CC-T9  -> CC-R11         (null inputs)</li>
 *   <li>CC-T10 -> CC-R2          (posts but no replies)</li>
 *   <li>CC-T11 -> CC-R4          (2 distinct askers -> FAIL)</li>
 *   <li>CC-T12 -> CC-R6          (many replies but only one distinct asker)</li>
 *   <li>CC-T13 -> CC-R7          (self-reply does not count)</li>
 * </ul>
 *
 * <p><b>How to interpret the output:</b> Each line prints PASS or FAIL followed by a test ID.
 * A FAIL line also prints the expected and actual values.  The final line shows total
 * passed and failed.  All requirements are satisfied if the final line shows 0 failed. </p>
 *
 * <p> Copyright: CSE 360 Team 21 &copy; 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-04-07  Initial HW3 Task 5 test suite for CoverageChecker
 */
public class CoverageCheckerTests {

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
        System.out.println("  CoverageCheckerTests -- HW3 Task 5");
        System.out.println("=============================================================\n");

        testPositiveSection();
        testNegativeSection();

        System.out.println("\n=============================================================");
        System.out.println("  Results: " + numPassed + " passed, " + numFailed + " failed");
        System.out.println("=============================================================");
    }


    /*-******************************************************************************************

    SECTION 1 -- POSITIVE TESTS (CC-T1..CC-T7)

    */

    private static void testPositiveSection() {
        System.out.println("--- Section 1: Positive Tests (CC-T1..CC-T7) ---\n");
        testCC_T1_threeDistinctAskersPass();
        testCC_T2_fourDistinctAskersPass();
        testCC_T3_configurableThreshold();
        testCC_T4_postOnlyStudentAppears();
        testCC_T5_deterministic();
        testCC_T6_replyToDeletedPostCounts();
        testCC_T7_humanReadableRendering();
    }

    /**********
     * <p> Method: testCC_T1_threeDistinctAskersPass() </p>
     *
     * <p> Tests CC-R3 and CC-R10: dave answers questions from alice, bob, and carol; with
     * threshold 3 his label is PASS, his count is 3, and his distinctAskers set contains
     * exactly those three usernames. </p>
     */
    private static void testCC_T1_threeDistinctAskersPass() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Q from alice", "body", "alice", "General", "question", -1);
        pl.createPost("Q from bob",   "body", "bob",   "General", "question", -1);
        pl.createPost("Q from carol", "body", "carol", "General", "question", -1);
        rl.createReply(1, "answer", "dave");
        rl.createReply(2, "answer", "dave");
        rl.createReply(3, "answer", "dave");

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage dave = r.get("dave");
        performTest("CC-T1a", "dave is in the result",
                dave != null ? "present" : "missing", "present");
        if (dave == null) return;
        performTest("CC-T1b", "dave count == 3",
                String.valueOf(dave.getCount()), "3");
        performTest("CC-T1c", "dave label == PASS",
                dave.getLabel(), "PASS");
        performTest("CC-T1d", "dave distinct askers contains alice",
                String.valueOf(dave.getDistinctAskers().contains("alice")), "true");
        performTest("CC-T1e", "dave distinct askers contains bob",
                String.valueOf(dave.getDistinctAskers().contains("bob")), "true");
        performTest("CC-T1f", "dave distinct askers contains carol",
                String.valueOf(dave.getDistinctAskers().contains("carol")), "true");
    }

    /**********
     * <p> Method: testCC_T2_fourDistinctAskersPass() </p>
     *
     * <p> Tests CC-R5: dave answers four distinct askers, threshold 3 -> PASS with count 4. </p>
     */
    private static void testCC_T2_fourDistinctAskersPass() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        pl.createPost("Qb", "b", "bob",   "General", "question", -1);
        pl.createPost("Qc", "b", "carol", "General", "question", -1);
        pl.createPost("Qe", "b", "eve",   "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        rl.createReply(2, "ans", "dave");
        rl.createReply(3, "ans", "dave");
        rl.createReply(4, "ans", "dave");

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage dave = r.get("dave");
        performTest("CC-T2a", "dave count == 4",
                dave == null ? "missing" : String.valueOf(dave.getCount()), "4");
        performTest("CC-T2b", "dave label == PASS",
                dave == null ? "missing" : dave.getLabel(), "PASS");
    }

    /**********
     * <p> Method: testCC_T3_configurableThreshold() </p>
     *
     * <p> Tests CC-R9: same input as CC-T1 (3 distinct askers for dave), but threshold 4
     * -> dave is FAIL because 3 &lt; 4. </p>
     */
    private static void testCC_T3_configurableThreshold() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        pl.createPost("Qb", "b", "bob",   "General", "question", -1);
        pl.createPost("Qc", "b", "carol", "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        rl.createReply(2, "ans", "dave");
        rl.createReply(3, "ans", "dave");

        Result r = new CoverageChecker().check(pl, rl, 4);
        StudentCoverage dave = r.get("dave");
        performTest("CC-T3a", "Threshold reflected in result",
                String.valueOf(r.getThreshold()), "4");
        performTest("CC-T3b", "dave count still 3",
                dave == null ? "missing" : String.valueOf(dave.getCount()), "3");
        performTest("CC-T3c", "dave label == FAIL at threshold 4",
                dave == null ? "missing" : dave.getLabel(), "FAIL");
    }

    /**********
     * <p> Method: testCC_T4_postOnlyStudentAppears() </p>
     *
     * <p> Tests CC-R13: alice posts but never replies and nobody replies to her.  She must
     * still appear in the result with count 0 and label FAIL. </p>
     */
    private static void testCC_T4_postOnlyStudentAppears() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage alice = r.get("alice");
        performTest("CC-T4a", "alice present in result",
                alice != null ? "present" : "missing", "present");
        if (alice == null) return;
        performTest("CC-T4b", "alice count == 0",
                String.valueOf(alice.getCount()), "0");
        performTest("CC-T4c", "alice label == FAIL",
                alice.getLabel(), "FAIL");
    }

    /**********
     * <p> Method: testCC_T5_deterministic() </p>
     *
     * <p> Tests CC-R12: calling the checker twice on the same input returns equal results.
     * The Result and StudentCoverage equals methods are used to compare. </p>
     */
    private static void testCC_T5_deterministic() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        pl.createPost("Qb", "b", "bob",   "General", "question", -1);
        pl.createPost("Qc", "b", "carol", "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        rl.createReply(2, "ans", "dave");
        rl.createReply(3, "ans", "dave");

        CoverageChecker cc = new CoverageChecker();
        Result r1 = cc.check(pl, rl);
        Result r2 = cc.check(pl, rl);
        performTest("CC-T5", "Two runs on same input return equal results",
                String.valueOf(r1.equals(r2)), "true");
    }

    /**********
     * <p> Method: testCC_T6_replyToDeletedPostCounts() </p>
     *
     * <p> Tests CC-R8: dave replies to alice's question, then alice soft-deletes her post.
     * dave should still get credit because he did the work; soft delete should not undo it. </p>
     */
    private static void testCC_T6_replyToDeletedPostCounts() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        // alice soft-deletes her post (same call StudentPostTests uses)
        pl.deletePost(1, "alice");

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage dave = r.get("dave");
        performTest("CC-T6a", "dave still credited after post soft-deleted",
                dave == null ? "missing" : String.valueOf(dave.getCount()), "1");
        performTest("CC-T6b", "dave distinct askers contains alice",
                dave == null ? "missing"
                        : String.valueOf(dave.getDistinctAskers().contains("alice")),
                "true");
    }

    /**********
     * <p> Method: testCC_T7_humanReadableRendering() </p>
     *
     * <p> Tests CC-R14: toReport returns a string that contains both usernames, both counts,
     * and both labels. </p>
     */
    private static void testCC_T7_humanReadableRendering() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        pl.createPost("Qb", "b", "bob",   "General", "question", -1);
        pl.createPost("Qc", "b", "carol", "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        rl.createReply(2, "ans", "dave");
        rl.createReply(3, "ans", "dave");

        CoverageChecker cc = new CoverageChecker();
        String report = cc.toReport(cc.check(pl, rl));
        performTest("CC-T7a", "Report mentions dave",
                String.valueOf(report.contains("dave")), "true");
        performTest("CC-T7b", "Report mentions alice",
                String.valueOf(report.contains("alice")), "true");
        performTest("CC-T7c", "Report contains PASS label",
                String.valueOf(report.contains("PASS")), "true");
        performTest("CC-T7d", "Report contains FAIL label",
                String.valueOf(report.contains("FAIL")), "true");
        performTest("CC-T7e", "Report contains threshold",
                String.valueOf(report.contains("threshold=3")), "true");
    }


    /*-******************************************************************************************

    SECTION 2 -- NEGATIVE TESTS (CC-T8..CC-T13)

    */

    private static void testNegativeSection() {
        System.out.println("\n--- Section 2: Negative Tests (CC-T8..CC-T13) ---\n");
        testCC_T8_emptyInput();
        testCC_T9_nullInputs();
        testCC_T10_postsButNoReplies();
        testCC_T11_twoDistinctAskersFail();
        testCC_T12_manyRepliesOneAsker();
        testCC_T13_selfReplyDoesNotCount();
    }

    /**********
     * <p> Method: testCC_T8_emptyInput() </p>
     *
     * <p> Tests CC-R1 and CC-R11: empty PostList and empty ReplyList -> empty result, no
     * exception. </p>
     */
    private static void testCC_T8_emptyInput() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        Result r = new CoverageChecker().check(pl, rl);
        performTest("CC-T8a", "Empty input returns non-null result",
                r != null ? "non-null" : "null", "non-null");
        if (r == null) return;
        performTest("CC-T8b", "Empty input result is empty",
                String.valueOf(r.isEmpty()), "true");
        performTest("CC-T8c", "Empty input result size 0",
                String.valueOf(r.size()), "0");
    }

    /**********
     * <p> Method: testCC_T9_nullInputs() </p>
     *
     * <p> Tests CC-R11: null PostList or null ReplyList -> empty result, no exception. </p>
     */
    private static void testCC_T9_nullInputs() {
        CoverageChecker cc = new CoverageChecker();
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);

        boolean threwOnNullPosts = false;
        Result r1 = null;
        try { r1 = cc.check(null, rl); }
        catch (Exception e) { threwOnNullPosts = true; }
        performTest("CC-T9a", "null PostList does not throw",
                String.valueOf(threwOnNullPosts), "false");
        performTest("CC-T9b", "null PostList returns empty result",
                r1 == null ? "null" : String.valueOf(r1.isEmpty()), "true");

        boolean threwOnNullReplies = false;
        Result r2 = null;
        try { r2 = cc.check(pl, null); }
        catch (Exception e) { threwOnNullReplies = true; }
        performTest("CC-T9c", "null ReplyList does not throw",
                String.valueOf(threwOnNullReplies), "false");
        performTest("CC-T9d", "null ReplyList returns empty result",
                r2 == null ? "null" : String.valueOf(r2.isEmpty()), "true");
    }

    /**********
     * <p> Method: testCC_T10_postsButNoReplies() </p>
     *
     * <p> Tests CC-R2: alice and bob each post a question, no replies.  Both should appear
     * in the result with count 0 and label FAIL. </p>
     */
    private static void testCC_T10_postsButNoReplies() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        pl.createPost("Qb", "b", "bob",   "General", "question", -1);

        Result r = new CoverageChecker().check(pl, rl);
        performTest("CC-T10a", "Result size == 2",
                String.valueOf(r.size()), "2");
        StudentCoverage alice = r.get("alice");
        StudentCoverage bob = r.get("bob");
        performTest("CC-T10b", "alice count 0 label FAIL",
                alice == null ? "missing" : alice.getCount() + "/" + alice.getLabel(),
                "0/FAIL");
        performTest("CC-T10c", "bob count 0 label FAIL",
                bob == null ? "missing" : bob.getCount() + "/" + bob.getLabel(),
                "0/FAIL");
    }

    /**********
     * <p> Method: testCC_T11_twoDistinctAskersFail() </p>
     *
     * <p> Tests CC-R4: dave answers questions from only 2 distinct askers, threshold 3
     * -> FAIL. </p>
     */
    private static void testCC_T11_twoDistinctAskersFail() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        pl.createPost("Qb", "b", "bob",   "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        rl.createReply(2, "ans", "dave");

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage dave = r.get("dave");
        performTest("CC-T11a", "dave count 2",
                dave == null ? "missing" : String.valueOf(dave.getCount()), "2");
        performTest("CC-T11b", "dave label FAIL",
                dave == null ? "missing" : dave.getLabel(), "FAIL");
    }

    /**********
     * <p> Method: testCC_T12_manyRepliesOneAsker() </p>
     *
     * <p> Tests CC-R6: dave replies five times but always to alice's posts.  Distinct
     * asker count is 1, not 5. </p>
     */
    private static void testCC_T12_manyRepliesOneAsker() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa1", "b", "alice", "General", "question", -1);
        pl.createPost("Qa2", "b", "alice", "General", "question", -1);
        pl.createPost("Qa3", "b", "alice", "General", "question", -1);
        pl.createPost("Qa4", "b", "alice", "General", "question", -1);
        pl.createPost("Qa5", "b", "alice", "General", "question", -1);
        rl.createReply(1, "ans", "dave");
        rl.createReply(2, "ans", "dave");
        rl.createReply(3, "ans", "dave");
        rl.createReply(4, "ans", "dave");
        rl.createReply(5, "ans", "dave");

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage dave = r.get("dave");
        performTest("CC-T12a", "dave count 1 (distinct, not total)",
                dave == null ? "missing" : String.valueOf(dave.getCount()), "1");
        performTest("CC-T12b", "dave label FAIL",
                dave == null ? "missing" : dave.getLabel(), "FAIL");
    }

    /**********
     * <p> Method: testCC_T13_selfReplyDoesNotCount() </p>
     *
     * <p> Tests CC-R7: alice posts a question and answers her own post.  Her self-reply
     * should not contribute to her distinct-asker count. </p>
     */
    private static void testCC_T13_selfReplyDoesNotCount() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Qa", "b", "alice", "General", "question", -1);
        rl.createReply(1, "self ans", "alice");

        Result r = new CoverageChecker().check(pl, rl);
        StudentCoverage alice = r.get("alice");
        performTest("CC-T13a", "alice present in result",
                alice != null ? "present" : "missing", "present");
        if (alice == null) return;
        performTest("CC-T13b", "alice count 0 (self-reply does not count)",
                String.valueOf(alice.getCount()), "0");
        performTest("CC-T13c", "alice label FAIL",
                alice.getLabel(), "FAIL");
    }


    /*-******************************************************************************************

    Helper

    */

    /**********
     * <p> Method: performTest(String, String, String, String) </p>
     *
     * <p> Description: Compares actual against expected, prints PASS or FAIL on the console,
     * and updates the running counters.  Same pattern as StudentPostTests from TP2. </p>
     *
     * @param testID        Test ID string (for example "CC-T1a")
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
