package lesnayatropa.handler;

import javafx.scene.control.Alert;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;

import java.util.Random;

/**
 * Обработчик события «Ловушка»: игрок теряет здоровье на случайную величину (2–4).
 * Если после урона здоровье ≤ 0 — устанавливается поражение (context.setDefeat(true)), показывается Alert и возвращается false.
 * Если тип запроса не TRAP — передаёт запрос дальше по цепочке.
 */
public class TrapHandler extends Handler {

    private static final int MIN_DAMAGE = 2;
    private static final int MAX_DAMAGE = 4;
    private final Random random = new Random();

    public TrapHandler(Handler processor) {
        super(processor);
    }

    @Override
    public boolean process(Integer request, GameContext context) {
        if (request != ForestPathChain.TRAP) {
            return super.process(request, context);
        }
        int damage = MIN_DAMAGE + random.nextInt(MAX_DAMAGE - MIN_DAMAGE + 1);
        context.getPlayer().subtractHealth(damage);
        int health = context.getPlayer().getHealth();
        if (health <= 0) {
            context.setDefeat(true);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Поражение");
            alert.setHeaderText("Ловушка!");
            alert.setContentText("Вы потеряли слишком много здоровья. Игра окончена.");
            alert.showAndWait();
            return false; // конец игры
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ловушка");
        alert.setHeaderText("Ловушка!");
        alert.setContentText("Вы получили " + damage + " урона. Осталось здоровья: " + health);
        alert.showAndWait();
        return true;
    }
}
