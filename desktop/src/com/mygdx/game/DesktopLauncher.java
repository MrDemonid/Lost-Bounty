package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.BountyGame;
import com.mygdx.game.config.ConfigGame;

import java.io.UnsupportedEncodingException;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) throws UnsupportedEncodingException {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(640, 640+64);
		config.useVsync(true);
		config.setForegroundFPS(ConfigGame.getFps());
		config.setTitle("The Lost Bounty");
		System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
		new Lwjgl3Application(new BountyGame(), config);
	}
}
