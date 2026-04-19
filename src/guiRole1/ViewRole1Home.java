package guiRole1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;


/*******
 * <p> Title: ViewRole1Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role1 (Student) Home Page.  This page serves as the
 * student's landing page after login.  It provides access to account update, the Student
 * Discussion System (TP2), logout, and quit.
 * 
 * <p><b>TP2 Addition:</b> A "Discussion" button has been added to GUI Area 2 that launches
 * the {@code guiStudentPosts.ViewStudentPosts} page, satisfying all Student User Stories
 * (US-S1 through US-S7) from the Student Discussion System requirements. </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * @author Manisha (TP2: added Discussion button)
 * 
 * @version 1.00    2025-08-20 Initial version
 * @version 1.01    2026-03-19 TP2: added Discussion button linking to guiStudentPosts
 *  
 */

public class ViewRole1Home {
	
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	private static double width  = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// GUI Area 1: page title, user details, account update button
	protected static Label  label_PageTitle      = new Label();
	protected static Label  label_UserDetails    = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: Student Discussion button (TP2 addition)
	
	/**
	 * Launches the Student Discussion System page.
	 * Satisfies all Student User Stories (US-S1 through US-S7) in TP2.
	 * Source: TP2 Task 4 — MVC GUI packages for Student User Stories.
	 */
	protected static Button button_Discussion = new Button("Student Discussion");

	protected static Line line_Separator4 = new Line(20, 525, width-20, 525);
	
	// GUI Area 3: logout and quit
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit   = new Button("Quit");

	private static ViewRole1Home theView;

	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;
	protected static Pane  theRootPane;
	protected static User  theUser;

	private static Scene theViewRole1HomeScene;
	protected static final int theRole = 2;


	/*-*******************************************************************************************

	Constructors
	
	 */

	/**********
	 * <p> Method: displayRole1Home(Stage, User) </p>
	 * 
	 * <p> Description: Single entry point from outside this package to display the Role1 Home
	 * page.  Sets up shared attributes, instantiates the singleton view if needed, populates
	 * dynamic fields, and shows the scene. </p>
	 * 
	 * @param ps    The JavaFX Stage to use
	 * @param user  The currently logged-in User
	 */
	public static void displayRole1Home(Stage ps, User user) {
		theStage = ps;
		theUser  = user;
		
		if (theView == null) theView = new ViewRole1Home();
		
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());
				
		theStage.setTitle("CSE 360 Foundations: Role1 Home Page");
		theStage.setScene(theViewRole1HomeScene);
		theStage.show();
	}
	
	/**********
	 * <p> Method: ViewRole1Home() </p>
	 * 
	 * <p> Description: Private constructor — singleton.  Initialises all GUI widgets including
	 * the TP2 Discussion button. </p>
	 */
	private ViewRole1Home() {

		theRootPane = new Pane();
		theViewRole1HomeScene = new Scene(theRootPane, width, height);
		
		// GUI Area 1
		label_PageTitle.setText("Role1 Home Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> { ControllerRole1Home.performUpdate(); });

		// GUI Area 2 — TP2: Discussion button
		// Launches the Student Discussion System, satisfying US-S1 through US-S7.
		setupButtonUI(button_Discussion, "Dialog", 18, 250, Pos.CENTER, 280, 200);
		button_Discussion.setOnAction((_) -> {
			// US-S1 through US-S7: navigate to the Student Discussion page
			guiStudentPosts.ViewStudentPosts.displayStudentPosts(theStage, theUser);
		});
		
		// GUI Area 3
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((_) -> { ControllerRole1Home.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((_) -> { ControllerRole1Home.performQuit(); });

		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			button_Discussion,                          // TP2 addition
	        line_Separator4, button_Logout, button_Quit);
	}
	
	
	/*-********************************************************************************************

	Helper methods

	 */
	
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, 
			double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, 
			double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
}
