package guiStudentPostTests;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;
import java.util.List;

/*******
 * <p> Title: StudentPostTests Class. </p>
 *
 * <p> Description: This class is the TP2 semi-automated test suite for the Student Discussion
 * System.  It tests all CRUD operations, input validation, and Student User Story behaviour
 * implemented in {@code Post}, {@code PostList}, {@code Reply}, and {@code ReplyList}.
 *
 * <p> The structure follows the same pattern as {@code PostReplyTestingAutomation} from HW2:
 * each test calls a helper that prints PASS/FAIL and tracks a running counter. </p>
 *
 * <p><b>Requirements tested (REQ-01 through REQ-17):</b></p>
 * <ul>
 *   <li>REQ-01 (US-S1): A student can create a post with title, body, thread, and type.</li>
 *   <li>REQ-02 (US-S5): If no thread is specified, post defaults to "General".</li>
 *   <li>REQ-03 (US-S2): A student can view a list of all posts.</li>
 *   <li>REQ-04 (US-S3): A student can view a list of only their own posts.</li>
 *   <li>REQ-05 (US-S3): Each post in "my posts" shows reply count and unread reply count.</li>
 *   <li>REQ-06 (US-S3): A student can filter their posts to show only those with unread replies.</li>
 *   <li>REQ-07 (US-S4, US-S7): A student can search posts by keyword across all threads.</li>
 *   <li>REQ-08 (US-S7): A student can search posts by keyword within a specific thread.</li>
 *   <li>REQ-09 (US-S4): Search results show read/unread status, reply count, unread reply count.</li>
 *   <li>REQ-10 (US-S6): A student can delete one of their own posts.</li>
 *   <li>REQ-11 (US-S6): Before deleting, an "Are you sure?" confirmation is required (UI layer).</li>
 *   <li>REQ-12 (US-S6): When a post is deleted, its replies are NOT deleted.</li>
 *   <li>REQ-13 (US-S6): Viewing a reply to a deleted post shows a deleted-post placeholder.</li>
 *   <li>REQ-14 (US-S6): A student cannot delete another student's post.</li>
 *   <li>REQ-15 (validation): Post titles and bodies must pass input validation.</li>
 *   <li>REQ-16 (Task 3.1): Post class is located in the entityClasses package.</li>
 *   <li>REQ-17 (Task 3.2): Post class implements full CRUD (Create, Read, Update, Delete).</li>
 * </ul>
 *
 * <p><b>Test-to-requirement mapping:</b></p>
 * <ul>
 *   <li>REQ-01: testCreatePost_validInputs(), testCreatePost_questionType()</li>
 *   <li>REQ-02: testCreatePost_defaultThread()</li>
 *   <li>REQ-03: testGetAllPosts_returnsAll(), testGetAllPosts_emptyList()</li>
 *   <li>REQ-04: testGetMyPosts_filtersByAuthor()</li>
 *   <li>REQ-05: testGetMyPosts_includesReplyCount()</li>
 *   <li>REQ-06: testGetMyPostsUnreadOnly()</li>
 *   <li>REQ-07: testSearchPosts_allThreads()</li>
 *   <li>REQ-08: testSearchPosts_specificThread()</li>
 *   <li>REQ-09: testSearchPosts_showsReadStatus()</li>
 *   <li>REQ-10: testDeletePost_ownPost()</li>
 *   <li>REQ-11: testDeletePost_confirmationHandledByView()</li>
 *   <li>REQ-12: testDeletePost_repliesPreserved()</li>
 *   <li>REQ-13: testDeletePost_placeholder()</li>
 *   <li>REQ-14: testDeletePost_cannotDeleteOthers()</li>
 *   <li>REQ-15: testValidation_emptyTitle(), testValidation_emptyBody(),
 *               testValidation_titleTooLong(), testValidation_bodyTooLong()</li>
 *   <li>REQ-16: testPostClassInEntityClassesPackage()</li>
 *   <li>REQ-17: testFullCRUD_createReadUpdateDelete()</li>
 * </ul>
 *
 * <p><b>How to interpret output:</b> Each line prints PASS or FAIL followed by the test ID.
 * A FAIL line also prints the expected and actual values.  The final summary shows total
 * passed and failed counts.  All tests must show PASS for the requirements to be satisfied.</p>
 *
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-03-20  Initial TP2 test suite
 */
