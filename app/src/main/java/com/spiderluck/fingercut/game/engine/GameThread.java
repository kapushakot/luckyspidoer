package com.spiderluck.fingercut.game.engine;



import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;


public class GameThread implements Runnable {

	private static final int maxFPS = 30;
	private static final int minSleep = 5;

	private final SurfaceHolder surfaceHolder;

	private Thread thread;

	private GameEngine gameEngine;

	private boolean running;

	public GameThread(SurfaceHolder surfaceHolder, GameEngine gameEngine) {
		this.surfaceHolder = surfaceHolder;
		this.gameEngine = gameEngine;

		thread = new Thread(this);
		running = true;
		thread.start();
	}

	public void terminate() {
		if (thread != null) {
			running = false;

			boolean retry = true;
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
			thread = null;
		}
	}

	@Override
	public void run() {
		while (running) {
			long timeStart = System.currentTimeMillis();

			Canvas canvas = surfaceHolder.lockCanvas(null);
			if (canvas != null) {
				try {
					gameEngine.frame(canvas, 1.0f / maxFPS);
				} catch (Exception e) {
					Log.e(getClass().getName(), Log.getStackTraceString(e));
				} finally {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}

			long timeEnd = System.currentTimeMillis();
			long elapsed = timeEnd - timeStart;
			long limit = 1000 / maxFPS - minSleep;
			try {
				if (elapsed < limit) {
					Thread.sleep(limit - elapsed);
				} else {
					Thread.sleep(minSleep);
				}
			} catch (InterruptedException e) {
			}
		}
	}
}
