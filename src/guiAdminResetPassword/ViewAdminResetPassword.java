package guiAdminResetPassword;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

public class ViewAdminResetPassword {

    // Window sizing
    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // UI controls
    protected static ComboBox<String> combo_Users = new ComboBox<>();
    protected static PasswordField text_NewPassword = new PasswordField();
    protected static PasswordField text_ConfirmPassword = new PasswordField();

    protected static Button button_Reset = new Button("Reset Password");
    protected static Button button_Cancel = new Button("Cancel");

    // JavaFX containers
    protected static Stage theStage;
    protected static Scene theScene;
    protected static Pane theRoot;

    // Database reference
    protected static Database theDatabase =
            applicationMain.FoundationsMain.database;

    // Current admin user (IMPORTANT)
    protected static User theAdminUser;

    /**
     * Entry point for this page
     */
    public static void display(Stage stage, User adminUser) {
        theStage = stage;
        theAdminUser = adminUser;

        if (theScene == null) {
            buildUI();
        }

        theStage.setTitle("Admin: Reset User Password");
        theStage.setScene(theScene);
        theStage.show();
    }

    /**
     * Build the GUI once (singleton pattern)
     */
    private static void buildUI() {
        theRoot = new Pane();
        theScene = new Scene(theRoot, width, height);

        // Title
        Label title = new Label("Reset User Password");
        title.setFont(Font.font("Arial", 26));
        title.setMinWidth(width);
        title.setAlignment(Pos.CENTER);
        title.setLayoutY(20);

        // User selection
        combo_Users.setLayoutX(200);
        combo_Users.setLayoutY(100);
        combo_Users.setMinWidth(300);

        // New password
        text_NewPassword.setLayoutX(200);
        text_NewPassword.setLayoutY(160);
        text_NewPassword.setMinWidth(300);
        text_NewPassword.setPromptText("New Password");

        // Confirm password
        text_ConfirmPassword.setLayoutX(200);
        text_ConfirmPassword.setLayoutY(210);
        text_ConfirmPassword.setMinWidth(300);
        text_ConfirmPassword.setPromptText("Confirm Password");

        // Buttons
        button_Reset.setLayoutX(200);
        button_Reset.setLayoutY(270);
        button_Reset.setMinWidth(140);

        button_Cancel.setLayoutX(360);
        button_Cancel.setLayoutY(270);
        button_Cancel.setMinWidth(140);

        // Add everything to the pane
        theRoot.getChildren().addAll(
                title,
                combo_Users,
                text_NewPassword,
                text_ConfirmPassword,
                button_Reset,
                button_Cancel
        );

        // Initialize controller logic WITH admin user
        ControllerAdminResetPassword.initialize(theAdminUser);
    }
}
