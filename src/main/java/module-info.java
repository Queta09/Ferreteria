module org.example.f {
    requires javafx.controls;
    requires javafx.fxml;

    // Abrir el paquete de controladores al módulo javafx.fxml para que cargue los controladores
    opens org.example.f.controles to javafx.fxml;
    exports org.example.f;

    // 💡 SOLUCIÓN CLAVE: Abrir el paquete de modelos a javafx.base
    // Esto permite que el TableView use reflection (PropertyValueFactory)
    opens org.example.f.modelos to javafx.base;
}