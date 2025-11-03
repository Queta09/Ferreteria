module org.example.f {

    // 1. Dependencias externas (Módulos de Framework)
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base; // Necesario para la reflexión de PropertyValueFactory

    // 2. Apertura/Exportación para JavaFX y Reflection (Solución clave)

    // Abre el paquete de controladores al módulo javafx.fxml para que cargue los controladores
    opens org.example.f.controles to javafx.fxml;

    // 🛑 SOLUCIÓN CLAVE: Abre el paquete de modelos a javafx.base
    // Esto permite que el TableView use reflexión (PropertyValueFactory) sin exportar el módulo base
    opens org.example.f.modelos to javafx.base;

    // Esto permite al lanzador de JavaFX acceder y crear tu clase FerreteriaApp.
    opens org.example.f to javafx.graphics;

    // 🛑 [OPCIONAL] Si necesitas acceder a los Managers/Servicios desde fuera del módulo (poco común)
    exports org.example.f.servicios;

    // 🛑 [OPCIONAL] Si el MainSystemController necesita ser accesible
    exports org.example.f.controles;

    // NOTA: La línea 'exports org.example.f;' es la que causaba la dependencia circular y ha sido eliminada.
}