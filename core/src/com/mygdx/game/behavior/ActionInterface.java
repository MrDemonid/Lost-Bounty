package com.mygdx.game.behavior;

import com.mygdx.game.person.PersonBase;

import java.util.ArrayList;

/**
 * Интерфейс взаимодействия персонажей
 */
public interface ActionInterface {

    /**
     * Один шаг действия персонажа
     * @param enemies Список его врагов
     */
    void step(ArrayList<PersonBase> enemies, ArrayList<PersonBase> friends);

    String getInfo();
}