public class StudentPostTests {

    /** Running count of failed tests. */
    private static int numFailed = 0;

    /** Running count of passed tests. */
    private static int numPassed = 0;


    /**********
     * <p> Method: main(String[]) </p>
     *
     * <p> Description: Entry point.  Runs all test sections in order then prints summary.
     * To interpret: if final line shows "0 failed" all requirements are satisfied. </p>
     *
     * @param args  Command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("  StudentPostTests -- TP2 Student Discussion System Tests");
        System.out.println("=============================================================\n");

        testCreateSection();
        testReadSection();
        testMyPostsSection();
        testSearchSection();
        testDeleteSection();
        testValidationSection();
        testPackageAndCRUDSection();

        System.out.println("\n=============================================================");
        System.out.println("  Results: " + numPassed + " passed, " + numFailed + " failed");
        System.out.println("=============================================================");
    }


    /*-******************************************************************************************

    SECTION 1 — CREATE (REQ-01, REQ-02)

    */

    private static void testCreateSection() {
        System.out.println("--- Section 1: Create Post (REQ-01, REQ-02) ---\n");
        testCreatePost_validInputs();
        testCreatePost_questionType();
        testCreatePost_defaultThread();
    }

    /**********
     * <p> Method: testCreatePost_validInputs() </p>
     *
     * <p> Tests REQ-01 (US-S1): A student can create a post with title, body, thread, type. </p>
     *
     * <p> How the test works: Calls createPost() with all valid inputs and checks that the
     * return value is an empty string (success) and that the post is stored in the list. </p>
     *
     * <p> How to interpret output: PASS means createPost() accepted valid inputs and stored
     * the post.  FAIL means the method incorrectly rejected valid inputs. </p>
     */
    private static void testCreatePost_validInputs() {
        PostList pl = new PostList();
        String result = pl.createPost("How do I use ArrayLists?",
                "I need help understanding ArrayLists in Java.",
                "student1", "General", "question", -1);
        boolean stored = pl.getPostByID(1) != null;
        performTest("REQ-01-1", "Create post with valid inputs",
                result.isEmpty() && stored ? "" : "Post not created", "");
    }

    /**********
     * <p> Method: testCreatePost_questionType() </p>
     *
     * <p> Tests REQ-01 (US-S1): Post type "question" is stored correctly. </p>
     *
     * <p> How the test works: Creates a post with postType="question" and verifies the
     * stored post returns "question" from getPostType(). </p>
     *
     * <p> How to interpret output: PASS means post type is stored and retrieved correctly. </p>
     */
    private static void testCreatePost_questionType() {
        PostList pl = new PostList();
        pl.createPost("Test Question", "Body here", "student1", "General", "question", -1);
        Post p = pl.getPostByID(1);
        performTest("REQ-01-2", "Post type 'question' stored correctly",
                p != null ? p.getPostType() : "null", "question");
    }

    /**********
     * <p> Method: testCreatePost_defaultThread() </p>
     *
     * <p> Tests REQ-02 (US-S5): If no thread specified, post defaults to "General". </p>
     *
     * <p> How the test works: Calls createPost() with null threadName and verifies the
     * stored post has threadName = "General". </p>
     *
     * <p> How to interpret output: PASS means the default thread logic works correctly.
     * FAIL means the Post constructor did not apply the "General" default. </p>
     */
    private static void testCreatePost_defaultThread() {
        PostList pl = new PostList();
        // US-S5: pass null for threadName — should default to "General"
        pl.createPost("No Thread Post", "Body here", "student1", null, "statement", -1);
        Post p = pl.getPostByID(1);
        performTest("REQ-02-1", "Post defaults to 'General' when thread is null",
                p != null ? p.getThreadName() : "null", "General");

        PostList pl2 = new PostList();
        // US-S5: pass blank string — should also default to "General"
        pl2.createPost("Blank Thread", "Body", "student1", "", "statement", -1);
        Post p2 = pl2.getPostByID(1);
        performTest("REQ-02-2", "Post defaults to 'General' when thread is blank",
                p2 != null ? p2.getThreadName() : "null", "General");
    }


    /*-******************************************************************************************

    SECTION 2 — READ / GET ALL POSTS (REQ-03)

    */

