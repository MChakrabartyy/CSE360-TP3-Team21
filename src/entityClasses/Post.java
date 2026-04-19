
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*******
 * <p> Title: Post Class (TP2). </p>
 *
 * <p> Description: This class represents a single discussion post in the Student Discussion
 * System, extending the HW2 Post class design to satisfy all Student User Stories required
 * by TP2 and to anticipate attributes needed by the Staff Epics in TP3.
 *
 * <p><b>Student User Stories satisfied by this class:</b></p>
 * <ul>
 *   <li><b>US-S1</b> – Students can post statements and questions and receive replies.
 *       Supported by: {@code title}, {@code body}, {@code postType}, {@code parentPostId}.</li>
 *   <li><b>US-S2</b> – Students can see a list of posts others have made.
 *       Supported by: {@code getAllPosts()} in PostList; {@code isDeleted} ensures deleted
 *       posts show a placeholder rather than disappearing.</li>
 *   <li><b>US-S3</b> – Students can see their own posts, reply counts, and unread reply
 *       counts.  Supported by: {@code author}, {@code replyCount}, {@code unreadReplyCount}.</li>
 *   <li><b>US-S4</b> – Students can search posts by keyword and see read/unread status,
 *       reply counts, and unread reply counts.
 *       Supported by: {@code isReadByCurrentUser}, {@code replyCount},
 *       {@code unreadReplyCount}.</li>
 *   <li><b>US-S5</b> – Students can post to different threads; defaults to "General".
 *       Supported by: {@code threadName}.</li>
 *   <li><b>US-S6</b> – Students can delete their own post with confirmation; replies are
 *       preserved with a deleted-post notice.
 *       Supported by: {@code isDeleted} (soft delete flag).</li>
 *   <li><b>US-S7</b> – Students can search across all threads or a specific thread.
 *       Supported by: {@code threadName} used as filter in PostList.searchPosts().</li>
 * </ul>
 *
 * <p><b>Staff Epic forward-compatibility (TP3):</b></p>
 * <ul>
 *   <li>{@code staffFeedback}    – private feedback a staff member attaches to a post.</li>
 *   <li>{@code isFlaggedByStaff} – marks a post for moderation review.</li>
 *   <li>{@code resolvedMark}     – grade or mark assigned by staff after evaluation.</li>
 * </ul>
 *
 * <p><b>Design note:</b> Validation is NOT performed in this class.  Following the pattern
 * established by the HW2 {@code PostList} class and the existing {@code User} entity, all
 * validation is centralised in {@code PostList} (the list/manager class).  This keeps the
 * model simple and focused purely on data storage.</p>
 *
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-03-19  Initial TP2 version – extends HW2 Post design
 * @version 1.01    2026-03-20  Added staff-epic forward-compatibility fields
 */
public class Post {

    /*-******************************************************************************************

    Attributes
    
    Fields are grouped into three categories:
      (A) Core fields carried over from HW2
      (B) New fields required by the TP2 Student User Stories
      (C) Forward-compatibility fields for the TP3 Staff Epics

    */

    // ── (A) CORE FIELDS (from HW2) ────────────────────────────────────────────────────────────

    /**
     * Unique integer identifier for this post, assigned by {@code PostList} at creation time.
     * IDs are never reused after deletion to avoid a reply accidentally linking to a new post
     * that happens to receive the same ID.
     * Source: HW2 Post design.
     */
    private int postID;

    /**
     * Short title summarising what the post is about (1–200 characters).
     * Required by <b>US-S1</b> (student posts a statement or question) and
     * <b>US-S4</b> (keyword search checks the title).
     * Source: HW2 Post design; US-S1.
     */
    private String title;

    /**
     * Full text content of the post (1–2000 characters).
     * Required by <b>US-S1</b> (the body IS the statement or question).
     * Source: HW2 Post design; US-S1.
     */
    private String body;

    /**
     * Username of the student who created this post.  Cannot be empty.
     * Required by <b>US-S3</b> (filter "my posts" by author) and
     * <b>US-S6</b> (only the author may delete their own post).
     * Source: HW2 Post design; US-S3; US-S6.
     */
    private String author;

    /**
     * Timestamp automatically set at construction time, stored as a formatted string
     * (pattern: {@code yyyy-MM-dd HH:mm:ss}).
     * Used to order posts chronologically in list views.
     * Source: HW2 Post design.
     */
    private String timestamp;

