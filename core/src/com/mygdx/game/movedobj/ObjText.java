package com.mygdx.game.movedobj;

import com.mygdx.game.config.ConfigGame;

public class ObjText extends ObjBase {

    private String text;

    public ObjText(int x, int y, String text)
    {
        super(x, y, ConfigGame.getExplodeLiveTime());
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public int getCurX() {
        return (int) fromX;
    }

    @Override
    public int getCurY() {
        return (int) fromY;
    }
}
