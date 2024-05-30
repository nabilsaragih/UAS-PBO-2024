package org.example.tugaspbocrud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private ChoiceBox<String> cbJenisObat;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnTambah;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnHapus;
    @FXML
    private Button btnLogOut;
    @FXML
    private TextField txtNamaObat;
    @FXML
    private RadioButton rbBebas;
    @FXML
    private RadioButton rbBebasTerbatas;
    @FXML
    private RadioButton rbKeras;
    @FXML
    private RadioButton rbNarkotika;
    @FXML
    private RadioButton rbFito;
    @FXML
    private DatePicker dtExpired;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private TableView<Obat> tblViewObat;
    @FXML
    private TableColumn<Obat, String> colID;
    @FXML
    private TableColumn<Obat, String> colNamaObat;
    @FXML
    private TableColumn<Obat, String> colJenisObat;
    @FXML
    private TableColumn<Obat, String> colGolonganObat;
    @FXML
    private TableColumn<Obat, String> colTanggalExpired;

    private static String selectedItemID;

    private void addItem() {
        boolean nullCheck = false;

        if (!(rbBebas.isSelected() || rbBebasTerbatas.isSelected() || rbKeras.isSelected() || rbNarkotika.isSelected() || rbFito.isSelected())) {
            nullCheck = true;
        }

        if (txtNamaObat.getText().isBlank() || cbJenisObat.getValue().isBlank() || (dtExpired.getValue() == null)) {
            nullCheck = true;
        }

        if (nullCheck) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter blank or empty text field");
        } else {
            String golonganObat = "";
            if (rbBebas.isSelected()) {
                golonganObat = rbBebas.getText();
            } else if (rbBebasTerbatas.isSelected()) {
                golonganObat= rbBebasTerbatas.getText();
            } else if (rbKeras.isSelected()) {
                golonganObat = rbKeras.getText();
            } else if (rbNarkotika.isSelected()) {
                golonganObat = rbNarkotika.getText();
            } else if (rbFito.isSelected()) {
                golonganObat = rbFito.getText();
            }

            try (Connection conn = Koneksi.DBConnect()) {
                String query = "INSERT INTO data_obat (nama_obat, jenis_obat, golongan_obat, tanggal_expired) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, txtNamaObat.getText());
                preparedStatement.setString(2, cbJenisObat.getValue());
                preparedStatement.setString(3, golonganObat);
                preparedStatement.setString(4, dtExpired.getValue().format(DateTimeFormatter.ISO_DATE));

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Data Inserted Successfully");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to Insert Data");
                }

                tblViewObat.getItems().clear();
                showItem();

                clear();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while inserting data");
            }
        }
    }

    private void showItem() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
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

    private void updateData() {
        String golonganObat = "";
        if (rbBebas.isSelected()) {
            golonganObat = rbBebas.getText();
        } else if (rbBebasTerbatas.isSelected()) {
            golonganObat = rbBebasTerbatas.getText();
        } else if (rbKeras.isSelected()) {
            golonganObat = rbKeras.getText();
        } else if (rbNarkotika.isSelected()) {
            golonganObat = rbNarkotika.getText();
        } else if (rbFito.isSelected()) {
            golonganObat = rbFito.getText();
        }

        try (Connection conn = Koneksi.DBConnect()) {
            String updateQuery = "UPDATE data_obat SET nama_obat = ?, jenis_obat = ?, golongan_obat = ?, tanggal_expired = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
            preparedStatement.setString(1, txtNamaObat.getText());
            preparedStatement.setString(2, cbJenisObat.getValue());
            preparedStatement.setString(3, golonganObat);
            preparedStatement.setString(4, dtExpired.getValue().format(DateTimeFormatter.ISO_DATE));
            preparedStatement.setString(5, selectedItemID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data Updated Successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to Update Data");
            }

            tblViewObat.getItems().clear();
            showItem();

            clear();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating data");
        }
    }

    private void deleteData() {
        try (Connection conn = Koneksi.DBConnect()) {
            String deleteQuery = "DELETE FROM data_obat WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
            preparedStatement.setString(1, selectedItemID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data Deleted Successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to Delete Data");
            }

            tblViewObat.getItems().clear();
            showItem();

            clear();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting data");
        }
    }

    private void extractFromTable(Obat newValue) {
        String golongan;
        if (newValue != null) {
            selectedItemID = newValue.getId();
            txtNamaObat.setText(newValue.getNamaObat());
            cbJenisObat.setValue(newValue.getJenisObat());
            golongan = newValue.getGolonganObat();
            switch (golongan) {
                case "Bebas" -> rbBebas.setSelected(true);
                case "Bebas Terbatas" -> rbBebasTerbatas.setSelected(true);
                case "Keras" -> rbKeras.setSelected(true);
                case "Narkotika" -> rbNarkotika.setSelected(true);
                case "Fitofarmatika" -> rbFito.setSelected(true);
            }
            String dateString = newValue.getTanggalExpired();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            dtExpired.setValue(localDate);
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clear() {
        txtNamaObat.clear();
        cbJenisObat.getSelectionModel().clearSelection();
        rbBebas.setSelected(false);
        rbBebasTerbatas.setSelected(false);
        rbKeras.setSelected(false);
        rbNarkotika.setSelected(false);
        rbFito.setSelected(false);
        dtExpired.setValue(null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showItem();

        toggleGroup = new ToggleGroup();
        rbBebas.setToggleGroup(toggleGroup);
        rbBebasTerbatas.setToggleGroup(toggleGroup);
        rbKeras.setToggleGroup(toggleGroup);
        rbNarkotika.setToggleGroup(toggleGroup);
        rbFito.setToggleGroup(toggleGroup);

        ObservableList<String> jenisList = FXCollections.observableArrayList(
                "Tablet", "Kapsul", "Pil", "Salep", "Krim", "Obat Tetes", "Sirup"
        );
        cbJenisObat.setItems(jenisList);

        dtExpired.getEditor().setDisable(true);
        dtExpired.getEditor().setOpacity(1);

        btnCancel.setOnAction(actionEvent -> {
            clear();
        });

        btnTambah.setOnAction(actionEvent -> {
            addItem();
        });

        tblViewObat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            extractFromTable(newValue);
        });

        btnUbah.setOnAction(actionEvent -> {
            updateData();
        });

        btnHapus.setOnAction(actionEvent -> {
            deleteData();
        });

        btnLogOut.setOnAction(actionEvent -> {
            try {
                LogOut();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
