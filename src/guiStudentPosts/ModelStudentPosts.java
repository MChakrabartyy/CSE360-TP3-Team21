package guiStudentPosts;

import entityClasses.Post;
import entityClasses.PostList;
import entityClasses.Reply;
import entityClasses.ReplyList;

import java.util.List;

/*******
 * <p> Title: ModelStudentPosts Class. </p>
 *
 * <p> Description: This class serves as the Model layer of the MVC architecture for the
 * Student Posts GUI package.  It holds the shared {@link PostList} and {@link ReplyList}
 * instances that are used across all student-facing views, and exposes all data-access
 * operations needed by {@link ControllerStudentPosts}.
 *
 * <p> Following the pattern established by the {@code guiUserLogin.Model} stub and the
 * existing codebase, this class is a singleton-style object instantiated once by
 * {@link ViewStudentPosts} and passed to the controller.  No JavaFX imports are needed
 * here — the Model knows nothing about the UI. </p>
 *
 * <p><b>Student User Stories supported:</b></p>
 * <ul>
 *   <li><b>US-S1</b> – createPost() and createReply() let students post and reply.</li>
 *   <li><b>US-S2</b> – getAllPosts() returns all posts for browsing.</li>
 *   <li><b>US-S3</b> – getMyPosts() and getMyPostsUnreadOnly() support the "my posts" view.</li>
 *   <li><b>US-S4</b> – searchPosts() supports keyword search with read/unread status.</li>
 *   <li><b>US-S5</b> – createPost() defaults thread to "General".</li>
 *   <li><b>US-S6</b> – deletePost() performs soft delete; replies survive.</li>
 *   <li><b>US-S7</b> – searchPosts(keyword, thread) searches one or all threads.</li>
 * </ul>
 *
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-03-19  Initial version
 */
public class ModelStudentPosts {

    /*-******************************************************************************************

    Attributes

    */

    /**
     * The master list of all posts.  Shared across all views in this package.
     * Source: PostList class from entityClasses; required by all Student User Stories.
     */
    private PostList postList;

    /**
     * The master list of all replies.  Requires a reference to postList to validate
     * that replies link to real posts.
     * Source: ReplyList class from entityClasses; required by US-S1.
     */
    private ReplyList replyList;

    /**
     * The username of the currently logged-in student.  Set when the student home page
     * launches this GUI.  Used to filter "my posts" and to enforce author-only
     * delete/edit rules.
     * Source: US-S3 (my posts), US-S6 (only author can delete).
     */
    private String currentUser;


    /*-******************************************************************************************

    Constructor

    */

    /**********
     * <p> Constructor: ModelStudentPosts(String) </p>
     *
     * <p> Description: Initialises the PostList and ReplyList and stores the current user.
     * Called once by {@link ViewStudentPosts} when the student home screen is launched. </p>
     *
     * @param currentUser   The username of the logged-in student
     */
    public ModelStudentPosts(String currentUser) {
        this.postList    = new PostList();
        this.replyList   = new ReplyList(postList);
        this.currentUser = currentUser;
    }


    /*-******************************************************************************************

    Post operations — delegate to PostList

    */

    /**
     * Creates a new post.  Delegates validation and storage to {@link PostList}.
     * Satisfies <b>US-S1</b> and <b>US-S5</b>.
     *
     * @param title         Post title
     * @param body          Post body
     * @param threadName    Thread name; defaults to "General" if blank
     * @param postType      "question" or "statement"
     * @return              Empty string on success, error message on failure
     */
    public String createPost(String title, String body, String threadName, String postType) {
        // US-S1: author is always the current logged-in student
        return postList.createPost(title, body, currentUser, threadName, postType, -1);
    }

    /**
     * Creates a reply to an existing post.  Satisfies <b>US-S1</b>.
     *
     * @param postID    ID of the post being replied to
     * @param body      Reply body text
     * @return          Empty string on success, error message on failure
     */
    public String createReply(int postID, String body) {
        String result = replyList.createReply(postID, body, currentUser);
        // US-S3: update reply count on the parent post when a reply is added
        if (result.isEmpty()) {
            postList.incrementReplyCount(postID);
        }
        return result;
    }

    /**
     * Returns all posts (including soft-deleted, so view can show placeholder).
     * Satisfies <b>US-S2</b>.
     *
     * @return List of all Post objects
     */
    public List<Post> getAllPosts() {
        return postList.getAllPosts();
    }

    /**
     * Returns all posts authored by the current user with reply counts.
     * Satisfies <b>US-S3</b>.
     *
     * @return List of Post objects by the current user
     */
    public List<Post> getMyPosts() {
        return postList.getMyPosts(currentUser);
    }

    /**
     * Returns only the current user's posts that have unread replies.
     * Satisfies <b>US-S3</b> ("just the unread").
     *
     * @return List of Post objects with unread replies
     */
    public List<Post> getMyPostsUnreadOnly() {
        return postList.getMyPostsUnreadOnly(currentUser);
    }

    /**
     * Searches posts by keyword across all threads or within a specific thread.
     * Satisfies <b>US-S4</b> and <b>US-S7</b>.
     *
     * @param keyword       Search term
     * @param threadName    Thread to search, or null for all threads
     * @return              List of matching Post objects
     */
    public List<Post> searchPosts(String keyword, String threadName) {
        return postList.searchPosts(keyword, threadName);
    }

    /**
     * Returns a single post by ID, or null if not found.
     *
     * @param postID    The post ID to retrieve
     * @return          The Post, or null
     */
    public Post getPostByID(int postID) {
        return postList.getPostByID(postID);
    }

    /**
     * Updates a post's title and body.  Only the author can update.
     *
     * @param postID    ID of the post to update
     * @param newTitle  New title
     * @param newBody   New body
     * @return          Empty string on success, error message on failure
     */
    public String updatePost(int postID, String newTitle, String newBody) {
        return postList.updatePost(postID, currentUser, newTitle, newBody);
    }

    /**
     * Soft-deletes a post.  Only the author can delete.  Replies survive.
     * Satisfies <b>US-S6</b>.
     *
     * @param postID    ID of the post to delete
     * @return          Empty string on success, error message on failure
     */
    public String deletePost(int postID) {
        return postList.deletePost(postID, currentUser);
    }

    /**
     * Marks a post as read by the current user.  Satisfies <b>US-S4</b>.
     *
     * @param postID    ID of the post to mark as read
     */
    public void markPostAsRead(int postID) {
        postList.markPostAsRead(postID);
    }

    /**
     * Returns all replies for a given post.  Satisfies <b>US-S1</b>.
     *
     * @param postID    The post whose replies to retrieve
     * @return          List of Reply objects
     */
    public List<Reply> getRepliesForPost(int postID) {
        return replyList.getRepliesForPost(postID);
    }

    /**
     * Returns the username of the currently logged-in student.
     *
     * @return current user's username string
     */
    public String getCurrentUser() {
        return currentUser;
    }
}