    /** Formatter shared across all Post instances to keep timestamp format consistent. */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    // ── (B) NEW FIELDS FOR TP2 STUDENT USER STORIES ───────────────────────────────────────────

    /**
     * The discussion thread this post belongs to (e.g., "General", "HW1", "Midterm").
     * Defaults to {@code "General"} when no thread is specified by the student.
     * Required by <b>US-S5</b> (post to a thread; default = General) and
     * <b>US-S7</b> (search within a specific thread or across all threads).
     * Source: US-S5; US-S7.
     */
    private String threadName;

    /**
     * Type of post: {@code "question"} or {@code "statement"}.
     * Required by <b>US-S1</b> which explicitly states students post "statements and questions".
     * Distinguishing type allows the UI to display a "?" icon for questions, matching
     * Ed Discussion's visual design.
     * Source: US-S1; Ed Discussion UI conventions.
     */
    private String postType;

    /**
     * Soft-delete flag.  When {@code true} the post has been deleted by its author but is
     * NOT physically removed from the list.  Instead the view layer renders a placeholder
     * message: "The original post has been deleted."
     * This preserves all replies to the post, satisfying <b>US-S6</b> which states:
     * "When I delete a post, any replies to that post are not deleted."
     * Source: US-S6.
     */
    private boolean isDeleted;

    /**
     * ID of the parent post if this post is a reply ({@code -1} if this is a top-level post).
     * Required by <b>US-S1</b> (students receive replies) so that replies can be grouped
     * under their parent post in the view.
     * Source: US-S1; Ed Discussion threading model.
     */
    private int parentPostId;

    /**
     * Total number of replies this post has received.
     * Required by <b>US-S3</b> ("I can see… the number of replies") and
     * <b>US-S4</b> (search results show reply counts).
     * Incremented by {@code PostList} whenever a reply is added.
     * Source: US-S3; US-S4.
     */
    private int replyCount;

    /**
     * Number of replies to this post that the current user has not yet read.
     * Required by <b>US-S3</b> ("how many of them I have not yet read") and
     * <b>US-S4</b> (search results show unread reply counts).
     * Source: US-S3; US-S4.
     */
    private int unreadReplyCount;

    /**
     * Whether the currently logged-in student has read this post.
     * Required by <b>US-S4</b> ("I can see which ones I have read and which I have not").
     * Set to {@code true} the first time the student opens the post detail view.
     * Source: US-S4.
     */
    private boolean isReadByCurrentUser;


    // ── (C) FORWARD-COMPATIBILITY FIELDS FOR TP3 STAFF EPICS ─────────────────────────────────

    /**
     * Private feedback written by a staff member for this post.  {@code null} if no feedback
     * has been given.
     * Forward-compatibility for TP3 Staff Epic: "staff can provide private feedback to
     * students".  Stored here so the Post class does not need to be restructured in TP3.
     * Source: Staff Epic (TP3); HW2 Staff Epic document.
     */
    private String staffFeedback;

    /**
     * Flag set by a staff member to mark this post for moderation review.
     * Forward-compatibility for TP3 Staff Epic: "review and manage interactions among
     * students to maintain a positive atmosphere and deal with inappropriate posts".
     * Source: Staff Epic (TP3); HW2 Staff Epic document.
     */
    private boolean isFlaggedByStaff;

    /**
     * Grade or mark assigned by staff after evaluating this post.  {@code null} if not yet
     * graded.
     * Forward-compatibility for TP3 Staff Epic: "assign marks (grades)" based on student
     * discussion performance.
     * Source: Staff Epic (TP3); HW2 Staff Epic document.
     */
    private String resolvedMark;


    /*-******************************************************************************************

    Constructor

    */

