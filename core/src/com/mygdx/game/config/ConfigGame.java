package com.mygdx.game.config;

import com.badlogic.gdx.Gdx;

public class ConfigGame {

    private final static int FPS = 40;
    private final static float EXPLODE_LIVE_TIME = 48.0f / FPS;

    private static final int MAP_WIDTH = 10;
    private static final int MAP_HEIGHT = 10;

    private static int mapTileWidth;
    private static int mapTileHeight;

    public ConfigGame()
    {
        configMap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void configMap(int width, int height)
    {
        width = Math.min(width, Gdx.graphics.getWidth());
        height = Math.min(height, Gdx.graphics.getHeight());
        mapTileWidth = width / MAP_WIDTH;
        mapTileHeight = height / (MAP_HEIGHT+2);
    }

    public static int getFps()
    {
        return FPS;
    }
    public static float getExplodeLiveTime() { return EXPLODE_LIVE_TIME; }

    /*
        Данные по нашему "миру"
     */
    public static int getMapWidth() { return MAP_WIDTH; }
    public static int getMapHeight() { return MAP_HEIGHT; }
    public static int getMapTileWidth() { return mapTileWidth; }
    public static int getMapTileHeight() { return mapTileHeight; }

}
