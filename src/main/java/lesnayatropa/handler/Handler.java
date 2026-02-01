package lesnayatropa.handler;

import lesnayatropa.model.GameContext;

/**
 * Абстрактный базовый класс цепочки обязанностей (Chain of Responsibility).
 * Каждый конкретный обработчик проверяет, совпадает ли тип запроса с его типом события;
 * если нет — передаёт запрос следующему обработчику в цепочке (processor).
 * Таким образом, отправитель (контроллер) не знает, какой обработчик сработает — это решается во время выполнения.
 */
public abstract class Handler {
    // Следующий обработчик в цепочке; null у последнего (ExitHandler).
    private final Handler processor;

    /** @param processor следующий обработчик; для конца цепочки передаётся null */
    public Handler(Handler processor) {
        this.processor = processor;
    }

    /**
     * Обрабатывает запрос по типу события.
     * В подклассах: если request совпадает с типом этого обработчика — выполняем действие (эффект на игрока, Alert),
     * возвращаем true (продолжить) или false (конец игры). Иначе вызываем super.process(request, context),
     * то есть передаём запрос дальше по цепочке.
     *
     * @param request тип события (константа из ForestPathChain: FOREST_SPIRIT, GOBLIN, MAGIC_SPRING, TRAP, REST, EXIT)
     * @param context контекст игры (игрок, шаг, последнее событие); здесь же устанавливаются victory/defeat
     * @return true — игра продолжается; false — игра завершена (победа или поражение; уточняется по context.isVictory() / isDefeat())
     */
    public boolean process(Integer request, GameContext context) {
        // Дефолтное поведение: передать запрос следующему; если следующего нет — вернуть true (безопасный вариант)
        if (processor != null) {
            return processor.process(request, context);
        }
        return true;
    }
}