    private static void testReadSection() {
        System.out.println("\n--- Section 2: Read / Get All Posts (REQ-03) ---\n");
        testGetAllPosts_returnsAll();
        testGetAllPosts_emptyList();
    }

    /**********
     * <p> Method: testGetAllPosts_returnsAll() </p>
     *
     * <p> Tests REQ-03 (US-S2): A student can view a list of all posts. </p>
     *
     * <p> How the test works: Creates 3 posts and calls getAllPosts().  Verifies the list
     * contains all 3 posts. </p>
     *
     * <p> How to interpret output: PASS means getAllPosts() returns all stored posts.
     * FAIL means some posts were lost or not stored. </p>
     */
    private static void testGetAllPosts_returnsAll() {
        PostList pl = new PostList();
        pl.createPost("Post A", "Body A", "alice",   "General", "question",  -1);
        pl.createPost("Post B", "Body B", "bob",     "HW1",     "statement", -1);
        pl.createPost("Post C", "Body C", "student1","General", "question",  -1);
        int size = pl.getAllPosts().size();
        performTest("REQ-03-1", "getAllPosts() returns all 3 posts",
                String.valueOf(size), "3");
    }

    /**********
     * <p> Method: testGetAllPosts_emptyList() </p>
     *
     * <p> Tests REQ-03 (US-S2): Empty list returns size 0 (UI shows "No posts found."). </p>
     *
     * <p> How to interpret output: PASS means empty state is handled correctly. </p>
     */
    private static void testGetAllPosts_emptyList() {
        PostList pl = new PostList();
        int size = pl.getAllPosts().size();
        performTest("REQ-03-2", "getAllPosts() returns empty list when no posts exist",
                String.valueOf(size), "0");
    }


    /*-******************************************************************************************

    SECTION 3 — MY POSTS (REQ-04, REQ-05, REQ-06)

    */

    private static void testMyPostsSection() {
        System.out.println("\n--- Section 3: My Posts (REQ-04, REQ-05, REQ-06) ---\n");
        testGetMyPosts_filtersByAuthor();
        testGetMyPosts_includesReplyCount();
        testGetMyPostsUnreadOnly();
    }

    /**********
     * <p> Method: testGetMyPosts_filtersByAuthor() </p>
     *
     * <p> Tests REQ-04 (US-S3): getMyPosts() returns only posts by the given author. </p>
     *
     * <p> How the test works: Creates posts by two different authors, calls getMyPosts()
     * for one author, and verifies only that author's posts are returned. </p>
     *
     * <p> How to interpret output: PASS means filtering by author works correctly. </p>
     */
    private static void testGetMyPosts_filtersByAuthor() {
        PostList pl = new PostList();
        pl.createPost("Alice Post 1", "Body", "alice",    "General", "question", -1);
        pl.createPost("Alice Post 2", "Body", "alice",    "General", "question", -1);
        pl.createPost("Bob Post 1",   "Body", "bob",      "General", "question", -1);
        List<Post> myPosts = pl.getMyPosts("alice");
        performTest("REQ-04-1", "getMyPosts() returns only alice's 2 posts",
                String.valueOf(myPosts.size()), "2");
        boolean allByAlice = myPosts.stream().allMatch(p -> p.getAuthor().equals("alice"));
        performTest("REQ-04-2", "All returned posts are authored by alice",
                allByAlice ? "true" : "false", "true");
    }

    /**********
     * <p> Method: testGetMyPosts_includesReplyCount() </p>
     *
     * <p> Tests REQ-05 (US-S3): Each post in "my posts" shows reply count and unread count. </p>
     *
     * <p> How the test works: Creates a post, increments its reply count twice via
     * incrementReplyCount(), then calls getMyPosts() and checks replyCount == 2. </p>
     *
     * <p> How to interpret output: PASS means reply count tracking works correctly. </p>
     */
    private static void testGetMyPosts_includesReplyCount() {
        PostList pl = new PostList();
        pl.createPost("My Post", "Body", "student1", "General", "question", -1);
        // Simulate 2 replies being added
        pl.incrementReplyCount(1);
        pl.incrementReplyCount(1);
        List<Post> myPosts = pl.getMyPosts("student1");
        int replyCount = myPosts.isEmpty() ? -1 : myPosts.get(0).getReplyCount();
        performTest("REQ-05-1", "Reply count is 2 after 2 replies added",
                String.valueOf(replyCount), "2");
        int unreadCount = myPosts.isEmpty() ? -1 : myPosts.get(0).getUnreadReplyCount();
        performTest("REQ-05-2", "Unread reply count is 2 after 2 replies added",
                String.valueOf(unreadCount), "2");
    }

