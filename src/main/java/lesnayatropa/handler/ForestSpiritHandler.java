package lesnayatropa.handler;

import javafx.scene.control.Alert;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;

import java.util.Random;

/**
 * Обработчик события «Встреча с лесным духом»: игрок получает случайное количество монет (2–5).
 * Если тип запроса не FOREST_SPIRIT — передаёт запрос дальше по цепочке.
 */
public class ForestSpiritHandler extends Handler {

    private static final int MIN_COINS = 2;
    private static final int MAX_COINS = 5;
    private final Random random = new Random();

    public ForestSpiritHandler(Handler processor) {
        super(processor);
    }

    @Override
    public boolean process(Integer request, GameContext context) {
        if (request != ForestPathChain.FOREST_SPIRIT) {
            return super.process(request, context);
        }
        int coins = MIN_COINS + random.nextInt(MAX_COINS - MIN_COINS + 1);
        context.getPlayer().addCoins(coins);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Лесной дух");
        alert.setHeaderText("Встреча с лесным духом!");
        alert.setContentText("Дух одарил вас " + coins + " монетами. Всего монет: " + context.getPlayer().getCoins());
        alert.showAndWait();
        return true; // игра продолжается
    }
}
