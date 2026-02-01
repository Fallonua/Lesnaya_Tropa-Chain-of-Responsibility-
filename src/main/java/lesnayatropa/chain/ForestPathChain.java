package lesnayatropa.chain;

import lesnayatropa.handler.Handler;
import lesnayatropa.handler.*;
import lesnayatropa.model.GameContext;

import java.util.Random;

/**
 * Cтроит цепочку из шести событий и определяет, какое «выпадает» на текущем шаге (resolveEventType).
 * Логика выбора зависит от номера шага, прошлого события и состояния игрока (здоровье, энергия).
 */
public class ForestPathChain {

    // Типы событий
    public static final int FOREST_SPIRIT = 1;
    public static final int GOBLIN = 2;
    public static final int MAGIC_SPRING = 3;
    public static final int TRAP = 4;
    public static final int REST = 5;
    public static final int EXIT = 6;

    // Минимальный номер шага, с которого возможно событие «Выход из леса»
    private static final int MIN_STEP_FOR_EXIT = 5;

    /** Голова цепочки обработчиков (первый обработчик). */
    private final Handler chain;
    private final Random random = new Random();

    public ForestPathChain() {
        chain = buildChain();
    }

    /**
     * Собирает цепочку: ForestSpirit → Goblin → MagicSpring → Trap → Rest → Exit (null).
     * Порядок определяет, кто первым получает запрос; каждый обработчик сравнивает request со своим типом.
     */
    private Handler buildChain() {
        return new ForestSpiritHandler(
                new GoblinHandler(
                        new MagicSpringHandler(
                                new TrapHandler(
                                        new RestHandler(
                                                new ExitHandler(null))))));
    }

    /**
     * Определяет тип события на текущем шаге по контексту.
     * Используется взвешенный случайный выбор: веса зависят от шага, прошлого события и здоровья/энергии.
     * Примеры: EXIT только при step >= 5; после гоблина выше шанс лесного духа; при низком здоровье чаще источник/привал.
     *
     * @param context контекст (шаг, lastEventType, здоровье и энергия игрока)
     * @return одна из констант FOREST_SPIRIT, GOBLIN, MAGIC_SPRING, TRAP, REST, EXIT
     */
    public int resolveEventType(GameContext context) {
        int step = context.getStepIndex();
        int last = context.getLastEventType();
        int health = context.getPlayer().getHealth();
        int energy = context.getPlayer().getEnergy();

        // Базовые веса для взвешенного выбора (чем больше вес — тем выше вероятность события)
        double wSpirit = 15;
        double wGoblin = 15;
        double wSpring = 15;
        double wTrap = 15;
        double wRest = 15;
        double wExit = 0;

        // Выход из леса возможен только с 5-го шага включительно
        if (step >= MIN_STEP_FOR_EXIT) {
            wExit = 10;
        }

        // В начале пути чаще ловушки, реже привал
        if (step < 3) {
            wTrap = 15;
            wRest = 7;
        }

        // После потери монет (гоблин) — повышаем шанс лесного духа
        if (last == GOBLIN) {
            wSpirit += 5;
        }
        if (last == TRAP) {
            wSpring += 5;
            wRest += 3;
        }
        if (last == REST) {
            wRest = Math.max(5, wRest - 10);
        }

        // Если здоровье/энергия уже на максимуме — почти не выдаём источник/привал
        if (health >= 10) {
            wSpring = 2;
        }
        if (energy >= 10) {
            wRest = 2;
        }
        // Если здоровье/энергия уже на минимуме — повышаем вероятность привала и источника
        if (health < 3) {
            wSpring += 10;
            wRest += 5;
        }
        if (energy < 3) {
            wRest += 10;
        }

        // Взвешенный случайный выбор: сумма весов = total, случайное число r в [0, total), выбираем интервал
        double total = wSpirit + wGoblin + wSpring + wTrap + wRest + wExit;
        double r = random.nextDouble() * total;

        r -= wSpirit;
        if (r < 0) return FOREST_SPIRIT;
        r -= wGoblin;
        if (r < 0) return GOBLIN;
        r -= wSpring;
        if (r < 0) return MAGIC_SPRING;
        r -= wTrap;
        if (r < 0) return TRAP;
        r -= wRest;
        if (r < 0) return REST;
        return EXIT;
    }

    /**
     * Выполняет один шаг игры: определяет тип события по контексту, передаёт его в цепочку обработчиков,
     * записывает выбранный тип в context.lastEventType для следующего шага.
     *
     * @param context контекст (игрок, шаг, последнее событие); после вызова в нём обновлены lastEventType, victory, defeat
     * @return true — продолжить игру; false — конец (победа или поражение)
     */
    public boolean process(GameContext context) {
        int eventType = resolveEventType(context);
        boolean continueGame = chain.process(eventType, context);
        context.setLastEventType(eventType);
        return continueGame;
    }
}
