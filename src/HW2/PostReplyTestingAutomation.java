package HW2;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;
import java.util.List;

/*******
 * <p> Title: PostReplyTestingAutomation Class. </p>
 * 
 * <p> Description: This class runs automated tests for the Post, Reply, PostList, and ReplyList
 * classes.  Each test maps directly to a test case in Test Cases.pdf.  The test IDs in the
 * output (e.g., TC-P01-1) match exactly so the grader can cross-reference the results.
 * 
 * The structure of this class follows the same pattern used in the TP1 testing classes
 * (PasswordEvaluationTestingAutomation, UserNameRecognizerTestingAutomation).  Each test
 * calls a helper method that prints a pass/fail result and tracks a running count of failures.
 * 
 * Tests are organized by section to match Test Cases.pdf:
 *   Section 1 -- Post CRUD (TC-P01 through TC-P05)
 *   Section 2 -- Reply CRUD (TC-R01 through TC-R05)
 *   Section 3 -- List and Search (TC-L01 through TC-L04)
 *
 * <p><b>TP2 Update:</b> Method signatures updated to match the TP2 PostList API:
 * createPost() now takes 6 parameters (added threadName, postType, parentPostId),
 * updatePost() now takes 4 parameters (added requestingUser),
 * deletePost() now takes 2 parameters (added requestingUser),
 * searchPosts() now takes 2 parameters (added threadName filter). </p>
 * </p>
 * 
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 * 
 * @author Brishti
 * 
 * @version 1.00    2026-02-23 Initial version
 * @version 1.01    2026-03-20 Updated method calls to match TP2 PostList signatures
 *  
 */

public class PostReplyTestingAutomation {

	// Running count of tests that produced the wrong result
	private static int numFailed = 0;
	
	// Running count of all tests attempted
	private static int numPassed = 0;


	/**********
	 * <p> Method: main(String[]) </p>
	 * 
	 * <p> Description: Entry point.  Runs all test cases in the order they appear in
	 * Test Cases.pdf, then prints a summary. </p>
	 * 
	 * @param args  Command-line arguments (not used)
	 * 
	 */
	public static void main(String[] args) {
		
		System.out.println("=============================================================");
		System.out.println("  PostReplyTestingAutomation -- HW2 Automated Tests");
		System.out.println("=============================================================\n");
		
		runPostCRUDTests();
		runReplyCRUDTests();
		runListSearchTests();
		
		System.out.println("\n=============================================================");
		System.out.println("  Results: " + numPassed + " passed, " + numFailed + " failed");
		System.out.println("=============================================================");
	}


	/*-********************************************************************************************

	SECTION 1 -- POST CRUD TESTS

	*/

	private static void runPostCRUDTests() {
		System.out.println("--- Section 1: Post CRUD ---\n");
		
		testCreatePostPositive();
		testCreatePostEmptyTitle();
		testCreatePostEmptyBody();
		testCreatePostEmptyAuthor();
		testCreatePostTitleTooLong();
		testCreatePostBodyTooLong();
		testReadAllPosts();
		testReadAllPostsEmpty();
		testReadSinglePostFound();
		testReadSinglePostNotFound();
		testUpdatePostPositive();
		testUpdatePostNotFound();
		testUpdatePostEmptyTitle();
		testUpdatePostEmptyBody();
		testDeletePostPositive();
		testDeletePostNotFound();
	}

	// TC-P01-1: Create a post with all valid inputs
	// TP2 note: createPost() now requires threadName, postType, parentPostId
	// Passing "General", "question", -1 as defaults to match TP2 API
	private static void testCreatePostPositive() {
		PostList pl = new PostList();
		String result = pl.createPost("How do I sort in Java?", 
				"Need help with Arrays.sort()", "jdoe", "General", "question", -1);
		performTestCase("TC-P01-1", "Create post with valid inputs", result, "");
	}

	// TC-P01-2: Create a post with an empty title
	private static void testCreatePostEmptyTitle() {
		PostList pl = new PostList();
		String result = pl.createPost("", "Some body", "jdoe", "General", "question", -1);
		performTestCase("TC-P01-2", "Create post with empty title", result, "Post title is required.");
	}

	// TC-P01-3: Create a post with an empty body
	private static void testCreatePostEmptyBody() {
		PostList pl = new PostList();
		String result = pl.createPost("Valid Title", "", "jdoe", "General", "question", -1);
		performTestCase("TC-P01-3", "Create post with empty body", result, "Post body is required.");
	}