    /**********
     * <p> Constructor: Post(int, String, String, String, String, String, int) </p>
     *
     * <p> Description: Constructs a new Post with all required fields.  The timestamp is set
     * automatically.  Staff-epic fields default to safe values ({@code null} / {@code false}).
     * Validation is NOT performed here — {@code PostList.createPost()} validates all inputs
     * before calling this constructor, following the same pattern as the HW2 design. </p>
     *
     * @param postID        Unique ID assigned by PostList
     * @param title         Short title of the post (already validated)
     * @param body          Full body text (already validated)
     * @param author        Username of the creating student (already validated)
     * @param threadName    Thread to post in; {@code "General"} if none specified
     * @param postType      {@code "question"} or {@code "statement"}
     * @param parentPostId  ID of parent post, or {@code -1} for a top-level post
     */
    public Post(int postID, String title, String body, String author,
                String threadName, String postType, int parentPostId) {
        this.postID         = postID;
        this.title          = title;
        this.body           = body;
        this.author         = author;

        // US-S5: default thread to "General" if caller passes null or blank
        this.threadName     = (threadName == null || threadName.trim().isEmpty())
                              ? "General" : threadName;

        this.postType       = (postType == null || postType.trim().isEmpty())
                              ? "statement" : postType;

        this.parentPostId   = parentPostId;

        // Soft-delete starts as false — post is live when first created
        this.isDeleted      = false;

        // Reply and read tracking start at zero / false
        this.replyCount          = 0;
        this.unreadReplyCount    = 0;
        this.isReadByCurrentUser = false;

        // Staff-epic fields default to null/false — populated later by staff actions in TP3
        this.staffFeedback    = null;
        this.isFlaggedByStaff = false;
        this.resolvedMark     = null;

        // Timestamp set automatically — same approach as HW2 Post
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }


    /*-******************************************************************************************

    Getters and Setters
    
    Immutable after creation: postID, author, timestamp, parentPostId
    Mutable: title, body, threadName, postType, isDeleted, replyCount,
             unreadReplyCount, isReadByCurrentUser, staffFeedback,
             isFlaggedByStaff, resolvedMark

    */

    /**
     * Returns the unique integer ID of this post.
     * @return the post's integer ID
     */
    public int getPostID() { return postID; }

    /**
     * Returns the title of this post.
     * @return the post title string
     */
    public String getTitle() { return title; }

    /**
     * Updates the title of this post.  Called by {@code PostList.updatePost()} after
     * validation confirms the new title is acceptable.
     * @param title the new title string
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Returns the body text of this post.
     * @return the post body string
     */
    public String getBody() { return body; }

    /**
     * Updates the body of this post.  Called by {@code PostList.updatePost()} after
     * validation confirms the new body is acceptable.
     * @param body the new body string
     */
    public void setBody(String body) { this.body = body; }

    /**
     * Returns the username of the student who created this post.
     * @return the author's username string
     */
    public String getAuthor() { return author; }

    /**
     * Returns the formatted timestamp string recording when this post was created.
     * @return the formatted timestamp string (yyyy-MM-dd HH:mm:ss)
     */
    public String getTimestamp() { return timestamp; }

    /**
     * Returns the name of the thread this post belongs to.
     * Satisfies <b>US-S5</b> and <b>US-S7</b>.
     * @return the thread name string
     */
    public String getThreadName() { return threadName; }

    /**
     * Updates the thread this post belongs to.
     * @param threadName the new thread name
     */
    public void setThreadName(String threadName) { this.threadName = threadName; }

    /**
     * Returns the type of this post: {@code "question"} or {@code "statement"}.
     * Satisfies <b>US-S1</b>.
     * @return the post type string
     */
    public String getPostType() { return postType; }

    /**
     * Updates the post type.
     * @param postType {@code "question"} or {@code "statement"}
     */
    public void setPostType(String postType) { this.postType = postType; }

    /**
     * Returns whether this post has been soft-deleted.
     * When {@code true}, the view layer shows "The original post has been deleted."
     * instead of the post content.  Satisfies <b>US-S6</b>.
     * @return {@code true} if the post has been deleted, {@code false} otherwise
     */
    public boolean isDeleted() { return isDeleted; }

    /**
     * Sets the soft-delete flag.  Called by {@code PostList.deletePost()} after the student
     * confirms deletion.  Setting to {@code true} preserves replies per <b>US-S6</b>.
     * @param deleted {@code true} to mark as deleted, {@code false} to restore
     */
    public void setDeleted(boolean deleted) { this.isDeleted = deleted; }

    /**
     * Returns the ID of the parent post, or {@code -1} if this is a top-level post.
     * Satisfies <b>US-S1</b> (reply threading).
     * @return parent post ID, or -1
     */
    public int getParentPostId() { return parentPostId; }

    /**
     * Returns the total number of replies this post has received.
     * Satisfies <b>US-S3</b> and <b>US-S4</b>.
     * @return integer reply count
     */
    public int getReplyCount() { return replyCount; }