    /**********
     * <p> Method: testGetMyPostsUnreadOnly() </p>
     *
     * <p> Tests REQ-06 (US-S3): getMyPostsUnreadOnly() returns only posts with unread replies. </p>
     *
     * <p> How the test works: Creates 2 posts for the same author, adds unread replies to
     * only one, calls getMyPostsUnreadOnly() and verifies only 1 post is returned. </p>
     *
     * <p> How to interpret output: PASS means unread-only filter works correctly. </p>
     */
    private static void testGetMyPostsUnreadOnly() {
        PostList pl = new PostList();
        pl.createPost("Post With Replies",    "Body", "student1", "General", "question", -1);
        pl.createPost("Post Without Replies", "Body", "student1", "General", "question", -1);
        // Only post 1 gets a reply
        pl.incrementReplyCount(1);
        List<Post> unreadOnly = pl.getMyPostsUnreadOnly("student1");
        performTest("REQ-06-1", "getMyPostsUnreadOnly() returns 1 post (only the one with unread)",
                String.valueOf(unreadOnly.size()), "1");
        performTest("REQ-06-2", "The returned post is the one with unread replies",
                unreadOnly.isEmpty() ? "none" : String.valueOf(unreadOnly.get(0).getPostID()), "1");
    }


    /*-******************************************************************************************

    SECTION 4 — SEARCH (REQ-07, REQ-08, REQ-09)

    */

    private static void testSearchSection() {
        System.out.println("\n--- Section 4: Search (REQ-07, REQ-08, REQ-09) ---\n");
        testSearchPosts_allThreads();
        testSearchPosts_specificThread();
        testSearchPosts_showsReadStatus();
    }

    /**********
     * <p> Method: testSearchPosts_allThreads() </p>
     *
     * <p> Tests REQ-07 (US-S4, US-S7): Search by keyword across all threads. </p>
     *
     * <p> How the test works: Creates posts in different threads, searches with a keyword
     * that matches one post, passes null for threadName (all threads), verifies 1 result. </p>
     *
     * <p> How to interpret output: PASS means keyword search across all threads works. </p>
     */
    private static void testSearchPosts_allThreads() {
        PostList pl = new PostList();
        pl.createPost("Java sorting help", "How do I sort?", "alice",   "General", "question", -1);
        pl.createPost("Python basics",     "How does Python work?", "bob", "HW1",  "question", -1);
        // null thread = search all threads (US-S7)
        List<Post> results = pl.searchPosts("java", null);
        performTest("REQ-07-1", "Search 'java' across all threads returns 1 result",
                String.valueOf(results.size()), "1");
        performTest("REQ-07-2", "Search is case-insensitive ('java' matches 'Java')",
                results.isEmpty() ? "no match" : results.get(0).getTitle(), "Java sorting help");
    }

    /**********
     * <p> Method: testSearchPosts_specificThread() </p>
     *
     * <p> Tests REQ-08 (US-S7): Search by keyword within a specific thread. </p>
     *
     * <p> How the test works: Creates posts in "General" and "HW1" threads, searches with
     * a keyword that exists in both but restricts to "HW1" thread only. </p>
     *
     * <p> How to interpret output: PASS means thread-filtered search works correctly. </p>
     */
    private static void testSearchPosts_specificThread() {
        PostList pl = new PostList();
        pl.createPost("Help with arrays",  "Arrays question", "alice", "General", "question", -1);
        pl.createPost("Arrays in HW1",     "Arrays in HW1",   "bob",   "HW1",     "question", -1);
        // Search "arrays" in HW1 only — should return 1 result
        List<Post> results = pl.searchPosts("arrays", "HW1");
        performTest("REQ-08-1", "Search 'arrays' in thread 'HW1' returns 1 result",
                String.valueOf(results.size()), "1");
        performTest("REQ-08-2", "Result is from HW1 thread only",
                results.isEmpty() ? "none" : results.get(0).getThreadName(), "HW1");
    }

