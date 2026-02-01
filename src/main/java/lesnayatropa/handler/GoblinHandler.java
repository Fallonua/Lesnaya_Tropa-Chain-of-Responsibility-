package lesnayatropa.handler;

import javafx.scene.control.Alert;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;

import java.util.Random;

/**
 * Обработчик события «Нападение гоблина»: игрок теряет случайное количество монет (1–4).
 * Списывается не больше, чем есть у игрока (монеты не уходят в минус).
 * Если тип запроса не GOBLIN — передаёт запрос дальше по цепочке.
 */
public class GoblinHandler extends Handler {

    private static final int MIN_LOSS = 1;
    private static final int MAX_LOSS = 4;
    private final Random random = new Random();

    public GoblinHandler(Handler processor) {
        super(processor);
    }

    @Override
    public boolean process(Integer request, GameContext context) {
        if (request != ForestPathChain.GOBLIN) {
            return super.process(request, context);
        }
        int loss = MIN_LOSS + random.nextInt(MAX_LOSS - MIN_LOSS + 1);
        int coins = context.getPlayer().getCoins();
        int actualLoss = Math.min(loss, coins);
        context.getPlayer().subtractCoins(actualLoss);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Гоблин");
        alert.setHeaderText("Нападение гоблина!");
        alert.setContentText("Вы потеряли " + actualLoss + " монет. Осталось: " + context.getPlayer().getCoins());
        alert.showAndWait();
        return true; // игра продолжается
    }
}
