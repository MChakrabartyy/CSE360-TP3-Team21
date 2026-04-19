package entityClasses;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ReplyList Class. </p>
 * 
 * <p> Description: This class manages the full collection of Reply objects in the discussion
 * forum application.  Like PostList, it provides all four CRUD operations for replies, as well
 * as keyword search and a method to delete all replies belonging to a given post.
 * 
 * All input validation for replies is centralized here.  One important validation that is unique
 * to replies -- and does not exist for posts -- is checking that the postID a reply references
 * actually exists in PostList.  This prevents orphaned replies that point to deleted or
 * non-existent posts.
 * 
 * ReplyList receives a reference to PostList in its constructor so it can call postExists().
 * This is a one-way dependency: ReplyList knows about PostList, but PostList does not know
 * about ReplyList.  That direction makes sense because a reply cannot exist without a post,
 * but a post can exist without any replies. </p>
 * 
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 * 
 * @author Brishti
 * 
 * @version 1.00		2026-02-23 Initial version
 *  
 */

public class ReplyList {

	/*-********************************************************************************************

	Attributes

	*/

	// The main list that holds all Reply objects currently in the system
	private List<Reply> replies = new ArrayList<>();
	
	// Counter to assign unique reply IDs, same approach as PostList
	private int nextReplyID = 1;
	
	// Reference to PostList so we can verify a post exists before linking a reply to it
	private PostList postList;
	
	// Maximum allowed character length for a reply body
	private static final int MAX_BODY_LENGTH = 2000;


	/**********
	 * <p> Constructor: ReplyList(PostList) </p>
	 * 
	 * <p> Description: Constructs a new ReplyList with a reference to the PostList.  The
	 * PostList reference is required because ReplyList needs to call postExists() to validate
	 * that a reply's postID is real before saving the reply. </p>
	 * 
	 * @param postList	The PostList instance to use for post existence checks
	 * 
	 */
	public ReplyList(PostList postList) {
		this.postList = postList;
	}


	/*-********************************************************************************************

	CREATE

	*/

	/**********
	 * <p> Method: createReply(int, String, String) </p>
	 * 
	 * <p> Description: Validates the inputs, checks that the referenced post exists, and if
	 * everything is valid, creates a new Reply and adds it to the list.  Returns an empty string
	 * on success or a specific error message on failure.
	 * 
	 * The postID check happens before the body and author checks because if the post does not
	 * exist then the reply cannot be saved regardless of the other fields.  Failing fast on the
	 * most important constraint first makes the error messages clearer. </p>
	 * 
	 * @param postID	The ID of the post this reply is responding to
	 * @param body		The text content of the reply
	 * @param author	The username of the person writing the reply
	 * @return			Empty string if the reply was created successfully, error message if not
	 * 
	 */
	public String createReply(int postID, String body, String author) {
		
		// Check that the post being replied to actually exists -- a reply without a real post
		// is meaningless and would leave orphaned data in the system
		if (!postList.postExists(postID)) {
			return "The post this reply references does not exist.";
		}
		
		// Validate the reply body
		String bodyError = validateBody(body);
		if (!bodyError.isEmpty()) {
			return bodyError;
		}
		
		// Validate the author
		if (author == null || author.trim().isEmpty()) {
			return "Author is required.";
		}
		
		// All checks passed, create the reply
		Reply newReply = new Reply(nextReplyID, postID, body, author);
		replies.add(newReply);
		
		System.out.println("** Reply created: ID=" + nextReplyID + ", PostID=" + postID + 
				", Author='" + author + "'");
		
		nextReplyID++;
		
		return "";
	}


	/*-********************************************************************************************

	READ

	*/

	/**********
	 * <p> Method: getRepliesForPost(int) </p>
	 * 
	 * <p> Description: Returns a list of all replies that belong to the given post ID.  Returns
	 * an empty list (never null) if no replies exist for that post, so the caller can always
	 * iterate the result safely without a null check.
	 * 
	 * If the list is empty, the caller is expected to display "No replies for this post yet."
	 * </p>
	 * 
	 * @param postID	The ID of the post whose replies should be returned
	 * @return			A List of Reply objects for that post (may be empty, never null)
	 * 
	 */
	public List<Reply> getRepliesForPost(int postID) {
		List<Reply> result = new ArrayList<>();
		for (Reply r : replies) {
			if (r.getPostID() == postID) {
				result.add(r);
			}
		}
		return result;
	}

	/**********
	 * <p> Method: getReplyByID(int) </p>
	 * 
	 * <p> Description: Searches the list for a reply with the given ID and returns it.
	 * Returns null if no reply with that ID exists. </p>
	 * 
	 * @param replyID	The integer ID of the reply to find
	 * @return			The matching Reply object, or null if not found
	 * 
	 */
	public Reply getReplyByID(int replyID) {
		for (Reply r : replies) {
			if (r.getReplyID() == replyID) {
				return r;
			}
		}
		return null;
	}

