package guiStudentPosts;

import entityClasses.User;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

/*******
 * <p> Title: ViewStudentPosts Class. </p>
 *
 * <p> Description: This class is the View layer of the MVC architecture for the Student Posts
 * GUI.  It builds the JavaFX scene for the student discussion interface, wires all button
 * actions to {@link ControllerStudentPosts}, and exposes the UI widgets as
 * {@code protected static} fields so the Controller can read inputs and update outputs
 * without holding a reference to the View object.
 *
 * <p> This class follows the pattern established by {@code guiUserLogin.ViewUserLogin}:
 * a single static {@code display()} method, a private constructor that builds the scene
 * once, and protected static widget references accessed by the Controller. </p>
 *
 * <p><b>Student User Stories implemented by this View:</b></p>
 * <ul>
 *   <li><b>US-S1</b> – New Post form (title, body, thread, type) + Reply text area.</li>
 *   <li><b>US-S2</b> – "All Posts" button + post list view.</li>
 *   <li><b>US-S3</b> – "My Posts" button + "Unread Only" toggle button.</li>
 *   <li><b>US-S4</b> – Search bar with keyword field + thread filter field.</li>
 *   <li><b>US-S5</b> – Thread name field with "General" prompt text.</li>
 *   <li><b>US-S6</b> – Delete button with "Are you sure?" Alert confirmation dialog.</li>
 *   <li><b>US-S7</b> – Search thread field (blank = all threads).</li>
 * </ul>
 *
 * <p> Copyright: CSE 360 Team 21 © 2026 </p>
 *
 * @author Manisha
 *
 * @version 1.00    2026-03-19  Initial version
 */
public class ViewStudentPosts {

    /*-******************************************************************************************

    Attributes — window dimensions (match existing codebase pattern)

    */

    /** Window width — pulled from FoundationsMain to stay consistent across all pages. */
    private static double width  = applicationMain.FoundationsMain.WINDOW_WIDTH;

    /** Window height — pulled from FoundationsMain. */
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


    /*-******************************************************************************************

    UI Widgets — protected static so ControllerStudentPosts can read/write them directly.
    This matches the pattern used in ViewUserLogin (e.g., text_Username, text_Password).

    */

    // ── Page title ────────────────────────────────────────────────────────────────────────────

    /** Page title label displayed at the top of the screen. */
    private static Label label_PageTitle = new Label("Student Discussion");

    /** Subtitle showing the logged-in student's username. */
    private static Label label_WelcomeUser = new Label("");


    // ── Navigation buttons ────────────────────────────────────────────────────────────────────

    /**
     * Shows all posts in the list view.  Satisfies <b>US-S2</b>.
     * Source: US-S2 — "I can see a list of posts others have made."
     */
    private static Button button_AllPosts = new Button("All Posts");

    /**
     * Shows only the current student's posts.  Satisfies <b>US-S3</b>.
     * Source: US-S3 — "I can see a list of my posts."
     */
    private static Button button_MyPosts = new Button("My Posts");

    /**
     * Toggles "my posts" to show only those with unread replies.  Satisfies <b>US-S3</b>.
     * Source: US-S3 — "I can list just the unread."
     */
    private static Button button_UnreadOnly = new Button("Unread Only");


    // ── Post list view ────────────────────────────────────────────────────────────────────────

    /**
     * Displays the list of posts returned by the Controller.
     * Used by US-S2, US-S3, US-S4.
     * Clicking an item calls doShowPostDetail() on the selected post.
     */
    protected static ListView<String> listArea_Posts = new ListView<>();


    // ── Post detail area ──────────────────────────────────────────────────────────────────────

    /**
     * Displays the full content of the selected post and its replies.
     * Shows placeholder "[The original post has been deleted.]" for soft-deleted posts
     * per <b>US-S6</b>.
     */
    protected static Label label_DetailContent = new Label("");

    /**
     * Tracks which post is currently selected/displayed in the detail area.
     * Set by ControllerStudentPosts.doShowPostDetail() so that the Delete and Reply
     * buttons know which post to act on.
     * Source: US-S6 (delete own post), US-S1 (post replies).
     */
    protected static int currentPostID = -1;


    // ── New Post form ─────────────────────────────────────────────────────────────────────────

    /** Section label for the new post form. */
    private static Label label_NewPostSection = new Label("Create New Post");

    /**
     * Title input field for a new post.  Required, 1–200 characters.
     * Satisfies <b>US-S1</b>.
     * Source: US-S1; HW2 validation rule (title 1–200 chars).
     */
    protected static TextField text_PostTitle = new TextField();