	// TC-P01-4: Create a post with an empty author
	private static void testCreatePostEmptyAuthor() {
		PostList pl = new PostList();
		String result = pl.createPost("Valid Title", "Valid body", "", "General", "question", -1);
		performTestCase("TC-P01-4", "Create post with empty author", result, "Author is required.");
	}

	// TC-P01-5: Create a post with a title that exceeds 200 characters
	private static void testCreatePostTitleTooLong() {
		PostList pl = new PostList();
		String longTitle = "A".repeat(201);
		String result = pl.createPost(longTitle, "Valid body", "jdoe", "General", "question", -1);
		performTestCase("TC-P01-5", "Create post with title over 200 chars", result, 
				"Title must be 200 characters or fewer.");
	}

	// TC-P01-6: Create a post with a body that exceeds 2000 characters
	private static void testCreatePostBodyTooLong() {
		PostList pl = new PostList();
		String longBody = "B".repeat(2001);
		String result = pl.createPost("Valid Title", longBody, "jdoe", "General", "question", -1);
		performTestCase("TC-P01-6", "Create post with body over 2000 chars", result,
				"Body must be 2000 characters or fewer.");
	}

	// TC-P02-1: Read all posts when the list has posts
	private static void testReadAllPosts() {
		PostList pl = new PostList();
		pl.createPost("Post One",   "Body one",   "alice", "General", "statement", -1);
		pl.createPost("Post Two",   "Body two",   "bob",   "General", "question",  -1);
		pl.createPost("Post Three", "Body three", "carol", "General", "statement", -1);
		
		List<Post> all = pl.getAllPosts();
		boolean passed = (all.size() == 3);
		performTestCase("TC-P02-1", "Read all posts (3 exist)", 
				passed ? "" : "Expected 3 posts, got " + all.size(), "");
	}

	// TC-P02-2: Read all posts when the list is empty
	private static void testReadAllPostsEmpty() {
		PostList pl = new PostList();
		List<Post> all = pl.getAllPosts();
		performTestCase("TC-P02-2", "Read all posts (list is empty)", 
				all.isEmpty() ? "" : "Expected empty list", "");
	}

	// TC-P03-1: Read a single post that exists
	private static void testReadSinglePostFound() {
		PostList pl = new PostList();
		pl.createPost("Java Sorting", "How does sort work?", "jdoe", "General", "question", -1);
		Post found = pl.getPostByID(1);
		boolean passed = (found != null && found.getTitle().equals("Java Sorting"));
		performTestCase("TC-P03-1", "Read single post by valid ID", 
				passed ? "" : "Post not found or wrong title", "");
	}

	// TC-P03-2: Read a single post with an ID that does not exist
	private static void testReadSinglePostNotFound() {
		PostList pl = new PostList();
		Post found = pl.getPostByID(9999);
		performTestCase("TC-P03-2", "Read single post by invalid ID (9999)", 
				found == null ? "" : "Expected null but got a post", "");
	}

	// TC-P04-1: Update a post with valid new title and body
	// TP2 note: updatePost() now requires requestingUser as second parameter
	private static void testUpdatePostPositive() {
		PostList pl = new PostList();
		pl.createPost("Old Title", "Old body", "jdoe", "General", "question", -1);
		// Pass "jdoe" as the requesting user — must match the author for update to succeed
		String result = pl.updatePost(1, "jdoe", "Updated Title", "Updated body");
		Post updated = pl.getPostByID(1);
		boolean correct = result.isEmpty() && updated != null 
				&& updated.getTitle().equals("Updated Title");
		performTestCase("TC-P04-1", "Update post with valid inputs", 
				correct ? "" : "Title was not updated correctly", "");
	}

	// TC-P04-2: Update a post that does not exist
	private static void testUpdatePostNotFound() {
		PostList pl = new PostList();
		String result = pl.updatePost(9999, "jdoe", "New Title", "New body");
		performTestCase("TC-P04-2", "Update post that does not exist", result, "Post not found.");
	}

	// TC-P04-3: Update a post with an empty title
	private static void testUpdatePostEmptyTitle() {
		PostList pl = new PostList();
		pl.createPost("Original Title", "Original body", "jdoe", "General", "question", -1);
		String result = pl.updatePost(1, "jdoe", "", "New body");
		performTestCase("TC-P04-3", "Update post with empty title", result, "Post title is required.");
	}

