package org.example.tugaspbocrud;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtUlangPassword;
    @FXML
    private Button btnUbah;
    @FXML
    private Label lblSignIn;

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
            txtUlangPassword.clear();
        } else {
            txtPassword.clear();
            txtUlangPassword.clear();
        }
    }

    private void Login() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 450);
        scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/login.css").toExternalForm());
        Stage stage = (Stage) btnUbah.getScene().getWindow();
        stage.setResizable(false);
        stage.setTitle("Sistem Manajemen Apotek");
        stage.setScene(scene);
        stage.show();
    }

    private void UbahPassword() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String ulangPassword = txtUlangPassword.getText();

        if (username.isBlank() || password.isBlank() || ulangPassword.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a valid username or password");
            clear(true);
            return;
        }

        if (!password.equals(ulangPassword)) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "The passwords you entered do not match");
            clear(false);
            return;
        }

        try (Connection conn = Koneksi.DBConnect()) {
            String checkQuery = "SELECT username FROM user_creds WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (!resultSet.next()) {
                        showAlert(Alert.AlertType.WARNING, "Credentials Error", "Username not found");
                        clear(true);
                        return;
                    }
                }
            }


            String insertQuery = "UPDATE user_creds SET password = ? WHERE username = ?";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, password);
                insertStmt.setString(2, username);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Password was updated successfully");
                    Login();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUbah.setOnAction(actionEvent -> {
            UbahPassword();
        });

        lblSignIn.setOnMouseClicked(actionEvent -> {
            try {
                Login();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
