package com.spiderluck.fingercut.game.engine;



import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import org.json.JSONException;

import com.spiderluck.fingercut.game.gameplay.GamelyModeDisplay;
import com.spiderluck.fingercut.game.model.Game;
import com.spiderluck.fingercut.game.model.meta.MetaData;
import com.spiderluck.fingercut.game.model.meta.MetaDataHelper;
import com.spiderluck.fingercut.game.model.meta.MetaDataMsg;
import com.spiderluck.fingercut.game.model.web.WebType;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class GameEngine implements MetaDataHelper.MetaDataObserver {

	private Handler messageHandler;

	private Game game;

	private GamelyModeDisplay gamelyModeDisplay;

	private VectoringMyGraf gravity;

	public GameEngine(Context context, Handler messageHandler, Game game) {
		this.messageHandler = messageHandler;
		this.game = game;

		gamelyModeDisplay = new GamelyModeDisplay(context);

		game.getMetaDataHelper().setObserver(this);

		gravity = new VectoringMyGraf(0.0f, 1.0f);
	}

	public synchronized void setSurfaceSize(int width, int height) {
		gamelyModeDisplay.getCanvasHelper().setSize(width, height);

		WebType webType = game.getWebType();
		gamelyModeDisplay.setupBackground(webType);
	}

	public synchronized void frame(Canvas canvas, float dt) {
		game.update(dt, gravity, gamelyModeDisplay.getCanvasHelper().getArea());

		gamelyModeDisplay.draw(game, canvas, gamelyModeDisplay.getCanvasHelper().getScale());
	}

	public synchronized void onTouchEvent(MotionEvent motionEvent) {
		if (game.isFinished()) {
			return;
		}

		VectoringMyGraf touch;
		switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch = gamelyModeDisplay.getCanvasHelper().transform(motionEvent.getX(), motionEvent.getY());
				game.getFinger().startTracking(touch, game.getWeb(), game.getSpiderSet());
				break;
			case MotionEvent.ACTION_MOVE:
				touch = gamelyModeDisplay.getCanvasHelper().transform(motionEvent.getX(), motionEvent.getY());
				game.getFinger().continueTracking(touch);
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				game.getFinger().cancelTracking();
				break;
		}
	}

	public synchronized void setGravity(float x, float y, float z) {
		gravity.set(-x, y + Math.abs(z)); // y is backwards, so here is positive
		gravity.scale(0.2f);
	}

	public synchronized boolean isFinished() {
		return game.getMetaDataHelper().getMetaData().isFinished();
	}

	public synchronized Game getGame() {
		return game;
	}

	private void onLevelUp() {
		game.prepareLevel();

		gamelyModeDisplay.setupBackground(game.getWebType());
	}

	@Override
	public void onMetaDataChanged(MetaDataMsg.Reason reason, MetaData metaData) {

		switch (reason) {
			case LevelUp:
				onLevelUp();
				break;
			default:
				break;
		}

		//forward to activity
		try {
			Message msg = messageHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString(MetaDataMsg.Fields.Reason.toString(), reason.toString());
			b.putString(MetaDataMsg.Fields.Data.toString(), metaData.toJSON().toString());
			msg.setData(b);
			messageHandler.sendMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