    /**********
     * <p> Method: testSearchPosts_showsReadStatus() </p>
     *
     * <p> Tests REQ-09 (US-S4): Search results show read/unread status. </p>
     *
     * <p> How the test works: Creates a post, marks it as read via markPostAsRead(),
     * searches for it, and verifies isReadByCurrentUser() returns true. </p>
     *
     * <p> How to interpret output: PASS means read status is tracked and shown in results. </p>
     */
    private static void testSearchPosts_showsReadStatus() {
        PostList pl = new PostList();
        pl.createPost("Unread Post", "Body about sorting", "alice", "General", "question", -1);
        // Initially unread
        List<Post> before = pl.searchPosts("sorting", null);
        performTest("REQ-09-1", "Post is unread before markPostAsRead()",
                before.isEmpty() ? "empty" :
                String.valueOf(before.get(0).isReadByCurrentUser()), "false");
        // Mark as read
        pl.markPostAsRead(1);
        List<Post> after = pl.searchPosts("sorting", null);
        performTest("REQ-09-2", "Post is read after markPostAsRead()",
                after.isEmpty() ? "empty" :
                String.valueOf(after.get(0).isReadByCurrentUser()), "true");
    }


    /*-******************************************************************************************

    SECTION 5 — DELETE (REQ-10 through REQ-14)

    */

    private static void testDeleteSection() {
        System.out.println("\n--- Section 5: Delete Post (REQ-10 through REQ-14) ---\n");
        testDeletePost_ownPost();
        testDeletePost_confirmationHandledByView();
        testDeletePost_repliesPreserved();
        testDeletePost_placeholder();
        testDeletePost_cannotDeleteOthers();
    }

    /**********
     * <p> Method: testDeletePost_ownPost() </p>
     *
     * <p> Tests REQ-10 (US-S6): A student can delete their own post. </p>
     *
     * <p> How the test works: Creates a post as "student1", calls deletePost() as "student1",
     * verifies return is empty string and isDeleted() is true. </p>
     *
     * <p> How to interpret output: PASS means author can soft-delete their own post. </p>
     */
    private static void testDeletePost_ownPost() {
        PostList pl = new PostList();
        pl.createPost("My Post", "Body", "student1", "General", "question", -1);
        String result = pl.deletePost(1, "student1");
        Post p = pl.getPostByID(1);
        performTest("REQ-10-1", "deletePost() returns empty string for own post",
                result, "");
        performTest("REQ-10-2", "Post isDeleted flag is true after deletion",
                p != null ? String.valueOf(p.isDeleted()) : "null", "true");
    }

    /**********
     * <p> Method: testDeletePost_confirmationHandledByView() </p>
     *
     * <p> Tests REQ-11 (US-S6): "Are you sure?" confirmation is required before delete. </p>
     *
     * <p> How the test works: This requirement is handled entirely by the View layer
     * (ViewStudentPosts shows an Alert dialog before calling doDeletePost()).
     * This test verifies the model-level delete only executes when called — confirming
     * the View is the gatekeeper.  See ViewStudentPosts.java button_DeletePost handler. </p>
     *
     * <p> How to interpret output: PASS confirms the model delete works when called.
     * The confirmation dialog is verified visually via app screenshot ss_26. </p>
     */
    private static void testDeletePost_confirmationHandledByView() {
        PostList pl = new PostList();
        pl.createPost("Test Post", "Body", "student1", "General", "question", -1);
        // Model-level: delete executes only when called (View shows dialog first)
        String result = pl.deletePost(1, "student1");
        performTest("REQ-11-1", "Model delete executes correctly when View calls it",
                result, "");
        System.out.println("      Note: 'Are you sure?' dialog verified via app screenshot ss_26");
    }

    /**********
     * <p> Method: testDeletePost_repliesPreserved() </p>
     *
     * <p> Tests REQ-12 (US-S6): When a post is deleted, its replies are NOT deleted. </p>
     *
     * <p> How the test works: Creates a post with 2 replies, soft-deletes the post,
     * then calls getRepliesForPost() and verifies 2 replies still exist. </p>
     *
     * <p> How to interpret output: PASS means soft delete preserves replies. </p>
     */
    private static void testDeletePost_repliesPreserved() {
        PostList pl = new PostList();
        ReplyList rl = new ReplyList(pl);
        pl.createPost("Post With Replies", "Body", "student1", "General", "question", -1);
        rl.createReply(1, "Reply one", "alice");
        rl.createReply(1, "Reply two", "bob");
        // Soft-delete the post
        pl.deletePost(1, "student1");
        // Replies must still exist (US-S6)
        int repliesAfter = rl.getRepliesForPost(1).size();
        performTest("REQ-12-1", "Replies survive after post is soft-deleted",
                String.valueOf(repliesAfter), "2");
    }

