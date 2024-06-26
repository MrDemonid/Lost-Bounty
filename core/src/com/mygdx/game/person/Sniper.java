package com.mygdx.game.person;

import com.mygdx.game.behavior.CoordXY;

/**
 * Класс Снайпер (лучник)
 */
public class Sniper extends ShooterBase {

    private static final int HEALTH = 450;
    private static final int POWER = 40;
    private static final int AGILITY = 30;
    private static final int DEFENCE = 5;
    private static final int DISTANCE = 16;
    private static final int ARROWS = 12;
    private static final int EFFECTIVE_DISTANCE = 6;


    /**
     * Создание экзеспляра Снайпера
     *
     * @param name Имя
     * @param pos  Положение в прогстранстве
     */
    public Sniper(String name, CoordXY pos)
    {
        super(name, 3, HEALTH, POWER, AGILITY, DEFENCE, DISTANCE, ARROWS, EFFECTIVE_DISTANCE, pos);
    }

    @Override
    public String toString()
    {
        return String.format("[Снайпер] (%s) %s { ❤️=%d, \uD83C\uDFF9=%d }", position.toString(), name, health, ammo);
    }

}

