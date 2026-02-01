package lesnayatropa.handler;

import javafx.scene.control.Alert;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;

import java.util.Random;

/**
 * Обработчик события «Волшебный источник»: восстанавливает здоровье на случайную величину (2–4).
 * Игрок.addHealth сам ограничивает здоровье значением 10.
 * Если тип запроса не MAGIC_SPRING — передаёт запрос дальше по цепочке.
 */
public class MagicSpringHandler extends Handler {

    private static final int MIN_HEAL = 2;
    private static final int MAX_HEAL = 4;
    private final Random random = new Random();

    public MagicSpringHandler(Handler processor) {
        super(processor);
    }

    @Override
    public boolean process(Integer request, GameContext context) {
        if (request != ForestPathChain.MAGIC_SPRING) {
            return super.process(request, context);
        }
        int heal = MIN_HEAL + random.nextInt(MAX_HEAL - MIN_HEAL + 1);
        int before = context.getPlayer().getHealth();
        context.getPlayer().addHealth(heal);
        int actualHeal = context.getPlayer().getHealth() - before;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Волшебный источник");
        alert.setHeaderText("Волшебный источник!");
        alert.setContentText("Здоровье восстановлено на " + actualHeal + ". Всего здоровья: " + context.getPlayer().getHealth());
        alert.showAndWait();
        return true;
    }
}
