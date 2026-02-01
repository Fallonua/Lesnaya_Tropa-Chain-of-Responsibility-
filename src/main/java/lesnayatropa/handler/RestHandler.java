package lesnayatropa.handler;

import javafx.scene.control.Alert;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;

import java.util.Random;

/**
 * Обработчик события «Привал»: восстанавливает энергию на случайную величину (2–4).
 * Игрок.addEnergy ограничивает энергию значением 10.
 * Если тип запроса не REST — передаёт запрос дальше по цепочке.
 */
public class RestHandler extends Handler {

    private static final int MIN_ENERGY = 2;
    private static final int MAX_ENERGY = 4;
    private final Random random = new Random();

    public RestHandler(Handler processor) {
        super(processor);
    }

    @Override
    public boolean process(Integer request, GameContext context) {
        if (request != ForestPathChain.REST) {
            return super.process(request, context);
        }
        int gain = MIN_ENERGY + random.nextInt(MAX_ENERGY - MIN_ENERGY + 1);
        int before = context.getPlayer().getEnergy();
        context.getPlayer().addEnergy(gain);
        int actualGain = context.getPlayer().getEnergy() - before;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Привал");
        alert.setHeaderText("Привал!");
        alert.setContentText("Энергия восстановлена на " + actualGain + ". Всего энергии: " + context.getPlayer().getEnergy());
        alert.showAndWait();
        return true;
    }
}
