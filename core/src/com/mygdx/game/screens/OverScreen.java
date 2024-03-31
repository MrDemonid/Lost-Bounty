package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OverScreen extends BaseScreen {

    TextureRegion title;
    SpriteBatch batch;
    private BitmapFont font;
    String text;


    public OverScreen(Game game, String text)
    {
        super(game);
        this.text = text;
    }

    @Override
    public void show()
    {
        title = new TextureRegion(new Texture(Gdx.files.internal("over.jpg")));
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(Color.CYAN);
        batch.getProjectionMatrix().setToOrtho2D(0, 0, title.getTexture().getWidth(), title.getTexture().getHeight());
    }

    @Override
    public void render (float delta) {
        batch.begin();
        batch.draw(title, 0, 0);

        GlyphLayout layout = new GlyphLayout(); //dont do this every frame! Store it as member
        layout.setText(font, text);
        float width = layout.width;// contains the width of the current set text
        float height = layout.height; // contains the height of the current set text
        float x = (title.getTexture().getWidth() - width) / 2;
        float y = (float) (title.getTexture().getHeight()) / 2;
        font.draw(batch,layout,x, y);

        batch.end();

//        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.justTouched())
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.justTouched())
        {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void hide () {
        batch.dispose();
        title.getTexture().dispose();
        font.dispose();
    }

}
