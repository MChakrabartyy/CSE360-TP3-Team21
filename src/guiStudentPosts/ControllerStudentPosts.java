package guiStudentPosts;

import entityClasses.Post;
import entityClasses.Reply;
import javafx.stage.Stage;

import java.util.List;

/*******
 * <p> Title: ControllerStudentPosts Class. </p>
 *
 * <p> Description: This class is the Controller layer of the MVC architecture for the
 * Student Posts GUI.  It handles all user-triggered actions from {@link ViewStudentPosts}
 * and delegates data operations to {@link ModelStudentPosts}.
 *
 * <p> Following the pattern established by {@code guiUserLogin.ControllerUserLogin}, all
 * methods in this class are {@code protected static}.  They are called exclusively by the
 * View and should never be called directly from outside this package. </p>
 *
 * <p><b>Student User Stories handled by this controller:</b></p>
 * <ul>
 *   <li><b>US-S1</b> – {@link #doCreatePost()} and {@link #doCreateReply(int)}</li>
 *   <li><b>US-S2</b> – {@link #doShowAllPosts()}</li>
 *   <li><b>US-S3</b> – {@link #doShowMyPosts(boolean)}</li>
 *   <li><b>US-S4</b> – {@link #doSearch()}</li>
 *   <li><b>US-S5</b> – thread defaulting handled in {@link #doCreatePost()}</li>
 *   <li><b>US-S6</b> – {@link #doDeletePost(int)} with confirmation check</li>
 *   <li><b>US-S7</b> – {@link #doSearch()} with optional thread filter</li>
 * </ul>
 *
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-03-19  Initial version
 */
public class ControllerStudentPosts {

    /** Reference to the stage so controllers can navigate between scenes. */
    private static Stage theStage;

    /** Reference to the model — single shared instance for this session. */
    private static ModelStudentPosts theModel;

    /**
     * Default constructor — not used.  All methods are static, following the
     * {@code ControllerUserLogin} pattern.
     */
    public ControllerStudentPosts() { }


    /*-******************************************************************************************

    Initialisation — called once by ViewStudentPosts

    */

    /**********
     * <p> Method: initialise(Stage, ModelStudentPosts) </p>
     *
     * <p> Description: Sets up the controller's references to the stage and model.
     * Called by {@link ViewStudentPosts#displayStudentPosts(Stage, entityClasses.User)}
     * before any user action is possible. </p>
     *
     * @param stage     The primary application stage
     * @param model     The shared model instance for this session
     */
    protected static void initialise(Stage stage, ModelStudentPosts model) {
        theStage = stage;
        theModel = model;
    }


    /*-******************************************************************************************

    CREATE actions — US-S1, US-S5

    */

    /**********
     * <p> Method: doCreatePost() </p>
     *
     * <p> Description: Reads the title, body, thread, and type fields from the View,
     * passes them to the Model for validation and storage, and updates the View with
     * the result.  If the thread field is blank, the Model defaults it to "General"
     * per <b>US-S5</b>.
     *
     * <p> On success the new-post form is cleared and the all-posts list is refreshed.
     * On failure the error message is shown in the View's error label. </p>
     *
     */
    protected static void doCreatePost() {
        // Read inputs directly from View fields — same pattern as ControllerUserLogin.doLogin()
        String title      = ViewStudentPosts.text_PostTitle.getText().trim();
        String body       = ViewStudentPosts.text_PostBody.getText().trim();
        String threadName = ViewStudentPosts.text_ThreadName.getText().trim();
        String postType   = ViewStudentPosts.combo_PostType.getValue();

        // US-S5: empty thread defaults to "General" — handled inside ModelStudentPosts/PostList
        String result = theModel.createPost(title, body, threadName, postType);

        if (result.isEmpty()) {
            // Success — clear the form and refresh the post list
            ViewStudentPosts.text_PostTitle.setText("");
            ViewStudentPosts.text_PostBody.setText("");
            ViewStudentPosts.text_ThreadName.setText("");
            ViewStudentPosts.label_ErrorMessage.setText("Post created successfully.");
            doShowAllPosts(); // refresh list
        } else {
            // Show the specific validation error returned by PostList
            ViewStudentPosts.label_ErrorMessage.setText(result);
        }
    }

