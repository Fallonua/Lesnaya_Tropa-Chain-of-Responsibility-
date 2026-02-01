package lesnayatropa.handler;

import javafx.scene.control.Alert;
import lesnayatropa.chain.ForestPathChain;
import lesnayatropa.model.GameContext;

/**
 * Обработчик события «Выход из леса»: победа
 * Устанавливает context.setVictory(true), показывает поздравление и возвращает false (игра завершена)
 * Вызывается только если ForestPathChain выдал тип EXIT
 */
public class ExitHandler extends Handler {

    public ExitHandler(Handler processor) {
        super(processor);
    }

    @Override
    public boolean process(Integer request, GameContext context) {
        if (request != ForestPathChain.EXIT) {
            return super.process(request, context);
        }
        context.setVictory(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Победа!");
        alert.setHeaderText("Выход из леса!");
        alert.setContentText("Поздравляем! Вы нашли выход из леса. Монет: " + context.getPlayer().getCoins());
        alert.showAndWait();
        return false; // конец игры (победа)
    }
}
