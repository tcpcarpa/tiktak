module com.example.t {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens com.example.tres_en_ralla to javafx.fxml;
    exports com.example.tres_en_ralla;
}