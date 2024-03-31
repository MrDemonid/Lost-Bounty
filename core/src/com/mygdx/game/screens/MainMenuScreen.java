package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Input.Keys;
import com.mygdx.game.GameLogger;


public class MainMenuScreen extends BaseScreen {

    TextureRegion title;
    SpriteBatch batch;

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show()
    {
        title = new TextureRegion(new Texture(Gdx.files.internal("title.png")));
        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, title.getTexture().getWidth(), title.getTexture().getHeight());
    }

    @Override
    public void render (float delta) {
        batch.begin();
        batch.draw(title, 0, 0);
        batch.end();

        if (Gdx.input.isKeyPressed(Keys.ANY_KEY) || Gdx.input.justTouched())
        {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void hide () {
        batch.dispose();
        title.getTexture().dispose();
    }

}
