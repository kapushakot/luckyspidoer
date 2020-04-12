package com.spiderluck.fingercut.game.display;



import android.graphics.Canvas;

import com.spiderluck.fingercut.game.model.web.Spring;
import com.spiderluck.fingercut.game.model.web.Web;
import com.spiderluck.fingercut.game.model.web.graph.Edge;

public class SpiderWebDisplay {
	private SpringDisplay springDisplay;

	public SpiderWebDisplay() {
		springDisplay = new SpringDisplay();
	}

	public void draw(Web web, Canvas canvas, float scale) {
		for (Edge edge : web.getEdges()) {
			springDisplay.draw((Spring) edge, canvas, scale);
		}
	}
}
