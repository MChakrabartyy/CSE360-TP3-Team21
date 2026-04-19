package guiFirstAdmin;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ViewFirstAdmin {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static Label label_ApplicationTitle = new Label("Foundation Application Startup Page");
    private static Label label_TitleLine1 = 
            new Label(" You are the first user.  You must be an administrator.");
    
    private static Label label_TitleLine2 = 
            new Label("Enter the Admin's Username, the Password twice, and then click on " + 
                    "Setup Admin Account.");
    
    protected static Label label_PasswordsDoNotMatch = new Label();
    protected static TextField text_AdminUsername = new TextField();
    protected static PasswordField text_AdminPassword1 = new PasswordField();
    protected static PasswordField text_AdminPassword2 = new PasswordField();
    private static Button button_AdminSetup = new Button("Setup Admin Account");
    
    // Error labels for real-time validation
    protected static Label label_UsernameError = new Label("");
    protected static Label label_PasswordRequirements = new Label("");

    protected static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);

    private static Button button_Quit = new Button("Quit");

    protected static Stage theStage;    
    private static Pane theRootPane;
    private static Scene theFirstAdminScene = null;
    private static final int theRole = 1;
        
    public static void displayFirstAdmin(Stage ps) {
        theStage = ps;
        new ViewFirstAdmin();
        applicationMain.FoundationsMain.activeHomePage = theRole;
        theStage.setTitle("CSE 360 Foundation Code: First User Account Setup");    
        theStage.setScene(theFirstAdminScene);
        theStage.show();
    }

    private ViewFirstAdmin() {

        theRootPane = new Pane();
        theFirstAdminScene = new Scene(theRootPane, width, height);

        // Title
        setupLabelUI(label_ApplicationTitle, "Arial", 32, width, Pos.CENTER, 0, 10);

        // Subtitle line 1
        setupLabelUI(label_TitleLine1, "Arial", 24, width, Pos.CENTER, 0, 70);

        // Subtitle line 2
        setupLabelUI(label_TitleLine2, "Arial", 18, width, Pos.CENTER, 0, 130);

        // Username field
        setupTextUI(text_AdminUsername, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 180, true);
        text_AdminUsername.setPromptText("Enter Admin Username");
        text_AdminUsername.textProperty().addListener((obs, oldVal, newVal) -> {
            ControllerFirstAdmin.setAdminUsername();
            if (newVal.length() > 0) {
                String error = Validators.UsernameValidator.checkUsername(newVal);
                label_UsernameError.setText(error);
            } else {
                label_UsernameError.setText("");
            }
        });

        // Username error label (to the right of username field)
        setupLabelUI(label_UsernameError, "Arial", 14, 350, Pos.BASELINE_LEFT, 360, 185);
        label_UsernameError.setStyle("-fx-text-fill: red;");

        // Password field 1
        setupTextUI(text_AdminPassword1, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 230, true);
        text_AdminPassword1.setPromptText("Enter Admin Password");
        text_AdminPassword1.textProperty().addListener((obs, oldVal, newVal) -> {
            ControllerFirstAdmin.setAdminPassword1();
            // Real-time password validation feedback
            if (newVal.length() > 0) {
                validator.PasswordValidator pv = new validator.PasswordValidator();
                pv.validate(newVal);
                StringBuilder req = new StringBuilder();
                req.append(pv.hasMinLength() ? "✓" : "✗").append(" At least 8 characters\n");
                req.append(pv.hasUppercase() ? "✓" : "✗").append(" One uppercase (A-Z)\n");
                req.append(pv.hasLowercase() ? "✓" : "✗").append(" One lowercase (a-z)\n");
                req.append(pv.hasDigit() ? "✓" : "✗").append(" One digit (0-9)\n");
                req.append(pv.hasSpecialCharacter() ? "✓" : "✗").append(" One special (!@#$%^&*)");
                label_PasswordRequirements.setText(req.toString());
            } else {
                label_PasswordRequirements.setText("");
            }
        });

        // Password requirements label (to the right of password field)
        setupLabelUI(label_PasswordRequirements, "Arial", 11, 300, Pos.BASELINE_LEFT, 360, 230);
        label_PasswordRequirements.setStyle("-fx-text-fill: #555;");

        // Password field 2
        setupTextUI(text_AdminPassword2, "Arial", 18, 300, Pos.BASELINE_LEFT, 50, 280, true);
        text_AdminPassword2.setPromptText("Enter Admin Password Again");
        text_AdminPassword2.textProperty().addListener((obs, oldVal, newVal) -> {
            ControllerFirstAdmin.setAdminPassword2();
        });

        // Setup Admin button
        setupButtonUI(button_AdminSetup, "Dialog", 18, 200, Pos.CENTER, 50, 340);
        button_AdminSetup.setOnAction(e -> {
            ControllerFirstAdmin.doSetupAdmin(theStage, 1); 
        });

        // Error message label (below everything)
        setupLabelUI(label_PasswordsDoNotMatch, "Arial", 14, width, Pos.CENTER, 0, 400);
        label_PasswordsDoNotMatch.setStyle("-fx-text-fill: red;");

        // Quit button
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 520);
        button_Quit.setOnAction(e -> {
            ControllerFirstAdmin.performQuit(); 
        });

        // Add all elements to pane
        theRootPane.getChildren().addAll(
            label_ApplicationTitle, 
            label_TitleLine1,
            label_TitleLine2, 
            text_AdminUsername, 
            label_UsernameError,
            text_AdminPassword1, 
            label_PasswordRequirements,
            text_AdminPassword2, 
            button_AdminSetup, 
            label_PasswordsDoNotMatch,
            button_Quit
        );
    }
    
    private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);        
    }

    private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);        
    }

    private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, 
            boolean e){
        t.setFont(Font.font(ff, f));
        t.setMinWidth(w);
        t.setMaxWidth(w);
        t.setAlignment(p);
        t.setLayoutX(x);
        t.setLayoutY(y);        
        t.setEditable(e);
    }    
}