    /**********
     * <p> Method: doCreateReply(int) </p>
     *
     * <p> Description: Creates a reply to the post with the given ID.  Reads the reply
     * body from the View's reply text field.  On success, refreshes the post detail view.
     * Satisfies <b>US-S1</b>. </p>
     *
     * @param postID    The ID of the post being replied to
     */
    protected static void doCreateReply(int postID) {
        String body = ViewStudentPosts.text_ReplyBody.getText().trim();
        String result = theModel.createReply(postID, body);

        if (result.isEmpty()) {
            ViewStudentPosts.text_ReplyBody.setText("");
            ViewStudentPosts.label_ErrorMessage.setText("Reply posted successfully.");
            doShowPostDetail(postID); // refresh detail view to show new reply
        } else {
            ViewStudentPosts.label_ErrorMessage.setText(result);
        }
    }


    /*-******************************************************************************************

    READ actions — US-S2, US-S3, US-S4, US-S7

    */

    /**********
     * <p> Method: doShowAllPosts() </p>
     *
     * <p> Description: Fetches all posts from the Model and populates the View's post
     * list area.  Soft-deleted posts are shown with the placeholder text
     * "The original post has been deleted." per <b>US-S6</b>.
     * Satisfies <b>US-S2</b>. </p>
     *
     */
    protected static void doShowAllPosts() {
        List<Post> posts = theModel.getAllPosts();

        // Clear existing list content
        ViewStudentPosts.listArea_Posts.getItems().clear();

        if (posts.isEmpty()) {
            // US-S2: show message when no posts exist
            ViewStudentPosts.listArea_Posts.getItems().add("No posts found.");
            return;
        }

        // Populate list — deleted posts show placeholder per US-S6
        for (Post p : posts) {
            if (p.isDeleted()) {
                ViewStudentPosts.listArea_Posts.getItems().add(
                    "[DELETED] Post ID: " + p.getPostID() +
                    " | Thread: " + p.getThreadName());
            } else {
                ViewStudentPosts.listArea_Posts.getItems().add(
                    "[" + p.getPostType().toUpperCase() + "] " +
                    "ID:" + p.getPostID() +
                    " | " + p.getTitle() +
                    " | " + p.getAuthor() +
                    " | " + p.getThreadName() +
                    " | Replies: " + p.getReplyCount() +
                    (p.isReadByCurrentUser() ? "" : " [UNREAD]"));
            }
        }
    }

    /**********
     * <p> Method: doShowMyPosts(boolean) </p>
     *
     * <p> Description: Fetches only the current user's posts.  If {@code unreadOnly} is
     * {@code true}, only posts with unread replies are shown, satisfying <b>US-S3</b>:
     * "I can list all the replies or just the unread." </p>
     *
     * @param unreadOnly    {@code true} to show only posts with unread replies
     */
    protected static void doShowMyPosts(boolean unreadOnly) {
        // US-S3: choose full list or unread-only based on the toggle
        List<Post> posts = unreadOnly
                ? theModel.getMyPostsUnreadOnly()
                : theModel.getMyPosts();

        ViewStudentPosts.listArea_Posts.getItems().clear();

        if (posts.isEmpty()) {
            ViewStudentPosts.listArea_Posts.getItems().add(
                unreadOnly ? "No posts with unread replies." : "You have no posts yet.");
            return;
        }

        for (Post p : posts) {
            ViewStudentPosts.listArea_Posts.getItems().add(
                "[" + p.getPostType().toUpperCase() + "] " +
                "ID:" + p.getPostID() +
                " | " + p.getTitle() +
                " | Thread: " + p.getThreadName() +
                " | Replies: " + p.getReplyCount() +
                " | Unread: " + p.getUnreadReplyCount());
        }
    }