    /**********
     * <p> Method: testDeletePost_placeholder() </p>
     *
     * <p> Tests REQ-13 (US-S6): Deleted post shows placeholder "The original post has been deleted." </p>
     *
     * <p> How the test works: Creates and soft-deletes a post, then calls toString() and
     * verifies it contains the placeholder message. </p>
     *
     * <p> How to interpret output: PASS means the deleted-post placeholder is shown correctly. </p>
     */
    private static void testDeletePost_placeholder() {
        PostList pl = new PostList();
        pl.createPost("Post To Delete", "Original body", "student1", "General", "question", -1);
        pl.deletePost(1, "student1");
        Post p = pl.getPostByID(1);
        boolean hasPlaceholder = p != null &&
                p.toString().contains("The original post has been deleted.");
        performTest("REQ-13-1", "Deleted post toString() shows placeholder message",
                hasPlaceholder ? "true" : "false", "true");
    }

    /**********
     * <p> Method: testDeletePost_cannotDeleteOthers() </p>
     *
     * <p> Tests REQ-14 (US-S6): A student cannot delete another student's post. </p>
     *
     * <p> How the test works: Creates a post as "alice", attempts deletePost() as "bob",
     * verifies an error message is returned and the post is NOT deleted. </p>
     *
     * <p> How to interpret output: PASS means the author check prevents unauthorised deletion. </p>
     */
    private static void testDeletePost_cannotDeleteOthers() {
        PostList pl = new PostList();
        pl.createPost("Alice's Post", "Body", "alice", "General", "question", -1);
        // Bob tries to delete Alice's post
        String result = pl.deletePost(1, "bob");
        Post p = pl.getPostByID(1);
        performTest("REQ-14-1", "deletePost() returns error when non-author tries to delete",
                result, "You can only delete your own posts.");
        performTest("REQ-14-2", "Post is NOT deleted when non-author attempts deletion",
                p != null ? String.valueOf(p.isDeleted()) : "null", "false");
    }


    /*-******************************************************************************************

    SECTION 6 — VALIDATION (REQ-15)

    */

    private static void testValidationSection() {
        System.out.println("\n--- Section 6: Input Validation (REQ-15) ---\n");
        testValidation_emptyTitle();
        testValidation_emptyBody();
        testValidation_titleTooLong();
        testValidation_bodyTooLong();
        testValidation_emptyAuthor();
    }

    /**********
     * <p> Method: testValidation_emptyTitle() </p>
     *
     * <p> Tests REQ-15: Post title cannot be empty. </p>
     *
     * <p> How to interpret output: PASS means empty title is rejected with correct error. </p>
     */
    private static void testValidation_emptyTitle() {
        PostList pl = new PostList();
        String result = pl.createPost("", "Valid body", "student1", "General", "question", -1);
        performTest("REQ-15-1", "Empty title rejected with correct error",
                result, "Post title is required.");
    }

    /**********
     * <p> Method: testValidation_emptyBody() </p>
     *
     * <p> Tests REQ-15: Post body cannot be empty. </p>
     *
     * <p> How to interpret output: PASS means empty body is rejected with correct error. </p>
     */
    private static void testValidation_emptyBody() {
        PostList pl = new PostList();
        String result = pl.createPost("Valid Title", "", "student1", "General", "question", -1);
        performTest("REQ-15-2", "Empty body rejected with correct error",
                result, "Post body is required.");
    }

    /**********
     * <p> Method: testValidation_titleTooLong() </p>
     *
     * <p> Tests REQ-15: Title over 200 characters is rejected. </p>
     *
     * <p> How to interpret output: PASS means title length limit is enforced. </p>
     */
    private static void testValidation_titleTooLong() {
        PostList pl = new PostList();
        String longTitle = "A".repeat(201);
        String result = pl.createPost(longTitle, "Body", "student1", "General", "question", -1);
        performTest("REQ-15-3", "Title over 200 chars rejected",
                result, "Title must be 200 characters or fewer.");
    }

