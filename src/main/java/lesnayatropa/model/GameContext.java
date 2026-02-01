package lesnayatropa.model;

/**
 * Контекст одного шага по лесной тропе.
 * Содержит ссылку на игрока, номер текущего шага, тип последнего события
 * и флаги победы/поражения (устанавливаются обработчиками цепочки).
 * Используется при выборе следующего события (resolveEventType) и при обработке (process).
 */
public class GameContext {

    private final Player player; // Игрок, к которому применяются эффекты событий
    private int stepIndex; // Номер текущего шага
    private int lastEventType; // Тип события на предыдущем шаге (нужен для взвешенного выбора следующего)

    private boolean victory; // true, если сработало событие «Выход из леса».
    private boolean defeat; // true, если игрок проиграл

    // Значение lastEventType до первого события (ещё не было события).
    public static final int NO_EVENT = -1;

    /**
     * Создаёт контекст для заданного игрока.
     * Шаг = 0, последнее событие = NO_EVENT, победа и поражение = false.
     */
    public GameContext(Player player) {
        this.player = player;
        this.stepIndex = 0;
        this.lastEventType = NO_EVENT;
        this.victory = false;
        this.defeat = false;
    }

    public Player getPlayer() {
        return player;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    /** Увеличивает номер шага на 1 (вызывается контроллером перед обработкой события). */
    public void incrementStep() {
        this.stepIndex++;
    }

    public int getLastEventType() {
        return lastEventType;
    }

    /** Устанавливается ForestPathChain после обработки события */
    public void setLastEventType(int lastEventType) {
        this.lastEventType = lastEventType;
    }

    public boolean isVictory() {
        return victory;
    }

    /** Вызывается ExitHandler при выходе из леса. */
    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public boolean isDefeat() {
        return defeat;
    }

    /** Вызывается TrapHandler при здоровье ≤ 0. */
    public void setDefeat(boolean defeat) {
        this.defeat = defeat;
    }
}