    /**********
     * <p> Method: doSearch() </p>
     *
     * <p> Description: Reads the keyword and optional thread filter from the View and
     * calls the Model's searchPosts() method.  If the thread field is blank, all threads
     * are searched (satisfying <b>US-S7</b>).  Results show read/unread status and reply
     * counts per <b>US-S4</b>. </p>
     *
     */
    protected static void doSearch() {
        String keyword    = ViewStudentPosts.text_SearchKeyword.getText().trim();
        // US-S7: blank thread = search all threads; non-blank = search specific thread
        String threadFilter = ViewStudentPosts.text_SearchThread.getText().trim();
        String threadArg  = threadFilter.isEmpty() ? null : threadFilter;

        List<Post> results = theModel.searchPosts(keyword, threadArg);

        ViewStudentPosts.listArea_Posts.getItems().clear();

        if (keyword.isEmpty()) {
            ViewStudentPosts.label_ErrorMessage.setText("Search keyword cannot be empty.");
            return;
        }

        if (results.isEmpty()) {
            ViewStudentPosts.listArea_Posts.getItems().add("No posts found.");
            return;
        }

        // US-S4: show read/unread status, reply count, unread reply count for each result
        for (Post p : results) {
            ViewStudentPosts.listArea_Posts.getItems().add(
                (p.isReadByCurrentUser() ? "[READ]   " : "[UNREAD] ") +
                "ID:" + p.getPostID() +
                " | " + p.getTitle() +
                " | Thread: " + p.getThreadName() +
                " | Replies: " + p.getReplyCount() +
                " | Unread replies: " + p.getUnreadReplyCount());
        }
        ViewStudentPosts.label_ErrorMessage.setText(
                results.size() + " result(s) found.");
    }

    /**********
     * <p> Method: doShowPostDetail(int) </p>
     *
     * <p> Description: Loads the full content of the post with the given ID into the
     * View's detail area, along with all its replies.  Marks the post as read by the
     * current user per <b>US-S4</b>. </p>
     *
     * @param postID    The ID of the post to display in detail
     */
    protected static void doShowPostDetail(int postID) {
        Post post = theModel.getPostByID(postID);

        if (post == null) {
            ViewStudentPosts.label_DetailContent.setText("Post not found.");
            return;
        }

        // US-S4: mark as read when student opens the detail view
        theModel.markPostAsRead(postID);

        // Build the detail display string
        StringBuilder detail = new StringBuilder();
        if (post.isDeleted()) {
            // US-S6: show placeholder for deleted posts
            detail.append("[The original post has been deleted.]\n");
            detail.append("Thread: ").append(post.getThreadName()).append("\n");
        } else {
            detail.append("Title:   ").append(post.getTitle()).append("\n");
            detail.append("Author:  ").append(post.getAuthor()).append("\n");
            detail.append("Thread:  ").append(post.getThreadName()).append("\n");
            detail.append("Type:    ").append(post.getPostType()).append("\n");
            detail.append("Posted:  ").append(post.getTimestamp()).append("\n\n");
            detail.append(post.getBody()).append("\n\n");
        }

        // Always show replies — they survive deletion per US-S6
        List<Reply> replies = theModel.getRepliesForPost(postID);
        if (replies.isEmpty()) {
            detail.append("--- No replies yet ---");
        } else {
            detail.append("--- Replies (").append(replies.size()).append(") ---\n");
            for (Reply r : replies) {
                detail.append("[").append(r.getAuthor()).append(" @ ")
                      .append(r.getTimestamp()).append("]\n");
                detail.append(r.getBody()).append("\n\n");
            }
        }

        ViewStudentPosts.label_DetailContent.setText(detail.toString());

        // Store the current post ID so delete/reply buttons know which post is selected
        ViewStudentPosts.currentPostID = postID;
    }


    /*-******************************************************************************************

    DELETE action — US-S6

    */

    /**********
     * <p> Method: doDeletePost(int) </p>
     *
     * <p> Description: Handles the delete action for the post with the given ID.
     * The "Are you sure?" confirmation dialog is shown by the View before this method
     * is called — this method only executes if the user clicked "Yes".
     *
     * <p> The delete is a soft delete: the post's {@code isDeleted} flag is set to
     * {@code true} but it is not removed from the list.  Replies are preserved and will
     * show the placeholder message per <b>US-S6</b>. </p>
     *
     * @param postID    The ID of the post to soft-delete
     */
    protected static void doDeletePost(int postID) {
        // US-S6: confirmation already shown by View before calling here
        String result = theModel.deletePost(postID);

        if (result.isEmpty()) {
            ViewStudentPosts.label_ErrorMessage.setText(
                "Post deleted. Replies have been preserved.");
            ViewStudentPosts.label_DetailContent.setText(
                "[The original post has been deleted.]");
            doShowAllPosts(); // refresh list to show placeholder
        } else {
            ViewStudentPosts.label_ErrorMessage.setText(result);
        }
    }
}
