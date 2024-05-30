package org.example.tugaspbocrud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserViewController implements Initializable {
    @FXML
    private TableView<Obat> tblViewObat;
    @FXML
    private TableColumn<Obat, String> colNamaObat;
    @FXML
    private TableColumn<Obat, String> colJenisObat;
    @FXML
    private TableColumn<Obat, String> colGolonganObat;
    @FXML
    private TableColumn<Obat, String> colTanggalExpired;
    @FXML
    private Button btnLogOut;

    private void viewItem() {
        colNamaObat.setCellValueFactory(cellData -> cellData.getValue().namaObatProperty());
        colJenisObat.setCellValueFactory(cellData -> cellData.getValue().jenisObatProperty());
        colGolonganObat.setCellValueFactory(cellData -> cellData.getValue().golonganObatProperty());
        colTanggalExpired.setCellValueFactory(cellData -> cellData.getValue().tanggalExpiredProperty());

        ObservableList<Obat> items = FXCollections.observableArrayList();

        try (Connection conn = Koneksi.DBConnect()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM data_obat");

            while (resultSet.next()) {
                String ID = resultSet.getString(1);
                String name = resultSet.getString(2);
                String jenis = resultSet.getString(3);
                String golongan = resultSet.getString(4);
                String expired = resultSet.getString(5);
                items.add(new Obat(ID, name, jenis, golongan, expired));
            }

            tblViewObat.setItems(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void LogOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 450);
        scene.getStylesheets().add(HelloApplication.class.getResource("/stylesheet/login.css").toExternalForm());
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.setResizable(false);
        stage.setTitle("Sistem Manajemen Apotek");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewItem();

        btnLogOut.setOnAction(actionEvent -> {
            try {
                LogOut();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
