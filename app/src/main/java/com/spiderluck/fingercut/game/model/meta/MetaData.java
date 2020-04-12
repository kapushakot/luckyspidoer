package com.spiderluck.fingercut.game.model.meta;



import org.json.JSONException;
import org.json.JSONObject;

import com.spiderluck.fingercut.utils.Savable;

public class MetaData implements Savable {

	private long date;
	private int level;
	private int lives;
	private int score;

	public enum Keys {
		Date,
		Level,
		Lives,
		Score,
	}

	public MetaData() {
		date = System.currentTimeMillis();
		level = 1;
		lives = 1;
		score = 0;
	}

	public MetaData(int level, int lives, int score) {
		this.date = System.currentTimeMillis();
		this.level = level;
		this.lives = lives;
		this.score = score;
	}

	public MetaData(JSONObject json) throws JSONException {
		date = json.getLong(Keys.Date.toString());
		level = json.getInt(Keys.Level.toString());
		lives = json.getInt(Keys.Lives.toString());
		score = json.getInt(Keys.Score.toString());
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();

		json.put(Keys.Date.toString(), date);
		json.put(Keys.Level.toString(), level);
		json.put(Keys.Lives.toString(), lives);
		json.put(Keys.Score.toString(), score);

		return json;
	}

	public Long getDate() {
		return date;
	}

	public int getLevel() {
		return level;
	}

	public int getLives() {
		return lives;
	}

	public int getScore() {
		return score;
	}

	public MetaData levelUp() {
		int newLevel = level + 1;
		int newLives = lives + 1;
		return new MetaData(newLevel, newLives, score);
	}

	public MetaData addScore(int add) {
		return new MetaData(level, lives, score + add * level);
	}

	public MetaData die() {
		return new MetaData(level, lives - 1, score);
	}

	public boolean isFinished() {
		return lives < 0;
	}
}

