package lesnayatropa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Точка входа приложения «Лесная тропа».
 * Загружает разметку интерфейса из FXML и отображает главное окно.
 */
public class Main extends Application {

    /** Вызывается при запуске JavaFX-приложения. */
    @Override
    public void start(Stage stage) throws IOException {
        // Загрузка разметки из ресурсов
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Лесная тропа");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /** Запуск JavaFX-приложения */
    public static void main(String[] args) {
        launch(args);
    }
}
