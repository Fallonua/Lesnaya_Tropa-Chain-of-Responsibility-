package lesnayatropa.model;

/**
 * Модель игрока: монеты, здоровье, энергия.
 * Здоровье и энергия ограничены значением 10.
 * Используется в GameContext и в обработчиках цепочки для применения эффектов событий.
 */
public class Player {
    // Максимальное значение здоровья и энергии.
    private static final int MAX_STAT = 10;

    private int coins;
    private int health;
    private int energy;

    // Создаёт игрока с начальными значениями: 10 монет, 10 здоровья, 10 энергии
    public Player() {
        this.coins = 10;
        this.health = 10;
        this.energy = 10;
    }

    /**
     * Тратит энергию на шаг по тропинке.
     *
     * @param amount сколько энергии потратить (1)
     * @return true, если энергии хватило и списание выполнено; false, если энергии недостаточно
     */
    public boolean spendEnergy(int amount) {
        if (amount <= this.energy) {
            this.energy -= amount;
            return true;
        }
        return false;
    }

    // Увеличивает количество монет (без лимита).
    public void addCoins(int amount) {
        this.coins += amount;
    }

    // Увеличивает здоровье, но не более чем до MAX_STAT (10).
    public void addHealth(int amount) {
        this.health = Math.min(MAX_STAT, this.health + amount);
    }

    // Увеличивает энергию, но не более чем до MAX_STAT (10).
    public void addEnergy(int amount) {
        this.energy = Math.min(MAX_STAT, this.energy + amount);
    }

    // Уменьшает количество монет
    public void subtractCoins(int amount) {
        this.coins -= amount;
    }

    // Уменьшает здоровье
    public void subtractHealth(int amount) {
        this.health -= amount;
    }

    /**
     * Проверка, может ли игрок продолжать игру.
     *
     * @return true, если здоровье и энергия строго больше 0
     */
    public boolean isAlive() {
        return health > 0 && energy > 0;
    }

    public int getCoins() {
        return coins;
    }

    public int getHealth() {
        return health;
    }

    public int getEnergy() {
        return energy;
    }
}
