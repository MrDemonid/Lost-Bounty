package com.mygdx.game.movedobj;

import com.mygdx.game.config.ConfigGame;

public class ObjExplode extends ObjBase
{

    public ObjExplode(int x, int y)
    {
        super(x, y, ConfigGame.getExplodeLiveTime());
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