    /**
     * Sets the total reply count.  Called by {@code PostList} when a reply is added or
     * removed.
     * @param replyCount the new reply count
     */
    public void setReplyCount(int replyCount) { this.replyCount = replyCount; }

    /**
     * Returns the number of replies the current user has not yet read.
     * Satisfies <b>US-S3</b> ("how many of them I have not yet read") and <b>US-S4</b>.
     * @return integer unread reply count
     */
    public int getUnreadReplyCount() { return unreadReplyCount; }

    /**
     * Sets the unread reply count.  Called by {@code PostList} when a reply is read or added.
     * @param unreadReplyCount the new unread reply count
     */
    public void setUnreadReplyCount(int unreadReplyCount) {
        this.unreadReplyCount = unreadReplyCount;
    }

    /**
     * Returns whether the currently logged-in student has read this post.
     * Satisfies <b>US-S4</b> ("I can see which ones I have read and which I have not").
     * @return {@code true} if the current user has read this post
     */
    public boolean isReadByCurrentUser() { return isReadByCurrentUser; }

    /**
     * Marks this post as read by the current user.  Called by the view controller when the
     * student opens the post detail screen.  Satisfies <b>US-S4</b>.
     * @param read {@code true} to mark as read, {@code false} to mark as unread
     */
    public void setReadByCurrentUser(boolean read) { this.isReadByCurrentUser = read; }

    /**
     * Returns the private staff feedback attached to this post, or {@code null} if none.
     * Forward-compatibility for TP3 Staff Epic.
     * @return staff feedback string, or null
     */
    public String getStaffFeedback() { return staffFeedback; }

    /**
     * Sets private staff feedback for this post.
     * Forward-compatibility for TP3 Staff Epic: private feedback to students.
     * @param staffFeedback the feedback text written by staff
     */
    public void setStaffFeedback(String staffFeedback) { this.staffFeedback = staffFeedback; }

    /**
     * Returns whether a staff member has flagged this post for moderation.
     * Forward-compatibility for TP3 Staff Epic.
     * @return {@code true} if flagged by staff
     */
    public boolean isFlaggedByStaff() { return isFlaggedByStaff; }

    /**
     * Sets the staff-flagged status of this post.
     * Forward-compatibility for TP3 Staff Epic: deal with inappropriate posts.
     * @param flagged {@code true} to flag, {@code false} to clear the flag
     */
    public void setFlaggedByStaff(boolean flagged) { this.isFlaggedByStaff = flagged; }

    /**
     * Returns the grade or mark assigned by staff, or {@code null} if not yet graded.
     * Forward-compatibility for TP3 Staff Epic.
     * @return the mark string, or null
     */
    public String getResolvedMark() { return resolvedMark; }

    /**
     * Sets the grade or mark for this post.
     * Forward-compatibility for TP3 Staff Epic: assign marks based on discussion performance.
     * @param resolvedMark the mark or grade string assigned by staff
     */
    public void setResolvedMark(String resolvedMark) { this.resolvedMark = resolvedMark; }


    /*-******************************************************************************************

    toString

    */

    /**********
     * <p> Method: toString() </p>
     *
     * <p> Description: Returns a human-readable string representation of this post for display
     * and logging purposes.  If the post has been soft-deleted ({@code isDeleted == true}),
     * the content is replaced with the placeholder message required by <b>US-S6</b>. </p>
     *
     * @return a formatted multi-line string describing the post
     */
    @Override
    public String toString() {
        // US-S6: show placeholder text instead of content for deleted posts
        if (isDeleted) {
            return "Post ID: " + postID + "\n" +
                   "[The original post has been deleted.]\n" +
                   "Thread:  " + threadName + "\n" +
                   "Author:  " + author     + "\n" +
                   "Posted:  " + timestamp;
        }
        return "Post ID:  " + postID      + "\n" +
               "Thread:   " + threadName  + "\n" +
               "Type:     " + postType    + "\n" +
               "Title:    " + title       + "\n" +
               "Author:   " + author      + "\n" +
               "Posted:   " + timestamp   + "\n" +
               "Replies:  " + replyCount  + " total, " + unreadReplyCount + " unread\n" +
               "Read:     " + (isReadByCurrentUser ? "Yes" : "No") + "\n" +
               "Body:     " + body;
    }
}
