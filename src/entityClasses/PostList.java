package entityClasses;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: PostList Class (TP2). </p>
 *
 * <p> Description: This class manages the complete collection of {@link Post} objects in the
 * Student Discussion System.  It is the single point of entry for all CRUD operations on
 * posts, all input validation, and all query operations required by the Student User Stories.
 *
 * <p><b>CRUD operations provided:</b></p>
 * <ul>
 *   <li><b>Create</b> – {@link #createPost(String, String, String, String, String, int)}</li>
 *   <li><b>Read</b>   – {@link #getAllPosts()}, {@link #getPostByID(int)},
 *                       {@link #getMyPosts(String)}, {@link #searchPosts(String, String)}</li>
 *   <li><b>Update</b> – {@link #updatePost(int, String, String, String)}</li>
 *   <li><b>Delete</b> – {@link #deletePost(int, String)}</li>
 * </ul>
 *
 * <p><b>Student User Stories satisfied:</b></p>
 * <ul>
 *   <li><b>US-S1</b> – {@code createPost()} lets students post statements and questions.</li>
 *   <li><b>US-S2</b> – {@code getAllPosts()} returns all posts for browsing.</li>
 *   <li><b>US-S3</b> – {@code getMyPosts()} returns a student's own posts with reply/unread
 *       counts; {@code getUnreadOnly()} filters to unread replies only.</li>
 *   <li><b>US-S4</b> – {@code searchPosts(keyword, thread)} returns matching posts with
 *       read/unread status and reply counts.</li>
 *   <li><b>US-S5</b> – {@code createPost()} defaults thread to "General" when unspecified.</li>
 *   <li><b>US-S6</b> – {@code deletePost()} performs a soft delete after author check;
 *       replies survive; placeholder message shown via {@code Post.isDeleted()}.</li>
 *   <li><b>US-S7</b> – {@code searchPosts(keyword, null)} searches all threads;
 *       {@code searchPosts(keyword, threadName)} searches a specific thread.</li>
 * </ul>
 *
 * <p><b>Design note:</b> Validation is centralised here, not in {@link Post}.  This follows
 * the same pattern established by the HW2 PostList and keeps the model class (Post) focused
 * purely on data storage.  All methods return an empty string on success or a specific
 * human-readable error message on failure, so the view layer can display it directly.</p>
 *
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-03-19  Initial TP2 version
 * @version 1.01    2026-03-20  Added getMyPosts, unread filtering, soft-delete author check
 */
public class PostList {

    /*-******************************************************************************************

    Attributes

    */

    /** The master list holding every Post object in the system. */
    private List<Post> posts = new ArrayList<>();

    /**
     * Counter used to assign unique post IDs.  Starts at 1, increments by 1 per post.
     * IDs are never reused after deletion — see {@link Post#getPostID()} rationale.
     */
    private int nextPostID = 1;

    /** Maximum character length allowed for a post title. Source: HW2 validation rules. */
    private static final int MAX_TITLE_LENGTH = 200;

    /** Maximum character length allowed for a post body. Source: HW2 validation rules. */
    private static final int MAX_BODY_LENGTH = 2000;


    /*-******************************************************************************************

    CREATE — US-S1, US-S5

    */

    /**********
     * <p> Method: createPost(String, String, String, String, String, int) </p>
     *
     * <p> Description: Validates all inputs and, if valid, constructs a new {@link Post} and
     * adds it to the list.  Returns an empty string on success or a specific error message
     * on failure.
     *
     * <p>If {@code threadName} is null or blank, the post defaults to "General" per
     * <b>US-S5</b>.  Validation rejects empty titles, empty bodies, and inputs that exceed
     * the character limits defined by the HW2 validation rules. </p>
     *
     * @param title         Short title of the post (1–200 characters)
     * @param body          Full body text (1–2000 characters)
     * @param author        Username of the posting student (cannot be empty)
     * @param threadName    Thread to post in; defaults to "General" if null or blank
     * @param postType      {@code "question"} or {@code "statement"}
     * @param parentPostId  ID of parent post for replies, or {@code -1} for top-level
     * @return              Empty string on success, specific error message on failure
     */
    public String createPost(String title, String body, String author,
                             String threadName, String postType, int parentPostId) {

        // Validate title — US-S1 requires a title for every post
        String titleError = validateTitle(title);
        if (!titleError.isEmpty()) return titleError;

        // Validate body — US-S1 requires a non-empty body
        String bodyError = validateBody(body);
        if (!bodyError.isEmpty()) return bodyError;

        // Validate author — system cannot save an anonymous post
        if (author == null || author.trim().isEmpty()) {
            return "Author is required.";
        }

        // US-S5: threadName defaults to "General" if not specified — handled inside Post()
        // US-S1: postType defaults to "statement" if not specified — handled inside Post()

        Post newPost = new Post(nextPostID, title, body, author, threadName, postType, parentPostId);
        posts.add(newPost);

        System.out.println("** Post created: ID=" + nextPostID +
                           ", Thread='" + newPost.getThreadName() +
                           "', Type='" + newPost.getPostType() +
                           "', Author='" + author + "'");

        nextPostID++;
        return "";
    }


    /*-******************************************************************************************

    READ — US-S2, US-S3, US-S4, US-S7

    */

    /**********
     * <p> Method: getAllPosts() </p>
     *
     * <p> Description: Returns the complete list of all posts including soft-deleted ones.
     * Soft-deleted posts are included so the view layer can display the
     * "The original post has been deleted." placeholder message alongside their replies,
     * satisfying <b>US-S6</b>.
     *
     * <p> If the list is empty the caller should display "No posts found." </p>
     *
     * @return  A List of all Post objects (including soft-deleted); never null
     */
    public List<Post> getAllPosts() {
        // Return the full list — view layer decides how to render deleted posts (US-S6)
        return posts;
    }

    /**********
     * <p> Method: getPostByID(int) </p>
     *
     * <p> Description: Finds and returns the post with the given ID, or {@code null} if not
     * found.  The caller is responsible for null-checking and displaying "Post not found."
     * </p>
     *
     * @param postID    The integer ID of the post to retrieve
     * @return          The matching Post, or null if none exists with that ID
     */
    public Post getPostByID(int postID) {
        for (Post p : posts) {
            if (p.getPostID() == postID) return p;
        }
        return null;
    }

    /**********
     * <p> Method: getMyPosts(String) </p>
     *
     * <p> Description: Returns a list of all posts created by the given author.  Each post
     * in the result carries its current {@code replyCount} and {@code unreadReplyCount},
     * satisfying <b>US-S3</b> ("I can see a list of my posts, the number of replies, and
     * how many of them I have not yet read").
     *
     * <p> Returns an empty list (never null) if the author has no posts. </p>
     *
     * @param author    The username whose posts should be returned
     * @return          A List of Post objects authored by that user; never null
     */
    public List<Post> getMyPosts(String author) {
        List<Post> myPosts = new ArrayList<>();

        // US-S3: filter by author username
        for (Post p : posts) {
            if (p.getAuthor().equals(author)) {
                myPosts.add(p);
            }
        }
        return myPosts;
    }

    /**********
     * <p> Method: getMyPostsUnreadOnly(String) </p>
     *
     * <p> Description: Returns only the posts authored by the given student that have at
     * least one unread reply.  Satisfies <b>US-S3</b>: "I can list all the replies or just
     * the unread, so I don't have to scan messages I've already read." </p>
     *
     * @param author    The username to filter by
     * @return          A List of Post objects with unread replies; never null
     */
    public List<Post> getMyPostsUnreadOnly(String author) {
        List<Post> result = new ArrayList<>();

        // US-S3: only include posts that have unread replies
        for (Post p : posts) {
            if (p.getAuthor().equals(author) && p.getUnreadReplyCount() > 0) {
                result.add(p);
            }
        }
        return result;
    }

    /**********
     * <p> Method: searchPosts(String, String) </p>
     *
     * <p> Description: Returns a subset of non-deleted posts whose title or body contains
     * the given keyword (case-insensitive).  If {@code threadName} is null or blank, all
     * threads are searched (satisfying <b>US-S7</b>).  If a thread is specified, only posts
     * in that thread are searched (satisfying <b>US-S4</b>).
     *
     * <p> Each result carries its {@code isReadByCurrentUser}, {@code replyCount}, and
     * {@code unreadReplyCount} fields so the view can display read/unread status and counts
     * per <b>US-S4</b>.
     *
     * <p> Returns an empty list (never null) if no posts match. </p>
     *
     * @param keyword       The search term to look for in titles and bodies
     * @param threadName    Thread to restrict search to, or null/blank for all threads
     * @return              A List of matching non-deleted Posts; never null
     */
    public List<Post> searchPosts(String keyword, String threadName) {
        List<Post> results = new ArrayList<>();

        // Reject empty keyword — an empty search would return everything, which is misleading
        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("** Search keyword cannot be empty.");
            return results;
        }

        // Convert once to avoid repeated toLowerCase() calls inside the loop
        String lowerKeyword = keyword.toLowerCase();
        boolean searchAllThreads = (threadName == null || threadName.trim().isEmpty());

        for (Post p : posts) {

            // US-S6: exclude soft-deleted posts from search results
            if (p.isDeleted()) continue;

            // US-S7: if a thread was specified, skip posts not in that thread
            if (!searchAllThreads &&
                !p.getThreadName().equalsIgnoreCase(threadName)) continue;

            // US-S4: search both title and body, case-insensitive
            boolean titleMatch = p.getTitle().toLowerCase().contains(lowerKeyword);
            boolean bodyMatch  = p.getBody().toLowerCase().contains(lowerKeyword);

            if (titleMatch || bodyMatch) {
                results.add(p);
            }
        }
        return results;
    }

    /**********
     * <p> Method: markPostAsRead(int) </p>
     *
     * <p> Description: Marks the post with the given ID as read by the current user.
     * Called by the view controller when a student opens the post detail screen.
     * Satisfies <b>US-S4</b> ("I can see which ones I have read and which I have not").
     * </p>
     *
     * @param postID    The ID of the post to mark as read
     */
    public void markPostAsRead(int postID) {
        Post p = getPostByID(postID);
        if (p != null) {
            // US-S4: mark as read when student views the post
            p.setReadByCurrentUser(true);
        }
    }

    /**********
     * <p> Method: incrementReplyCount(int) </p>
     *
     * <p> Description: Increments both the total reply count and unread reply count of the
     * post with the given ID.  Called when a new reply is added to that post.
     * Satisfies <b>US-S3</b> and <b>US-S4</b>. </p>
     *
     * @param postID    The ID of the post that received a new reply
     */
    public void incrementReplyCount(int postID) {
        Post p = getPostByID(postID);
        if (p != null) {
            // US-S3: keep reply counts accurate for "my posts" view
            p.setReplyCount(p.getReplyCount() + 1);
            p.setUnreadReplyCount(p.getUnreadReplyCount() + 1);
        }
    }

    /**********
     * <p> Method: decrementUnreadReplyCount(int) </p>
     *
     * <p> Description: Decrements the unread reply count of the post with the given ID.
     * Called when a student reads a reply.  Never goes below zero.
     * Satisfies <b>US-S3</b>. </p>
     *
     * @param postID    The ID of the post whose unread count should decrease
     */
    public void decrementUnreadReplyCount(int postID) {
        Post p = getPostByID(postID);
        if (p != null && p.getUnreadReplyCount() > 0) {
            p.setUnreadReplyCount(p.getUnreadReplyCount() - 1);
        }
    }


    /*-******************************************************************************************

    UPDATE

    */

    /**********
     * <p> Method: updatePost(int, String, String, String) </p>
     *
     * <p> Description: Finds the post, checks that the requesting user is the author (a
     * student may only edit their own post), validates the new content, and applies the
     * update if everything passes.  Returns an empty string on success or an error message
     * on failure.
     *
     * <p> We look up the post first — no point validating inputs if the post does not exist.
     * </p>
     *
     * @param postID            The ID of the post to update
     * @param requestingUser    The username attempting the update
     * @param newTitle          The new title string
     * @param newBody           The new body string
     * @return                  Empty string on success, specific error message on failure
     */
    public String updatePost(int postID, String requestingUser,
                             String newTitle, String newBody) {

        Post post = getPostByID(postID);
        if (post == null) return "Post not found.";

        // A student may only edit their own post
        if (!post.getAuthor().equals(requestingUser)) {
            return "You can only edit your own posts.";
        }

        // Cannot edit a deleted post
        if (post.isDeleted()) return "Post not found.";

        String titleError = validateTitle(newTitle);
        if (!titleError.isEmpty()) return titleError;

        String bodyError = validateBody(newBody);
        if (!bodyError.isEmpty()) return bodyError;

        post.setTitle(newTitle);
        post.setBody(newBody);

        System.out.println("** Post updated: ID=" + postID + ", Author='" + requestingUser + "'");
        return "";
    }


    /*-******************************************************************************************

    DELETE — US-S6

    */

    /**********
     * <p> Method: deletePost(int, String) </p>
     *
     * <p> Description: Performs a <em>soft delete</em> of the post with the given ID.
     * "Soft delete" means {@code isDeleted} is set to {@code true} rather than physically
     * removing the Post object.  This preserves all replies that reference this post,
     * satisfying <b>US-S6</b>: "When I delete a post, any replies to that post are not
     * deleted, but anyone viewing the reply will see a message saying that the original
     * post has been deleted."
     *
     * <p> Only the original author may delete their own post.  If another user attempts
     * to delete the post, an error message is returned. </p>
     *
     * <p> The "Are you sure?" confirmation dialog is handled by the view layer
     * ({@code DeletePostConfirmPage}), not here, keeping validation logic and UI logic
     * separate per the MVC pattern. </p>
     *
     * @param postID            The ID of the post to soft-delete
     * @param requestingUser    The username attempting the deletion
     * @return                  Empty string on success, specific error message on failure
     */
    public String deletePost(int postID, String requestingUser) {

        Post post = getPostByID(postID);
        if (post == null) return "Post not found.";

        // US-S6: only the author may delete their own post
        if (!post.getAuthor().equals(requestingUser)) {
            return "You can only delete your own posts.";
        }

        // Already deleted — nothing to do
        if (post.isDeleted()) return "Post not found.";

        // US-S6: soft delete — set flag, do NOT remove from list so replies survive
        post.setDeleted(true);

        System.out.println("** Post soft-deleted: ID=" + postID +
                           ", Author='" + requestingUser + "'");
        return "";
    }


    /*-******************************************************************************************

    Utility methods

    */

    /**********
     * <p> Method: postExists(int) </p>
     *
     * <p> Description: Returns {@code true} if a non-deleted post with the given ID exists.
     * Used by {@code ReplyList} to validate that a reply is being linked to a real post.
     * </p>
     *
     * @param postID    The post ID to check
     * @return          {@code true} if the post exists and is not deleted
     */
    public boolean postExists(int postID) {
        Post p = getPostByID(postID);
        return p != null && !p.isDeleted();
    }

    /**
     * Returns the total number of posts in the list (including soft-deleted).
     * Useful in tests to confirm a post was added.
     * @return integer count of all posts
     */
    public int getSize() { return posts.size(); }

    /**
     * Returns the number of non-deleted posts in the list.
     * Useful in tests to confirm a soft-delete worked correctly.
     * @return integer count of active (non-deleted) posts
     */
    public int getActiveSize() {
        int count = 0;
        for (Post p : posts) {
            if (!p.isDeleted()) count++;
        }
        return count;
    }


    /*-******************************************************************************************

    Private Validation Helpers
    
    Private — no outside class should call these directly.  Only used internally by
    createPost() and updatePost(), following the HW2 PostList pattern.

    */

    /**********
     * <p> Method: validateTitle(String) </p>
     *
     * <p> Description: Checks that the title is not null, not blank, and within the 200-
     * character limit.  Returns an empty string if valid, a specific error message if not.
     * Source: HW2 validation rules. </p>
     *
     * @param title     The title string to validate
     * @return          Empty string if valid, error message if not
     */
    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) return "Post title is required.";
        if (title.length() > MAX_TITLE_LENGTH)
            return "Title must be 200 characters or fewer.";
        return "";
    }

    /**********
     * <p> Method: validateBody(String) </p>
     *
     * <p> Description: Checks that the body is not null, not blank, and within the 2000-
     * character limit.  Returns an empty string if valid, a specific error message if not.
     * Source: HW2 validation rules. </p>
     *
     * @param body      The body string to validate
     * @return          Empty string if valid, error message if not
     */
    private String validateBody(String body) {
        if (body == null || body.trim().isEmpty()) return "Post body is required.";
        if (body.length() > MAX_BODY_LENGTH)
            return "Body must be 2000 characters or fewer.";
        return "";
    }
}
