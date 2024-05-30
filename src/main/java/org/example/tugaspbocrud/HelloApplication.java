package org.example.tugaspbocrud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 450);
        scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/login.css").toExternalForm());
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/assets/icon.png")));
        stage.setResizable(false);
        stage.setTitle("Sistem Manajemen Apotek");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}