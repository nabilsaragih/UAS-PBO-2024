module org.example.tugaspbocrud {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;

    opens org.example.tugaspbocrud to javafx.fxml;
    exports org.example.tugaspbocrud;
}