	// TC-P04-4: Update a post with an empty body
	private static void testUpdatePostEmptyBody() {
		PostList pl = new PostList();
		pl.createPost("Original Title", "Original body", "jdoe", "General", "question", -1);
		String result = pl.updatePost(1, "jdoe", "New Title", "");
		performTestCase("TC-P04-4", "Update post with empty body", result, "Post body is required.");
	}

	// TC-P05-1: Delete a post — TP2 uses soft delete, so replies stay in list
	// TP2 note: deletePost() now requires requestingUser as second parameter
	// TP2 note: soft delete means the post stays in list with isDeleted=true
	//           so we check getActiveSize() instead of list size
	private static void testDeletePostPositive() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Post to Delete", "Some body", "jdoe", "General", "question", -1);
		rl.createReply(1, "Reply one", "alice");
		rl.createReply(1, "Reply two", "bob");
		
		// Soft-delete the post — pass "jdoe" as the author performing the delete
		String result = pl.deletePost(1, "jdoe");
		
		// TP2: replies are NOT deleted — they survive the soft delete (US-S6)
		int repliesAfter = rl.getRepliesForPost(1).size();
		
		// Post is soft-deleted so getActiveSize() should be 0, but getSize() still 1
		boolean correct = result.isEmpty() 
				&& pl.getPostByID(1).isDeleted()
				&& repliesAfter == 2;  // replies survive per US-S6
		performTestCase("TC-P05-1", "Soft-delete post and verify replies preserved (US-S6)",
				correct ? "" : "Soft delete failed or replies were incorrectly removed", "");
	}

	// TC-P05-2: Delete a post that does not exist
	private static void testDeletePostNotFound() {
		PostList pl = new PostList();
		String result = pl.deletePost(9999, "jdoe");
		performTestCase("TC-P05-2", "Delete post that does not exist", result, "Post not found.");
	}


	/*-********************************************************************************************

	SECTION 2 -- REPLY CRUD TESTS

	*/

	private static void runReplyCRUDTests() {
		System.out.println("\n--- Section 2: Reply CRUD ---\n");
		
		testCreateReplyPositive();
		testCreateReplyPostNotFound();
		testCreateReplyEmptyBody();
		testCreateReplyEmptyAuthor();
		testCreateReplyBodyTooLong();
		testReadRepliesForPost();
		testReadRepliesForPostEmpty();
		testReadSingleReplyFound();
		testReadSingleReplyNotFound();
		testUpdateReplyPositive();
		testUpdateReplyNotFound();
		testUpdateReplyEmptyBody();
		testDeleteReplyPositive();
		testDeleteReplyNotFound();
	}

	// TC-R01-1: Create a reply with valid inputs
	private static void testCreateReplyPositive() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How does sort work?", "jdoe", "General", "question", -1);
		String result = rl.createReply(1, "Use Arrays.sort(arr) for primitives.", "profSmith");
		performTestCase("TC-R01-1", "Create reply with valid inputs", result, "");
	}

	// TC-R01-2: Create a reply referencing a post ID that does not exist
	private static void testCreateReplyPostNotFound() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		String result = rl.createReply(9999, "Valid body", "jdoe");
		performTestCase("TC-R01-2", "Create reply with non-existent post ID", result,
				"The post this reply references does not exist.");
	}

	// TC-R01-3: Create a reply with an empty body
	private static void testCreateReplyEmptyBody() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How does sort work?", "jdoe", "General", "question", -1);
		String result = rl.createReply(1, "", "jdoe");
		performTestCase("TC-R01-3", "Create reply with empty body", result, "Reply body is required.");
	}

	// TC-R01-4: Create a reply with an empty author
	private static void testCreateReplyEmptyAuthor() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How does sort work?", "jdoe", "General", "question", -1);
		String result = rl.createReply(1, "Valid body", "");
		performTestCase("TC-R01-4", "Create reply with empty author", result, "Author is required.");
	}

	// TC-R01-5: Create a reply with a body over 2000 characters
	private static void testCreateReplyBodyTooLong() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How does sort work?", "jdoe", "General", "question", -1);
		String longBody = "C".repeat(2001);
		String result = rl.createReply(1, longBody, "jdoe");
		performTestCase("TC-R01-5", "Create reply with body over 2000 chars", result,
				"Reply must be 2000 characters or fewer.");
	}

	// TC-R02-1: Get all replies for a post that has replies
	private static void testReadRepliesForPost() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How does sort work?", "jdoe", "General", "question", -1);
		rl.createReply(1, "Use Arrays.sort()", "profSmith");
		rl.createReply(1, "You can also use Collections.sort()", "alice");
		rl.createReply(1, "Check the Java docs for details", "bob");
		
		List<Reply> result = rl.getRepliesForPost(1);
		boolean passed = (result.size() == 3);
		performTestCase("TC-R02-1", "Read all replies for post with 3 replies",
				passed ? "" : "Expected 3 replies, got " + result.size(), "");
	}

	// TC-R02-2: Get all replies for a post that has no replies
	private static void testReadRepliesForPostEmpty() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Unanswered Post", "Body here", "jdoe", "General", "question", -1);
		List<Reply> result = rl.getRepliesForPost(1);
		performTestCase("TC-R02-2", "Read replies for post with no replies",
				result.isEmpty() ? "" : "Expected empty list", "");
	}

	// TC-R03-1: Read a single reply that exists
	private static void testReadSingleReplyFound() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("A Post", "Post body", "jdoe", "General", "question", -1);
		rl.createReply(1, "This is the answer.", "alice");
		Reply found = rl.getReplyByID(1);
		boolean passed = (found != null && found.getBody().equals("This is the answer."));
		performTestCase("TC-R03-1", "Read single reply by valid ID",
				passed ? "" : "Reply not found or wrong body", "");
	}

	// TC-R03-2: Read a single reply with an ID that does not exist
	private static void testReadSingleReplyNotFound() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		Reply found = rl.getReplyByID(9999);
		performTestCase("TC-R03-2", "Read single reply by invalid ID (9999)",
				found == null ? "" : "Expected null but got a reply", "");
	}

	// TC-R04-1: Update a reply with a valid new body
	private static void testUpdateReplyPositive() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("A Post", "Post body", "jdoe", "General", "question", -1);
		rl.createReply(1, "Original answer.", "alice");
		String result = rl.updateReply(1, "Updated answer here.");
		Reply updated = rl.getReplyByID(1);
		boolean correct = result.isEmpty() && updated != null && 
				updated.getBody().equals("Updated answer here.");
		performTestCase("TC-R04-1", "Update reply with valid new body",
				correct ? "" : "Reply body was not updated correctly", "");
	}

	// TC-R04-2: Update a reply that does not exist
	private static void testUpdateReplyNotFound() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		String result = rl.updateReply(9999, "New body");
		performTestCase("TC-R04-2", "Update reply that does not exist", result, "Reply not found.");
	}

	// TC-R04-3: Update a reply with an empty body
	private static void testUpdateReplyEmptyBody() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("A Post", "Post body", "jdoe", "General", "question", -1);
		rl.createReply(1, "Original answer.", "alice");
		String result = rl.updateReply(1, "");
		performTestCase("TC-R04-3", "Update reply with empty body", result, "Reply body is required.");
	}

	// TC-R05-1: Delete a reply that exists
	private static void testDeleteReplyPositive() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("A Post", "Post body", "jdoe", "General", "question", -1);
		rl.createReply(1, "Reply one", "alice");
		rl.createReply(1, "Reply two", "bob");
		
		String result = rl.deleteReply(1);
		List<Reply> remaining = rl.getRepliesForPost(1);
		boolean correct = result.isEmpty() && remaining.size() == 1;
		performTestCase("TC-R05-1", "Delete reply and verify other reply unaffected",
				correct ? "" : "Delete failed or wrong reply count", "");
	}

	// TC-R05-2: Delete a reply that does not exist
	private static void testDeleteReplyNotFound() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		String result = rl.deleteReply(9999);
		performTestCase("TC-R05-2", "Delete reply that does not exist", result, "Reply not found.");
	}


	/*-********************************************************************************************

	SECTION 3 -- LIST AND SEARCH TESTS

	*/

	private static void runListSearchTests() {
		System.out.println("\n--- Section 3: List and Search ---\n");
		
		testSearchPostsKeywordMatch();
		testSearchPostsNoMatch();
		testSearchPostsEmptyKeyword();
		testSearchRepliesKeywordMatch();
		testSearchRepliesNoMatch();
		testPostListEmptyState();
		testReplyListEmptyState();
	}

	// TC-L01-1: Search posts with a keyword that matches
	// TP2 note: searchPosts() now takes (keyword, threadName) — pass null for all threads
	private static void testSearchPostsKeywordMatch() {
		PostList pl = new PostList();
		pl.createPost("How do I sort in Java?", 
				"I need help with Arrays.sort()", "jdoe", "General", "question", -1);
		pl.createPost("Python list comprehensions", 
				"Can someone explain list comps?", "alice", "General", "question", -1);
		
		// null threadName = search all threads (US-S7)
		List<Post> results = pl.searchPosts("java", null);
		boolean passed = (results.size() == 1 && results.get(0).getTitle().contains("Java"));
		performTestCase("TC-L01-1", "Search posts by keyword 'java' (case-insensitive)",
				passed ? "" : "Expected 1 result matching 'java', got " + results.size(), "");
	}

	// TC-L01-2: Search posts with a keyword that matches nothing
	private static void testSearchPostsNoMatch() {
		PostList pl = new PostList();
		pl.createPost("How do I sort in Java?", "Arrays.sort question", 
				"jdoe", "General", "question", -1);
		List<Post> results = pl.searchPosts("zzznomatch", null);
		performTestCase("TC-L01-2", "Search posts with keyword that matches nothing",
				results.isEmpty() ? "" : "Expected empty list, got " + results.size(), "");
	}

	// TC-L01-3: Search posts with an empty keyword
	private static void testSearchPostsEmptyKeyword() {
		PostList pl = new PostList();
		pl.createPost("Some Post", "Some body", "jdoe", "General", "question", -1);
		List<Post> results = pl.searchPosts("", null);
		performTestCase("TC-L01-3", "Search posts with empty keyword",
				results.isEmpty() ? "" : "Expected empty list for empty keyword", "");
	}

	// TC-L02-1: Search replies with a keyword that matches
	private static void testSearchRepliesKeywordMatch() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How do I sort?", "jdoe", "General", "question", -1);
		rl.createReply(1, "Use Arrays.sort() for primitives.", "profSmith");
		rl.createReply(1, "You can also use a for loop.", "alice");
		
		List<Reply> results = rl.searchReplies("Arrays");
		boolean passed = (results.size() == 1 && results.get(0).getBody().contains("Arrays"));
		performTestCase("TC-L02-1", "Search replies by keyword 'Arrays'",
				passed ? "" : "Expected 1 result matching 'Arrays', got " + results.size(), "");
	}

	// TC-L02-2: Search replies with a keyword that matches nothing
	private static void testSearchRepliesNoMatch() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Java Sorting", "How do I sort?", "jdoe", "General", "question", -1);
		rl.createReply(1, "Use Arrays.sort()", "alice");
		List<Reply> results = rl.searchReplies("zzznomatch");
		performTestCase("TC-L02-2", "Search replies with keyword that matches nothing",
				results.isEmpty() ? "" : "Expected empty list, got " + results.size(), "");
	}

	// TC-L03-1: PostList empty state
	private static void testPostListEmptyState() {
		PostList pl = new PostList();
		List<Post> all = pl.getAllPosts();
		performTestCase("TC-L03-1", "PostList empty state returns empty list",
				all.isEmpty() ? "" : "Expected empty list", "");
	}

	// TC-L04-1: ReplyList empty state for a post with no replies
	private static void testReplyListEmptyState() {
		PostList pl = new PostList();
		ReplyList rl = new ReplyList(pl);
		pl.createPost("Post With No Replies", "Asking a question", 
				"jdoe", "General", "question", -1);
		List<Reply> replies = rl.getRepliesForPost(1);
		performTestCase("TC-L04-1", "ReplyList empty state for post with no replies",
				replies.isEmpty() ? "" : "Expected empty list", "");
	}


	/*-********************************************************************************************

	Test Helper

	*/

	/**********
	 * <p> Method: performTestCase(String, String, String, String) </p>
	 * 
	 * <p> Description: Compares actual vs expected result and prints PASS or FAIL.
	 * Updates running counters. </p>
	 * 
	 * @param testID        The test case ID (e.g., "TC-P01-1")
	 * @param description   Brief description of what is being tested
	 * @param actual        Actual result returned by the method under test
	 * @param expected      Expected result from Test Cases.pdf
	 * 
	 */
	private static void performTestCase(String testID, String description, 
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