    /**
     * Body input field for a new post.  Required, 1–2000 characters.
     * Satisfies <b>US-S1</b>.
     * Source: US-S1; HW2 validation rule (body 1–2000 chars).
     */
    protected static TextArea text_PostBody = new TextArea();

    /**
     * Thread name field.  If left blank, the post is placed in the "General" thread
     * per <b>US-S5</b>.
     * Source: US-S5 — "If I do not specify a thread, it defaults to the 'General' thread."
     */
    protected static TextField text_ThreadName = new TextField();

    /**
     * Drop-down to select post type: "question" or "statement".
     * Satisfies <b>US-S1</b> which explicitly mentions both types.
     * Source: US-S1; Ed Discussion UI conventions.
     */
    protected static ComboBox<String> combo_PostType = new ComboBox<>();

    /** Button that submits the new post form. */
    private static Button button_SubmitPost = new Button("Post");


    // ── Reply form ────────────────────────────────────────────────────────────────────────────

    /** Label for the reply section. */
    private static Label label_ReplySection = new Label("Reply to Selected Post");

    /**
     * Body input field for a reply.  Satisfies <b>US-S1</b>.
     * Source: US-S1 — "I can post statements and questions and receive replies."
     */
    protected static TextArea text_ReplyBody = new TextArea();

    /** Button that submits the reply. */
    private static Button button_SubmitReply = new Button("Submit Reply");


    // ── Delete button ─────────────────────────────────────────────────────────────────────────

    /**
     * Deletes the currently selected post after showing a confirmation dialog.
     * Satisfies <b>US-S6</b>.
     * Source: US-S6 — "I receive an 'Are you sure?' question before the delete takes place."
     */
    private static Button button_DeletePost = new Button("Delete Post");


    // ── Search bar ────────────────────────────────────────────────────────────────────────────

    /** Section label for the search area. */
    private static Label label_SearchSection = new Label("Search Posts");

    /**
     * Keyword input for searching posts.  Satisfies <b>US-S4</b> and <b>US-S7</b>.
     * Source: US-S4 — "I can search for posts with keywords."
     */
    protected static TextField text_SearchKeyword = new TextField();

    /**
     * Optional thread filter for search.  If blank, all threads are searched.
     * Satisfies <b>US-S7</b>.
     * Source: US-S7 — "If I do not specify a thread, all threads are searched."
     */
    protected static TextField text_SearchThread = new TextField();

    /** Button that triggers the keyword search. */
    private static Button button_Search = new Button("Search");


    // ── Error / status message ────────────────────────────────────────────────────────────────

    /**
     * Displays validation error messages and success confirmations to the student.
     * Used by ControllerStudentPosts to provide feedback after every action.
     */
    protected static Label label_ErrorMessage = new Label("");


    // ── Scene ─────────────────────────────────────────────────────────────────────────────────

    /** The JavaFX Scene for this page. */
    public static Scene theStudentPostsScene = null;

    /** Singleton view instance — only built once, matching ViewUserLogin pattern. */
    private static ViewStudentPosts theView = null;


    /*-******************************************************************************************

    Public display method — entry point called by the student home page

    */

    /**********
     * <p> Method: displayStudentPosts(Stage, User) </p>
     *
     * <p> Description: Entry point for displaying the Student Posts page.  Follows the
     * exact same pattern as {@code ViewUserLogin.displayUserLogin(Stage)}: creates the
     * view singleton on the first call, resets dynamic fields, and shows the scene.
     *
     * <p> Called from the student role home page (guiRole1 or guiRole2) when the student
     * clicks the "Discussion" button. </p>
     *
     * @param ps    The primary application Stage
     * @param user  The currently logged-in User object
     */
    public static void displayStudentPosts(Stage ps, User user) {
        // Build the model for this session
        ModelStudentPosts model = new ModelStudentPosts(user.getUserName());

        // Build the view once — singleton pattern from ViewUserLogin
        if (theView == null) theView = new ViewStudentPosts();

        // Initialise the controller with stage + model
        ControllerStudentPosts.initialise(ps, model);

        // Update welcome label with current user's name
        label_WelcomeUser.setText("Welcome, " + user.getUserName());

        // Reset error message and detail area
        label_ErrorMessage.setText("");
        label_DetailContent.setText("Select a post to view details.");
        currentPostID = -1;

        // Show the scene
        ps.setTitle("CSE 360 Foundation: Student Discussion");
        ps.setScene(theStudentPostsScene);
        ps.show();

        // Load all posts immediately on page open — satisfies US-S2
        ControllerStudentPosts.doShowAllPosts();
    }


    /*-******************************************************************************************

    Private constructor — builds the scene once

    */

