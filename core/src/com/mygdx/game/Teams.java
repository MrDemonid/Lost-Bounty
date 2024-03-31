package com.mygdx.game;

import com.mygdx.game.behavior.*;
import com.mygdx.game.config.ConfigGame;
import com.mygdx.game.movedobj.*;
import com.mygdx.game.person.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Teams {
    private final ArrayList<PersonBase> red;
    private final ArrayList<PersonBase> blue;

    private final ArrayList<TeamPerson> allPersons;

    int curPerson;

    public final ArrayList<ObjBase> actionObjects;  // взрывы, "стрелы" и тд.

    public Teams()
    {
        this.red = new ArrayList<PersonBase>();
        this.blue = new ArrayList<PersonBase>();
        this.allPersons = new ArrayList<TeamPerson>();
        actionObjects = new ArrayList<ObjBase>();
        curPerson = -1;
    }

    /**
     * Возвращает список всех персонажей, отсортированный для правильной отрисовки
     * @return Отсортированный список персонажей
     */
    public ArrayList<TeamPerson> getAllPersons()
    {
        ArrayList<TeamPerson> team = new ArrayList<TeamPerson>(allPersons);
        Collections.sort(team, new TeamSort());
//        ArrayList<TeamPerson> team = (ArrayList<TeamPerson>) allPersons.stream()
//                .sorted((n1, n2) -> Integer.compare(n2.person.getPosition().getY(), n1.person.getPosition().getY()))
//                .collect(Collectors.toList());
        return team;
    }

    /**
     * Ход персонажей, или вывод анимированных объектов
     *
     * @param deltaTime Прошедшее с последнего вызова время
     */
    public void update(float deltaTime)
    {
        if (actionObjects.isEmpty())
        {
            updateTeams();
        } else {
            updateAction(deltaTime);
        }
    }


    private void updateTeams()
    {
        allPersons.get(curPerson).active = false;
        curPerson++;
        if (curPerson >= allPersons.size())
        {
            curPerson = 0;
        }
        TeamPerson p = allPersons.get(curPerson);
        p.active = true;
        if (p.team == TeamType.RED)
        {
            p.person.step(blue, red);
        } else {
            p.person.step(red, blue);
        }
        /*
            Проверяем последние действия активного персонажа
         */
        PersonBase person = allPersons.get(curPerson).person;
        ActionLog history = person.getHistory();
        switch (history.getActionType())
        {
            case SHOOT:
            case ATTACK:
                CoordXY from = person.getPosition();
                CoordXY to = history.getTarget().getPosition();
                ObjBase shoot = new ObjShoot(from.getX()* ConfigGame.getMapTileWidth(), from.getY()*ConfigGame.getMapTileHeight(),
                        to.getX()*ConfigGame.getMapTileWidth(), to.getY()*ConfigGame.getMapTileHeight(), history.getValue());
                actionObjects.add(shoot);
                break;
            case HEALED:
                int toX = history.getTarget().getPosition().getX();
                int toY = history.getTarget().getPosition().getY();

                ObjBase healed = new ObjResurrect(toX * ConfigGame.getMapTileWidth(), toY * ConfigGame.getMapTileHeight());
                ObjBase text = new ObjText(toX * ConfigGame.getMapTileWidth(), (toY+1) * ConfigGame.getMapTileHeight(), "" + history.getValue());
                actionObjects.add(healed);
                actionObjects.add(text);
                break;
        }
    }

    private void updateAction(float delta)
    {
        int index = 0;
        while (index < actionObjects.size())
        {
            ObjBase p = actionObjects.get(index);
            if (!p.update(delta))
            {
                if (p instanceof ObjShoot)
                {
                    ObjExplode exp = new ObjExplode(((ObjShoot) p).getTargetX(), ((ObjShoot) p).getTargetY());
                    ObjText txt = new ObjText(((ObjShoot) p).getTargetX(), ((ObjShoot) p).getTargetY()+ ConfigGame.getMapTileHeight(), "" + ((ObjShoot) p).getTargetDamage());
                    // добавляем в начало, чтобы сразу не обновлять
                    actionObjects.add(0, exp);
                    actionObjects.add(0, txt);
                    index += 2;
                }
                actionObjects.remove(index);
            } else {
                index++;
            }
        }
    }

    public boolean checkRedCommand()
    {
        for (PersonBase p : red)
            if (p.getHealth() > 0)
                return true;
        return false;
    }

    public boolean checkBlueCommand()
    {
        for (PersonBase p : blue)
            if (p.getHealth() > 0)
                return true;
        return  false;
    }

    public void createTeams(int numPersons)
    {
        createOneTeam(red, numPersons, 0);
        createOneTeam(blue, numPersons, 3);
        // создаём список всех персонажей, отсортированных по приоритету хода
        ArrayList<PersonBase> all = new ArrayList<>();
        all.addAll(red);
        all.addAll(blue);
        Collections.sort(all, new PrioritySort());  //        all.sort((o1, o2) -> Integer.compare(o2.priority, o1.priority));
        // переносим их в команды
        for (PersonBase p : all)
        {
            if (red.contains(p))
            {
                allPersons.add(new TeamPerson(TeamType.RED, p));
            } else {
                allPersons.add(new TeamPerson(TeamType.BLUE, p));
            }
        }
        curPerson = allPersons.size()-1;
    }

    private void createOneTeam(ArrayList<PersonBase> team, int num, int start)
    {
        Random rnd = new Random();
        while (--num >= 0)
        {
            int n = start + rnd.nextInt(4);
            switch (n)
            {
                case 0:
                    team.add(new Crossbowman(HeroesNames.getRandomName(), new CoordXY(0, num)));
                    break;
                case 1:
                    team.add(new Spearman(HeroesNames.getRandomName(), new CoordXY(0, num)));
                    break;
                case 2:
                    team.add(new Wizard(HeroesNames.getRandomName(), new CoordXY(0, num)));
                    break;
                case 3:
                    team.add(new Peasant(HeroesNames.getRandomName(), new CoordXY(start > 0 ? 9 : 0, num)));
                    break;
                case 4:
                    team.add(new Sniper(HeroesNames.getRandomName(), new CoordXY(9, num)));
                    break;
                case 5:
                    team.add(new Monk(HeroesNames.getRandomName(), new CoordXY(9, num)));
                    break;
                case 6:
                    team.add(new Robber(HeroesNames.getRandomName(), new CoordXY(9, num)));
                    break;
                default:
                    System.out.println("ERROR! Пересмотри алгоритм, ламер!");
            }

        }
    }

}

