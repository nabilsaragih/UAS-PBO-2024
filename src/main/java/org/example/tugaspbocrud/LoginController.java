package org.example.tugaspbocrud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblLupa;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblRegister;

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clear(boolean withUsername) {
        if (withUsername) {
            txtUsername.clear();
            txtPassword.clear();
        } else {
            txtPassword.clear();
        }
    }

    private void Register() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 450);
        scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/register.css").toExternalForm());
        Stage stage = (Stage) lblRegister.getScene().getWindow();
        stage.setResizable(false);
        stage.setTitle("Sistem Manajemen Apotek");
        stage.setScene(scene);
        stage.show();
    }

    private void Login() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try (Connection conn = Koneksi.DBConnect()) {
            String query = "SELECT * FROM user_creds WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String credsRole = resultSet.getString(4);
                showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome, " + username + "!");
                if (credsRole.equals("admin")) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 745, 645);
                    scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/hello-view.css").toExternalForm());
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setResizable(false);
                    stage.setTitle("Sistem Manajemen Apotek");
                    stage.setScene(scene);
                    stage.show();
                } else {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 745, 645);
                    scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/user-view.css").toExternalForm());
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setResizable(false);
                    stage.setTitle("Sistem Manajemen Apotek");
                    stage.setScene(scene);
                    stage.show();
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter valid username and password.");
                clear(true);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    private void ForgotPassword() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("forgot-password.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 450);
        scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/forgot-password.css").toExternalForm());
        Stage stage = (Stage) lblLupa.getScene().getWindow();
        stage.setResizable(false);
        stage.setTitle("Sistem Manajemen Apotek");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLogin.setOnAction(actionEvent -> {
            Login();
        });

        lblRegister.setOnMouseClicked(actionEvent -> {
            try {
                Register();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        lblLupa.setOnMouseClicked(actionEvent -> {
            try {
                ForgotPassword();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
