module org.example.f {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.f to javafx.fxml;
    exports org.example.f;
}