
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.behavior.BackGrounds;
import com.mygdx.game.behavior.TeamType;
import com.mygdx.game.config.ConfigGame;
import com.mygdx.game.movedobj.ObjBase;
import com.mygdx.game.movedobj.ObjText;
import com.mygdx.game.person.PersonBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Render {

    private final OrthographicCamera camera;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private float gameTime;

    private final Texture texturePersons;
    private final Texture textureExplode;
    private final Texture textureResurrect;
    private final Texture textureShoot;
    private final Texture backGround;

    private final HashMap<String, Animation<TextureRegion>> activePerson;
    private final HashMap<String, TextureRegion> txPersons;

    private final HashMap<String, Animation<TextureRegion>> actionAnim; // взрывы, выстрелы и тд.

    private Music music;


    public Render()
    {
        gameTime = 0.0f;
        batch = new SpriteBatch();
        font = new BitmapFont();
        // настраиваем камеру и размер карты
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        // запускаем музон
        doPlayMusic();

        // подгружаем текстуру фона
        backGround = new Texture("fons/" + BackGrounds.values()[new Random().nextInt(BackGrounds.values().length)] + ".png");

        // подгружаем текстуры
        texturePersons = new Texture(Gdx.files.internal("tiles.png"));
        TextureRegion[][] tiles = TextureRegion.split(texturePersons, 64, 64);
        // для персонажей
        txPersons = new HashMap<>();
        txPersons.put("Peasant",        new TextureRegion(tiles[1][0]));
        txPersons.put("Crossbowman",    new TextureRegion(tiles[1][1]));
        txPersons.put("Sniper",         new TextureRegion(tiles[1][2]));
        txPersons.put("Robber",         new TextureRegion(tiles[1][3]));
        txPersons.put("Spearman",       new TextureRegion(tiles[1][4]));
        txPersons.put("Monk",           new TextureRegion(tiles[1][5]));
        txPersons.put("Wizard",         new TextureRegion(tiles[1][6]));
        // для активного персонажа
        activePerson = new HashMap<>();
        activePerson.put("Peasant",     new Animation<>(0.2f, tiles[1][0], tiles[2][0]));
        activePerson.put("Crossbowman", new Animation<>(0.2f, tiles[1][1], tiles[2][1]));
        activePerson.put("Sniper",      new Animation<>(0.2f, tiles[1][2], tiles[2][2]));
        activePerson.put("Robber",      new Animation<>(0.2f, tiles[1][3], tiles[2][3]));
        activePerson.put("Spearman",    new Animation<>(0.2f, tiles[1][4], tiles[2][4]));
        activePerson.put("Monk",        new Animation<>(0.2f, tiles[1][5], tiles[2][5]));
        activePerson.put("Wizard",      new Animation<>(0.2f, tiles[1][6], tiles[2][6]));

         actionAnim = new HashMap<>();
        // создаём анимацию под взрыв
        textureExplode = new Texture(Gdx.files.internal("explode.png"));
        tiles = TextureRegion.split(textureExplode, 64, 64);
        TextureRegion[] expl = new TextureRegion[6*8];
        for (int y = 0, index = 0; y < 6; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                expl[index++] = tiles[y][x];
            }
        }
        actionAnim.put("ObjExplode", new Animation<>((float) 1/ ConfigGame.getFps(), expl));

        // создаём анимацию под лечение
        textureResurrect = new Texture(Gdx.files.internal("resurrect.png"));
        tiles = TextureRegion.split(textureResurrect, 64, 64);
        expl = new TextureRegion[6*8];
        for (int y = 0, index = 0; y < 6; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                expl[index++] = tiles[y][x];
            }
        }
        actionAnim.put("ObjResurrect", new Animation<>((float) 1/ ConfigGame.getFps(), expl));

        // под файер-болл
        textureShoot = new Texture(Gdx.files.internal("shoot.png"));
        tiles = TextureRegion.split(textureShoot, 32, 32);
        expl = new TextureRegion[9];
        System.arraycopy(tiles[0], 0, expl, 0, 9);
        actionAnim.put("ObjShoot", new Animation<>((float) 1/ ConfigGame.getFps(), expl));
    }

    public void render(float delta, ArrayList<TeamPerson> team, ArrayList<ObjBase> actionObjects)
    {
        gameTime += delta;
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        batch.begin();

        renderMap();
        renderTeams(gameTime, team);
        renderAction(actionObjects);
//        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 8, 20);
        batch.end();
        if (!music.isPlaying())
        {
            music.dispose();
            doPlayMusic();
        }
    }


    private void renderAction(ArrayList<ObjBase> actionObjects)
    {
        if (!actionObjects.isEmpty())
        {
            for (ObjBase p : actionObjects)
            {
                if (p instanceof ObjText)
                {
                    font.draw(batch, ((ObjText) p).getText(), p.getCurX(), p.getCurY());
                } else {
                    int x = p.getCurX();
                    int y = p.getCurY();
                    int w = ConfigGame.getMapTileWidth();
                    int h = ConfigGame.getMapTileHeight();
                    TextureRegion frame = actionAnim.get(p.getClass().getSimpleName()).getKeyFrame(p.getTime(), true);
                    if (frame.getRegionWidth() < w || frame.getRegionHeight() < h)
                    {
                        // текстура меньше тайла, центрируем её
                        x = x + (w - frame.getRegionWidth())/2;
                        y = y + (h - frame.getRegionHeight())/2;
                    }
                    batch.draw(frame, x, y);//, w, h);
                }
            }
        }
    }


    private void renderMap()
    {
        batch.draw(backGround,0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    /**
     * Отрисовка команд
     *
     * @param time Общее время игры
     */
    private void renderTeams(float time, ArrayList<TeamPerson> team)
    {
        for (TeamPerson p : team)
        {
            PersonBase person = p.person;
            if (person.getHealth() > 0)
            {
                String className = person.getClass().getSimpleName();
                TextureRegion frame;
                if (p.active)
                {
                    frame = activePerson.get(className).getKeyFrame(time, true);
                } else {
                    frame = txPersons.get(className);
                }
                if (frame != null)
                {
                    drawPerson(frame, person, p.team != TeamType.RED);
                } else {
                    System.out.println("renderTeams(): unknown class: " + p.getClass().getSimpleName());
                }
            }
        }
    }

    private void drawPerson(TextureRegion frame, PersonBase person, boolean flip)
    {
        int tileWidth = ConfigGame.getMapTileWidth();
        int tileHeight = ConfigGame.getMapTileHeight();
        int gx = person.getPosition().getX() * tileWidth;
        int gy = person.getPosition().getY() * tileHeight;
        gx += ((tileWidth - frame.getRegionWidth()) / 2);
        gy += ((tileHeight - frame.getRegionHeight()) / 2);
        if (flip)
        {
            frame.flip(true,false);
            batch.draw(frame,gx, gy);
            frame.flip(true,false);
        } else {
            batch.draw(frame,gx, gy);

        }
    }

    /**
     * Запуск на проигрывание рандомной музыки
     */
    private void doPlayMusic()
    {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/paul-romero-rob-king-combat-theme-0" + (new Random().nextInt(4)+1) + ".mp3"));
        music.setVolume(0.3f);
        music.play();
    }

    /**
     * Изменение размеров окна
     * @param width не существенно, мы ставим свою ширину
     * @param height не существенно, мы ставим свою ширину
     */
    public void resize(int width, int height)
    {
//        GameLogger.info(String.format("render.resize(%d, %d)", width, height));
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    /**
     * Освобождаем память
     */
    public void dispose()
    {
        font.dispose();
        batch.dispose();

        texturePersons.dispose();
        textureExplode.dispose();
        textureResurrect.dispose();
        textureShoot.dispose();
        backGround.dispose();

        music.stop();
        music.dispose();
    }

}
