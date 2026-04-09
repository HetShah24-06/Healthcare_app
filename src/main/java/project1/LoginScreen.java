package project1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScreen {

    private MainApp mainApp;
    private Stage stage;
    private Scene scene;

    public LoginScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        createLoginScene();
    }

    private void createLoginScene() {
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label errorMessageLabel = new Label();

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setAlignment(Pos.CENTER);

        layout.add(usernameLabel, 0, 0);
        layout.add(usernameField, 1, 0);
        layout.add(passwordLabel, 0, 1);
        layout.add(passwordField, 1, 1);
        layout.add(loginButton, 1, 2);
        layout.add(errorMessageLabel, 1, 3);

        scene = new Scene(layout, 300, 200);

        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), errorMessageLabel));
    }

    public void show(Stage _stage) {
        this.stage = _stage;
        stage.setScene(scene);
        stage.setTitle("Healthcare App - Login");
        stage.show();
    }

    private void handleLogin(String username, String password, Label errorMessageLabel) {
        if (isValidCredentials(username, password)) {
            mainApp.showDashboard();
        } else {
            errorMessageLabel.setText("Invalid username or password.");
        }
    }

    /**
     * Validates credentials against environment variables APP_USER and APP_PASS.
     * Falls back to default demo credentials if env vars are not set.
     * Set APP_USER and APP_PASS environment variables to override.
     */
    private boolean isValidCredentials(String username, String password) {
        String expectedUser = System.getenv("APP_USER");
        String expectedPass = System.getenv("APP_PASS");

        // Fall back to demo credentials if env vars not configured
        if (expectedUser == null || expectedPass == null) {
            expectedUser = "admin";
            expectedPass = "admin";
        }

        return expectedUser.equals(username) && expectedPass.equals(password);
    }
}
