module com.example.wappler_jumper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.wappler_jumper to javafx.fxml;
    exports com.example.wappler_jumper;
}