	/**********
	 * <p> Method: searchReplies(String) </p>
	 * 
	 * <p> Description: Returns a subset of replies whose body contains the given keyword.
	 * The search is case-insensitive.  Returns an empty list (never null) if no matches are
	 * found. </p>
	 * 
	 * @param keyword	The keyword string to search for in reply bodies
	 * @return			A List of matching Reply objects (may be empty, never null)
	 * 
	 */
	public List<Reply> searchReplies(String keyword) {
		List<Reply> results = new ArrayList<>();
		
		if (keyword == null || keyword.trim().isEmpty()) {
			System.out.println("** Search keyword cannot be empty.");
			return results;
		}
		
		String lowerKeyword = keyword.toLowerCase();
		
		for (Reply r : replies) {
			if (r.getBody().toLowerCase().contains(lowerKeyword)) {
				results.add(r);
			}
		}
		
		return results;
	}

	/**********
	 * <p> Method: getAllReplies() </p>
	 * 
	 * <p> Description: Returns the full list of all replies.  Mainly used in testing to verify
	 * that deleting a post also removed its replies. </p>
	 * 
	 * @return	A List of all Reply objects currently stored
	 * 
	 */
	public List<Reply> getAllReplies() {
		return replies;
	}


	/*-********************************************************************************************

	UPDATE

	*/

	/**********
	 * <p> Method: updateReply(int, String) </p>
	 * 
	 * <p> Description: Finds the reply with the given ID, validates the new body, and updates
	 * it if everything is valid.  Returns an empty string on success or an error message on
	 * failure.
	 * 
	 * Only the body is updatable.  We do not allow changing the postID or author after creation
	 * because that would alter the identity of the reply in a misleading way. </p>
	 * 
	 * @param replyID	The ID of the reply to update
	 * @param newBody	The new body text to replace the existing one
	 * @return			Empty string on success, error message on failure
	 * 
	 */
	public String updateReply(int replyID, String newBody) {
		
		Reply reply = getReplyByID(replyID);
		if (reply == null) {
			return "Reply not found.";
		}
		
		String bodyError = validateBody(newBody);
		if (!bodyError.isEmpty()) {
			return bodyError;
		}
		
		reply.setBody(newBody);
		
		System.out.println("** Reply updated: ID=" + replyID);
		
		return "";
	}


	/*-********************************************************************************************

	DELETE

	*/

	/**********
	 * <p> Method: deleteReply(int) </p>
	 * 
	 * <p> Description: Removes the reply with the given ID from the list.  Returns an empty
	 * string on success or "Reply not found." if no reply with that ID exists. </p>
	 * 
	 * @param replyID	The ID of the reply to delete
	 * @return			Empty string on success, error message on failure
	 * 
	 */
	public String deleteReply(int replyID) {
		Reply reply = getReplyByID(replyID);
		if (reply == null) {
			return "Reply not found.";
		}
		
		replies.remove(reply);
		
		System.out.println("** Reply deleted: ID=" + replyID);
		
		return "";
	}

	/**********
	 * <p> Method: deleteRepliesForPost(int) </p>
	 * 
	 * <p> Description: Removes all replies that belong to the given post ID.  This is called
	 * whenever a post is deleted, to clean up the associated replies.  Without this, deleting
	 * a post would leave orphaned replies in the list that reference a post that no longer
	 * exists.
	 * 
	 * We build a separate removal list first rather than removing from replies while iterating
	 * it, because modifying a list during iteration causes a ConcurrentModificationException
	 * in Java. </p>
	 * 
	 * @param postID	The ID of the post whose replies should all be deleted
	 * 
	 */
	public void deleteRepliesForPost(int postID) {
		// Collect the replies to remove first, then remove them all at once
		List<Reply> toRemove = new ArrayList<>();
		for (Reply r : replies) {
			if (r.getPostID() == postID) {
				toRemove.add(r);
			}
		}
		replies.removeAll(toRemove);
		
		System.out.println("** Deleted " + toRemove.size() + " replies for Post ID=" + postID);
	}

	/**********
	 * <p> Method: getSize() </p>
	 * 
	 * <p> Description: Returns the total number of replies currently stored.  Useful in tests
	 * to verify a reply was added or removed. </p>
	 * 
	 * @return	The integer count of all replies in the list
	 * 
	 */
	public int getSize() {
		return replies.size();
	}


	/*-********************************************************************************************

	Private Validation Helper

	*/

	/**********
	 * <p> Method: validateBody(String) </p>
	 * 
	 * <p> Description: Checks that the reply body is not null, not blank, and not too long.
	 * Returns an empty string if valid, or a specific error message if not. </p>
	 * 
	 * @param body		The body string to validate
	 * @return			Empty string if valid, error message if not
	 * 
	 */
	private String validateBody(String body) {
		if (body == null || body.trim().isEmpty()) {
			return "Reply body is required.";
		}
		if (body.length() > MAX_BODY_LENGTH) {
			return "Reply must be 2000 characters or fewer.";
		}
		return "";
	}
}

