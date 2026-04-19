package guiAdminResetPassword;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import guiNewAccount.ModelNewAccount;
import database.Database;
import entityClasses.User;
import guiAdminHome.ViewAdminHome;

/**
 * <p>Title: ControllerAdminResetPassword Class</p>
 * 
 * <p>Description: Controller for Admin Reset Password page.</p>
 *
 * @author Ali
 * @version 1.0
 */
public class ControllerAdminResetPassword {

    /** Database reference */
    private static Database theDatabase =
            applicationMain.FoundationsMain.database;

    /** The admin user who opened this page */
    protected static User adminUser;

    /**
     * Default constructor.
     */
    public ControllerAdminResetPassword() {
    }

    /**
     * Initializes the controller with the admin user.
     * 
     * @param user the admin user
     */
    protected static void initialize(User user) {
        adminUser = user;

        ObservableList<String> users =
                FXCollections.observableArrayList(theDatabase.getUserList());

        ViewAdminResetPassword.combo_Users.setItems(users);

        ViewAdminResetPassword.button_Reset
                .setOnAction(e -> handleReset());

        ViewAdminResetPassword.button_Cancel.setOnAction(e ->
                ViewAdminHome.displayAdminHome(
                        ViewAdminResetPassword.theStage,
                        adminUser
                )
        );
    }

    /**
     * Handles the reset password action.
     */
    private static void handleReset() {
        String username =
                ViewAdminResetPassword.combo_Users.getValue();
        String newPassword =
                ViewAdminResetPassword.text_NewPassword.getText();
        String confirm =
                ViewAdminResetPassword.text_ConfirmPassword.getText();

        if (username == null || username.equals("<Select a User>")) {
            showError("Please select a user.");
            return;
        }

        if (!newPassword.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }

        String validation =
                ModelNewAccount.validatePassword(newPassword);

        if (!validation.isEmpty()) {
            showError(validation);
            return;
        }

        theDatabase.updatePassword(username, newPassword);

        showInfo("Password reset successfully.");

        ViewAdminResetPassword.text_NewPassword.clear();
        ViewAdminResetPassword.text_ConfirmPassword.clear();
    }

    /**
     * Shows an error alert.
     * 
     * @param msg the error message
     */
    private static void showError(String msg) {
        Alert a = new Alert(AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    /**
     * Shows an info alert.
     * 
     * @param msg the info message
     */
    private static void showInfo(String msg) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}