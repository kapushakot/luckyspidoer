package com.spiderluck.fingercut.game.display;



import android.graphics.Canvas;

import com.spiderluck.fingercut.game.model.Spider;
import com.spiderluck.fingercut.game.model.SpiderSet;

public class SpiderSetDisplay {
	private GameSpiderScreen gameSpiderScreen;

	public SpiderSetDisplay() {
		gameSpiderScreen = new GameSpiderScreen();
	}

	public void draw(SpiderSet spiderSet, Canvas canvas, float scale) {
		for (Spider spider : spiderSet.getSpiders()) {
			gameSpiderScreen.draw(spider, canvas, scale);
		}
	}
}
