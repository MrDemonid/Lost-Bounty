package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screens.MainMenuScreen;

public class BountyGame extends Game {

	@Override
	public void create() {
		setScreen(new MainMenuScreen(this));
	}
}
