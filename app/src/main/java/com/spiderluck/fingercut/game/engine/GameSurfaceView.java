package com.spiderluck.fingercut.game.engine;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	private GameEngine gameEngine;

	private GameThread gameThread;

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (!isInEditMode()) {

			setFocusable(true);

			SurfaceHolder holder = getHolder();
			holder.addCallback(this);
		}
	}

	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
		gameEngine.onTouchEvent(motionEvent);
		//return super.onTouchEvent(motionEvent);
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//Log.i(getClass().getName(), "surfaceCreated");
		setWillNotDraw(false);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		//Log.i(getClass().getName(), "surfaceChanged");
		gameEngine.setSurfaceSize(width, height);
		if (gameThread != null) {
			gameThread.terminate();
		}
		gameThread = new GameThread(getHolder(), gameEngine);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//Log.i(getClass().getName(), "surfaceDestroyed");
		gameThread.terminate();
		gameThread = null;
	}
}