    /**********
     * <p> Method: testValidation_bodyTooLong() </p>
     *
     * <p> Tests REQ-15: Body over 2000 characters is rejected. </p>
     *
     * <p> How to interpret output: PASS means body length limit is enforced. </p>
     */
    private static void testValidation_bodyTooLong() {
        PostList pl = new PostList();
        String longBody = "B".repeat(2001);
        String result = pl.createPost("Title", longBody, "student1", "General", "question", -1);
        performTest("REQ-15-4", "Body over 2000 chars rejected",
                result, "Body must be 2000 characters or fewer.");
    }

    /**********
     * <p> Method: testValidation_emptyAuthor() </p>
     *
     * <p> Tests REQ-15: Author cannot be empty. </p>
     *
     * <p> How to interpret output: PASS means empty author is rejected. </p>
     */
    private static void testValidation_emptyAuthor() {
        PostList pl = new PostList();
        String result = pl.createPost("Title", "Body", "", "General", "question", -1);
        performTest("REQ-15-5", "Empty author rejected with correct error",
                result, "Author is required.");
    }


    /*-******************************************************************************************

    SECTION 7 — PACKAGE AND FULL CRUD (REQ-16, REQ-17)

    */

    private static void testPackageAndCRUDSection() {
        System.out.println("\n--- Section 7: Package & Full CRUD (REQ-16, REQ-17) ---\n");
        testPostClassInEntityClassesPackage();
        testFullCRUD_createReadUpdateDelete();
    }

    /**********
     * <p> Method: testPostClassInEntityClassesPackage() </p>
     *
     * <p> Tests REQ-16 (Task 3.1): Post class is in entityClasses package. </p>
     *
     * <p> How the test works: Uses Java reflection to check the package name of the Post class. </p>
     *
     * <p> How to interpret output: PASS means Post.class is in the entityClasses package. </p>
     */
    private static void testPostClassInEntityClassesPackage() {
        String packageName = Post.class.getPackageName();
        performTest("REQ-16-1", "Post class is in entityClasses package",
                packageName, "entityClasses");
    }

    /**********
     * <p> Method: testFullCRUD_createReadUpdateDelete() </p>
     *
     * <p> Tests REQ-17 (Task 3.2): Post class implements full CRUD. </p>
     *
     * <p> How the test works: Performs all four CRUD operations in sequence on one PostList:
     * Create a post, Read it back, Update its title, Delete it. Verifies each step. </p>
     *
     * <p> How to interpret output: PASS on all 4 sub-tests means full CRUD is implemented. </p>
     */
    private static void testFullCRUD_createReadUpdateDelete() {
        PostList pl = new PostList();

        // CREATE
        String createResult = pl.createPost("Original Title", "Original body",
                "student1", "General", "question", -1);
        performTest("REQ-17-1", "CRUD - Create: post created successfully",
                createResult, "");

        // READ
        Post p = pl.getPostByID(1);
        performTest("REQ-17-2", "CRUD - Read: post retrieved by ID",
                p != null ? p.getTitle() : "null", "Original Title");

        // UPDATE
        String updateResult = pl.updatePost(1, "student1", "Updated Title", "Updated body");
        Post updated = pl.getPostByID(1);
        performTest("REQ-17-3", "CRUD - Update: title changed correctly",
                updated != null ? updated.getTitle() : "null", "Updated Title");

        // DELETE (soft delete)
        String deleteResult = pl.deletePost(1, "student1");
        Post deleted = pl.getPostByID(1);
        performTest("REQ-17-4", "CRUD - Delete: post soft-deleted (isDeleted = true)",
                deleted != null ? String.valueOf(deleted.isDeleted()) : "null", "true");
    }


    /*-******************************************************************************************

    Test Helper — same pattern as PostReplyTestingAutomation

    */

    /**********
     * <p> Method: performTest(String, String, String, String) </p>
     *
     * <p> Description: Compares actual vs expected and prints PASS or FAIL.
     * Updates running counters.  Same pattern as HW2 testing class. </p>
     *
     * @param testID        Test ID string (e.g. "REQ-01-1")
     * @param description   What is being tested
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