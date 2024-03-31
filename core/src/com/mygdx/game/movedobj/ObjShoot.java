package com.mygdx.game.movedobj;

import com.mygdx.game.behavior.CoordXY;
import com.mygdx.game.config.ConfigGame;

public class ObjShoot extends ObjBase
{
    private static final int SIZE_STEP = 16;

    private float deltaX;       // вектор на цель по X
    private float deltaY;       // вектор на цель по Y

    protected int curX, curY;             // текущие

    protected int toX;
    protected int toY;

    protected CoordXY to;

    private int targetDamage;

    public ObjShoot(int fromX, int fromY, int toX, int toY, int targetDamage)
    {
        super(fromX, fromY, 15f);
        this.targetDamage = targetDamage;
        this.toX = toX;
        this.toY = toY;

        float w = ConfigGame.getMapWidth() * ConfigGame.getMapTileWidth();
        float h = ConfigGame.getMapHeight() * ConfigGame.getMapTileHeight();
        float maxLen = (float) Math.sqrt(w*w + h*h);
        deltaX = toX - fromX;
        deltaY = toY - fromY;
        float len = (float) Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        float time = (1.5f / maxLen) * len;
        setLiveTime(time);
    }

    public int getTargetDamage()
    {
        return this.targetDamage;
    }

    @Override
    public int getCurX() {
        return curX;
    }

    @Override
    public int getCurY() {
        return curY;
    }

    public int getTargetX()
    {
        return toX;
    }
    public int getTargetY()
    {
        return toY;
    }

    @Override
    public boolean update(float delta)
    {
        if (!super.update(delta))
            return false;

        curX = fromX + (int) ((deltaX / getLiveTime()) * getTime());
        curY = fromY + (int) ((deltaY / getLiveTime()) * getTime());
        return true;
    }

}