    /**********
     * <p> Constructor: ViewStudentPosts() </p>
     *
     * <p> Description: Private constructor that builds the entire JavaFX scene.  Called
     * only once by {@link #displayStudentPosts(Stage, User)}.  All widgets are positioned
     * using the same {@code setupLabelUI} / {@code setupButtonUI} / {@code setupTextUI}
     * helper methods from {@code ViewUserLogin}. </p>
     *
     */
    private ViewStudentPosts() {
        Pane theRootPane = new Pane();
        theStudentPostsScene = new Scene(theRootPane, width, height);

        // ── Title bar ────────────────────────────────────────────────────────────────────────
        setupLabelUI(label_PageTitle,   "Arial", 28, width, Pos.CENTER, 0, 10);
        setupLabelUI(label_WelcomeUser, "Arial", 16, width, Pos.CENTER, 0, 45);

        // ── Navigation buttons ────────────────────────────────────────────────────────────────
        // US-S2: All Posts button
        setupButtonUI(button_AllPosts,  "Arial", 14, 120, Pos.CENTER, 20,  75);
        button_AllPosts.setOnAction((_) -> ControllerStudentPosts.doShowAllPosts());

        // US-S3: My Posts button
        setupButtonUI(button_MyPosts,   "Arial", 14, 120, Pos.CENTER, 150, 75);
        button_MyPosts.setOnAction((_) -> ControllerStudentPosts.doShowMyPosts(false));

        // US-S3: Unread Only toggle
        setupButtonUI(button_UnreadOnly,"Arial", 14, 130, Pos.CENTER, 280, 75);
        button_UnreadOnly.setOnAction((_) -> ControllerStudentPosts.doShowMyPosts(true));

        // ── Post list view ────────────────────────────────────────────────────────────────────
        // US-S2, US-S3, US-S4: main scrollable list of posts
        listArea_Posts.setLayoutX(20);
        listArea_Posts.setLayoutY(110);
        listArea_Posts.setPrefWidth(460);
        listArea_Posts.setPrefHeight(280);
        // Clicking a list item loads the post detail — US-S2, US-S4
        listArea_Posts.setOnMouseClicked((_) -> {
            String selected = listArea_Posts.getSelectionModel().getSelectedItem();
            if (selected != null && selected.contains("ID:")) {
                // Parse the post ID from the list string format "... ID:N ..."
                try {
                    int idStart = selected.indexOf("ID:") + 3;
                    int idEnd   = selected.indexOf(" ", idStart);
                    if (idEnd == -1) idEnd = selected.length();
                    int postID  = Integer.parseInt(selected.substring(idStart, idEnd).trim());
                    ControllerStudentPosts.doShowPostDetail(postID);
                } catch (NumberFormatException e) {
                    // Ignore clicks on non-post items like "No posts found."
                }
            }
        });

        // ── Post detail area ──────────────────────────────────────────────────────────────────
        // US-S2, US-S6: detail label showing full post content and replies
        label_DetailContent.setLayoutX(500);
        label_DetailContent.setLayoutY(110);
        label_DetailContent.setPrefWidth(450);
        label_DetailContent.setPrefHeight(280);
        label_DetailContent.setWrapText(true);
        label_DetailContent.setAlignment(Pos.TOP_LEFT);
        label_DetailContent.setStyle("-fx-border-color: lightgray; -fx-padding: 6;");

        // ── Delete button ─────────────────────────────────────────────────────────────────────
        // US-S6: delete with "Are you sure?" confirmation
        setupButtonUI(button_DeletePost, "Arial", 14, 130, Pos.CENTER, 500, 400);
        button_DeletePost.setOnAction((_) -> {
            if (currentPostID == -1) {
                label_ErrorMessage.setText("Select a post first.");
                return;
            }
            // US-S6: show "Are you sure?" confirmation before deleting
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Delete Post");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete this post?");
            Optional<ButtonType> response = confirm.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.OK) {
                // User confirmed — proceed with soft delete
                ControllerStudentPosts.doDeletePost(currentPostID);
            }
        });

        // ── Search section ────────────────────────────────────────────────────────────────────
        // US-S4, US-S7: keyword search with optional thread filter
        setupLabelUI(label_SearchSection, "Arial", 16, 200, Pos.BASELINE_LEFT, 20, 400);

        setupTextUI(text_SearchKeyword, "Arial", 14, 200, Pos.BASELINE_LEFT, 20, 425, true);
        text_SearchKeyword.setPromptText("Search keyword");

        // US-S7: blank thread = all threads searched
        setupTextUI(text_SearchThread, "Arial", 14, 150, Pos.BASELINE_LEFT, 230, 425, true);
        text_SearchThread.setPromptText("Thread (optional)");

        setupButtonUI(button_Search, "Arial", 14, 100, Pos.CENTER, 390, 425);
        button_Search.setOnAction((_) -> ControllerStudentPosts.doSearch());

        // ── New Post form ─────────────────────────────────────────────────────────────────────
        // US-S1, US-S5: create a new post
        setupLabelUI(label_NewPostSection, "Arial", 16, 200, Pos.BASELINE_LEFT, 20, 470);

        setupTextUI(text_PostTitle, "Arial", 14, 300, Pos.BASELINE_LEFT, 20, 500, true);
        text_PostTitle.setPromptText("Post title (required)");

        // US-S5: "General" shown as prompt — controller defaults to it if blank
        setupTextUI(text_ThreadName, "Arial", 14, 150, Pos.BASELINE_LEFT, 330, 500, true);
        text_ThreadName.setPromptText("Thread (default: General)");

        // US-S1: post type selector
        combo_PostType.setItems(FXCollections.observableArrayList("question", "statement"));
        combo_PostType.setValue("question");
        combo_PostType.setLayoutX(490);
        combo_PostType.setLayoutY(500);

        text_PostBody.setLayoutX(20);
        text_PostBody.setLayoutY(535);
        text_PostBody.setPrefWidth(550);
        text_PostBody.setPrefHeight(80);
        text_PostBody.setPromptText("Post body (required)");
        text_PostBody.setFont(Font.font("Arial", 14));
        text_PostBody.setWrapText(true);

        setupButtonUI(button_SubmitPost, "Arial", 14, 100, Pos.CENTER, 580, 555);
        button_SubmitPost.setOnAction((_) -> ControllerStudentPosts.doCreatePost());

        // ── Reply form ────────────────────────────────────────────────────────────────────────
        // US-S1: reply to the currently selected post
        setupLabelUI(label_ReplySection, "Arial", 16, 200, Pos.BASELINE_LEFT, 20, 630);

        text_ReplyBody.setLayoutX(20);
        text_ReplyBody.setLayoutY(660);
        text_ReplyBody.setPrefWidth(550);
        text_ReplyBody.setPrefHeight(60);
        text_ReplyBody.setPromptText("Write a reply...");
        text_ReplyBody.setFont(Font.font("Arial", 14));
        text_ReplyBody.setWrapText(true);

        setupButtonUI(button_SubmitReply, "Arial", 14, 130, Pos.CENTER, 580, 672);
        button_SubmitReply.setOnAction((_) -> {
            if (currentPostID == -1) {
                label_ErrorMessage.setText("Select a post to reply to.");
            } else {
                ControllerStudentPosts.doCreateReply(currentPostID);
            }
        });

        // ── Error / status message ────────────────────────────────────────────────────────────
        setupLabelUI(label_ErrorMessage, "Arial", 14, width, Pos.BASELINE_LEFT, 20, 735);
        label_ErrorMessage.setStyle("-fx-text-fill: red;");

        // ── Add all widgets to the pane ───────────────────────────────────────────────────────
        theRootPane.getChildren().addAll(
            label_PageTitle, label_WelcomeUser,
            button_AllPosts, button_MyPosts, button_UnreadOnly,
            listArea_Posts,
            label_DetailContent,
            button_DeletePost,
            label_SearchSection, text_SearchKeyword, text_SearchThread, button_Search,
            label_NewPostSection, text_PostTitle, text_ThreadName, combo_PostType,
            text_PostBody, button_SubmitPost,
            label_ReplySection, text_ReplyBody, button_SubmitReply,
            label_ErrorMessage
        );
    }


    /*-******************************************************************************************

    Helper methods — identical pattern to ViewUserLogin

    */

    /**
     * Initialises a Label's font, width, alignment, and position.
     * Same signature and behaviour as in {@code ViewUserLogin}.
     */
    private void setupLabelUI(Label l, String ff, double f, double w,
                               Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    /**
     * Initialises a Button's font, width, alignment, and position.
     * Same signature and behaviour as in {@code ViewUserLogin}.
     */
    private void setupButtonUI(Button b, String ff, double f, double w,
                                Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

    /**
     * Initialises a TextField's font, width, alignment, position, and editability.
     * Same signature and behaviour as in {@code ViewUserLogin}.
     */
    private void setupTextUI(TextField t, String ff, double f, double w,
                              Pos p, double x, double y, boolean e) {
        t.setFont(Font.font(ff, f));
        t.setMinWidth(w);
        t.setMaxWidth(w);
        t.setAlignment(p);
        t.setLayoutX(x);
        t.setLayoutY(y);
        t.setEditable(e);
    }
}
