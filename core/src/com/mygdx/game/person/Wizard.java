package com.mygdx.game.person;

import com.mygdx.game.behavior.CoordXY;

/**
 * Класс Колдун
 */
public class Wizard extends MagicianBase {

    private static final int HEALTH = 350;
    private static final int POWER = 40;
    private static final int AGILITY = 10;
    private static final int DEFENCE = 0;
    private static final int DISTANCE = 8;
    private static final int MANA = 100;

    /**
     * Создание экземпляра Колдуна
     *
     * @param name имя
     */
    public Wizard(String name, CoordXY pos)
    {
        super(name, 1, HEALTH, POWER, AGILITY, DEFENCE, DISTANCE, MANA, pos);
    }


    @Override
    public String toString()
    {
        return String.format("[Волшебник] (%s) %s { ❤️=%d, \uD83D\uDD25=%d }", position.toString(), name, health, mana);
    }

}
