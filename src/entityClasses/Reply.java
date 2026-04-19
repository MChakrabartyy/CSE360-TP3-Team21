package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*******
 * <p> Title: Reply Class. </p>
 * 
 * <p> Description: This class represents a single reply to a post in the discussion forum.
 * A reply is a response to an existing post, identified by the post's ID.  Each reply stores
 * a unique reply ID, the ID of the post it belongs to, the body text of the reply, the
 * author's username, and a timestamp.
 * 
 * Replies do not have titles -- only a body -- because a reply is a direct response rather
 * than a new topic.  This mirrors how Ed Discussion structures replies: no title, just content.
 * 
 * This class is used by ReplyList to store and manage all replies in the system. </p>
 * 
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 * 
 * @author Brishti
 * 
 * @version 1.00		2026-02-23 Initial version
 *  
 */

public class Reply {

	/*-********************************************************************************************

	The attributes that define a Reply object.
	
	Each reply has a unique integer ID (replyID), a reference to the post it responds to
	(postID), the body text, the author's username, and a timestamp.  No title field exists
	because replies are always tied to a specific post and do not need their own topic label.
	
	*/

	// Unique identifier for this reply, assigned by ReplyList at creation time
	private int replyID;
	
	// The ID of the post that this reply belongs to -- used to group replies by post
	private int postID;
	
	// Full text content of the reply (1 to 2000 characters)
	private String body;
	
	// Username of the person who wrote this reply (cannot be empty)
	private String author;
	
	// Timestamp automatically set when the reply is created
	private String timestamp;
	
	// Same formatter as Post -- keeps the timestamp format consistent across the system
	private static final DateTimeFormatter FORMATTER =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	
	/**********
	 * <p> Constructor: Reply(int, int, String, String) </p>
	 * 
	 * <p> Description: Constructs a new Reply object with the given reply ID, post ID, body,
	 * and author.  The timestamp is automatically set to the current date and time.  Validation
	 * is handled by ReplyList before this constructor is called -- this class only stores data.
	 * </p>
	 * 
	 * @param replyID	The unique integer identifier assigned by ReplyList
	 * @param postID	The ID of the post this reply is responding to
	 * @param body		The text content of the reply
	 * @param author	The username of the person writing the reply
	 * 
	 */
	public Reply(int replyID, int postID, String body, String author) {
		this.replyID   = replyID;
		this.postID    = postID;
		this.body      = body;
		this.author    = author;
		
		// Set the timestamp at construction time, same approach as in the Post class
		this.timestamp = LocalDateTime.now().format(FORMATTER);
	}

	
	/*-********************************************************************************************

	Getters and Setters
	
	Only the body has a setter because that is the only field a user is allowed to edit after
	a reply is created.  replyID, postID, author, and timestamp are fixed at creation and must
	never change.
	
	*/

	/**********
	 * <p> Method: getReplyID() </p>
	 * 
	 * <p> Description: Returns the unique integer ID of this reply. </p>
	 * 
	 * @return	The reply's integer ID
	 * 
	 */
	public int getReplyID() {
		return replyID;
	}

	/**********
	 * <p> Method: getPostID() </p>
	 * 
	 * <p> Description: Returns the ID of the post this reply belongs to.  This is used by
	 * ReplyList to filter replies when displaying all replies for a specific post. </p>
	 * 
	 * @return	The post ID integer this reply is linked to
	 * 
	 */
	public int getPostID() {
		return postID;
	}

	/**********
	 * <p> Method: getBody() </p>
	 * 
	 * <p> Description: Returns the body text of this reply. </p>
	 * 
	 * @return	The reply body string
	 * 
	 */
	public String getBody() {
		return body;
	}

	/**********
	 * <p> Method: setBody(String) </p>
	 * 
	 * <p> Description: Updates the body of this reply.  Called by ReplyList after validating
	 * that the new body is not empty and is within the length limit. </p>
	 * 
	 * @param body		The new body string to replace the existing one
	 * 
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**********
	 * <p> Method: getAuthor() </p>
	 * 
	 * <p> Description: Returns the username of the person who wrote this reply. </p>
	 * 
	 * @return	The author's username string
	 * 
	 */
	public String getAuthor() {
		return author;
	}

	/**********
	 * <p> Method: getTimestamp() </p>
	 * 
	 * <p> Description: Returns the timestamp string recording when this reply was created. </p>
	 * 
	 * @return	The formatted timestamp string
	 * 
	 */
	public String getTimestamp() {
		return timestamp;
	}

	
	/**********
	 * <p> Method: toString() </p>
	 * 
	 * <p> Description: Returns a formatted string representation of this reply.  Includes the
	 * post ID so that when replies are printed during testing it is always clear which post
	 * each reply belongs to. </p>
	 * 
	 * @return	A formatted multi-line string describing the reply
	 * 
	 */
	@Override
	public String toString() {
		return "Reply ID: " + replyID + "\n" +
			   "Post ID:  " + postID  + "\n" +
			   "Author:   " + author  + "\n" +
			   "Posted:   " + timestamp + "\n" +
			   "Body:     " + body;
	}
}
