package lesnayatropa.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;
import lesnayatropa.model.Player;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер главного экрана игры «Лесная тропа».
 * Связывает разметку view.fxml с логикой: создаёт игрока и цепочку, обрабатывает нажатия кнопок выбора пути
 * и кнопки «Начать сначала», обновляет отображение монет/здоровья/энергии и шага.
 */
public class Controller implements Initializable {

    // Элементы интерфейса (привязываются к view.fxml по fx:id)
    @FXML
    private Label coinsLabel;
    @FXML
    private Label healthLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label stepLabel;
    @FXML
    private Button btnLeft;
    @FXML
    private Button btnStraight;
    @FXML
    private Button btnRight;
    @FXML
    private Button btnRestart;

    // Модель игрока (монеты, здоровье, энергия)
    private Player player;
    // Контекст шага (игрок, номер шага, последнее событие, флаги победы/поражения).
    private GameContext context;
    // Цепочка обработчиков и логика «розыгрыша» события.
    private ForestPathChain forestPathChain;
    // true, если игра завершена (победа или поражение) — кнопки пути отключены.
    private boolean gameEnded;

    /**
     * Вызывается после загрузки FXML.
     * Инициализирует игрока, контекст и цепочку, сбрасывает флаг окончания игры, обновляет подписи на экране.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player = new Player();
        context = new GameContext(player);
        forestPathChain = new ForestPathChain();
        gameEnded = false;
        updateLabels();
    }

    /**
     * Синхронизирует отображение на экране с состоянием игрока и контекста:
     * монеты, здоровье, энергия, номер шага.
     */
    private void updateLabels() {
        if (player == null) return;
        coinsLabel.setText(String.valueOf(player.getCoins()));
        healthLabel.setText(String.valueOf(player.getHealth()));
        energyLabel.setText(String.valueOf(player.getEnergy()));
        stepLabel.setText("Шаг: " + context.getStepIndex());
    }

    /**
     * Завершает игру: устанавливает gameEnded = true и отключает кнопки выбора пути
     * (кнопка «Начать сначала» остаётся активной).
     */
    private void endGame() {
        gameEnded = true;
        btnLeft.setDisable(true);
        btnStraight.setDisable(true);
        btnRight.setDisable(true);
    }

    /**
     * Обработчик кнопки «Начать сначала». Создаёт нового игрока, новый контекст и новую цепочку,
     * сбрасывает флаг окончания игры, включает кнопки пути и обновляет подписи.
     */
    @FXML
    public void onRestart(javafx.event.ActionEvent actionEvent) {
        player = new Player();
        context = new GameContext(player);
        forestPathChain = new ForestPathChain();
        gameEnded = false;
        btnLeft.setDisable(false);
        btnStraight.setDisable(false);
        btnRight.setDisable(false);
        updateLabels();
    }

    /**
     * Обработчик нажатия любой из кнопок пути (Налево, Прямо, Направо).
     * Порядок: списание 1 энергии (если 0 — поражение «Недостаточно энергии») → шаг → событие → победа проверяется первой
     * (если при 1 энергии игрок пошёл, энергия стала 0, но выпал Выход — он побеждает).
     */
    @FXML
    public void onPathChosen(javafx.event.ActionEvent actionEvent) {
        if (gameEnded) return;

        // Энергия тратится только при ходе; если энергии нет — нельзя идти
        if (!player.spendEnergy(1)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Поражение");
            alert.setHeaderText("Недостаточно энергии");
            alert.setContentText("Вы не можете идти дальше. Игра окончена.");
            alert.showAndWait();
            endGame();
            return;
        }

        context.incrementStep();
        updateLabels();

        // Обработка события
        boolean continueGame = forestPathChain.process(context);
        updateLabels();

        // Сначала проверяем победу: при 1 энергии игрок пошёл (стало 0), но вышел из леса — победа
        if (context.isVictory()) {
            endGame();
            return;
        }
        // Поражение по здоровью только от Ловушки — сообщение уже показал TrapHandler
        if (context.isDefeat()) {
            endGame();
            return;
        }
        if (!continueGame) {
            endGame();
            return;
        }

        // Энергия закончилась после хода (событие не Выход и не восстановило энергию)
        if (player.getEnergy() <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Поражение");
            alert.setHeaderText("Энергия закончилась");
            alert.setContentText("Вы не можете идти дальше. Игра окончена.");
            alert.showAndWait();
            endGame();
        }
    }
}
