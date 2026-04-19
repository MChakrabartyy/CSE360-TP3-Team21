package guiAdminHome;

import database.Database;
import guiAdminResetPassword.ViewAdminResetPassword;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void setOnetimePassword () {
	    guiAdminResetPassword.ViewAdminResetPassword.display(
	        ViewAdminHome.theStage, ViewAdminHome.theUser);
	}
	
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void deleteUser() {
	    // Get list of users
	    java.util.List<String> users = theDatabase.getUserList();
	    
	    // Create choice dialog
	    javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(users.get(0), users);
	    dialog.setTitle("Delete User");
	    dialog.setHeaderText("Select a user to delete");
	    dialog.setContentText("User:");
	    
	    // Show dialog and get result
	    java.util.Optional<String> result = dialog.showAndWait();
	    
	    if (result.isPresent() && !result.get().equals("<Select a User>")) {
	        String selectedUser = result.get();
	        
	        // Don't allow deleting yourself
	        if (selectedUser.equals(ViewAdminHome.theUser.getUserName())) {
	            ViewAdminHome.alertNotImplemented.setTitle("Error");
	            ViewAdminHome.alertNotImplemented.setHeaderText("Cannot Delete");
	            ViewAdminHome.alertNotImplemented.setContentText("You cannot delete your own account!");
	            ViewAdminHome.alertNotImplemented.showAndWait();
	            return;
	        }
	        
	        // Confirm deletion
	        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
	        confirm.setTitle("Confirm Delete");
	        confirm.setHeaderText("Delete user: " + selectedUser + "?");
	        confirm.setContentText("This action cannot be undone.");
	        
	        java.util.Optional<javafx.scene.control.ButtonType> confirmResult = confirm.showAndWait();
	        if (confirmResult.isPresent() && confirmResult.get() == javafx.scene.control.ButtonType.OK) {
	            // Delete the user
	            if (theDatabase.deleteUser(selectedUser)) {
	                ViewAdminHome.alertNotImplemented.setTitle("Success");
	                ViewAdminHome.alertNotImplemented.setHeaderText("User Deleted");
	                ViewAdminHome.alertNotImplemented.setContentText("User '" + selectedUser + "' has been deleted.");
	                ViewAdminHome.alertNotImplemented.showAndWait();
	                
	                // Update user count
	                ViewAdminHome.label_NumberOfUsers.setText("Number of users: " + theDatabase.getNumberOfUsers());
	            } else {
	                ViewAdminHome.alertNotImplemented.setTitle("Error");
	                ViewAdminHome.alertNotImplemented.setHeaderText("Delete Failed");
	                ViewAdminHome.alertNotImplemented.setContentText("Could not delete user.");
	                ViewAdminHome.alertNotImplemented.showAndWait();
	            }
	        }
	    }
	}
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {
	    // Get list of users from database
	    java.util.List<String> users = theDatabase.getUserList();
	    
	    // Build display string
	    StringBuilder userList = new StringBuilder();
	    userList.append("Total Users: ").append(users.size() - 1).append("\n\n"); // -1 for "<Select a User>"
	    
	    for (String user : users) {
	        if (!user.equals("<Select a User>")) {
	            userList.append("• ").append(user).append("\n");
	        }
	    }
	    
	    // Show in alert
	    ViewAdminHome.alertNotImplemented.setTitle("All Users");
	    ViewAdminHome.alertNotImplemented.setHeaderText("Users in System");
	    ViewAdminHome.alertNotImplemented.setContentText(userList.toString());
	    ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that checks if an email address is valid
	 * using the EmailValidator class.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		// Check if empty
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Email address cannot be empty.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		
		// Validate email format using EmailValidator
		Validators.EmailValidator emailValidator = new Validators.EmailValidator();
		if (!emailValidator.validate(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"Invalid email format: " + emailValidator.getErrorMessage());
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
