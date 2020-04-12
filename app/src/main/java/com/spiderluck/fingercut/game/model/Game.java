package com.spiderluck.fingercut.game.model;



import android.graphics.RectF;

import org.json.JSONException;
import org.json.JSONObject;

import com.spiderluck.fingercut.game.model.meta.MetaData;
import com.spiderluck.fingercut.game.model.meta.MetaDataHelper;
import com.spiderluck.fingercut.game.model.web.Spring;
import com.spiderluck.fingercut.game.model.web.Web;
import com.spiderluck.fingercut.game.model.web.WebFactory;
import com.spiderluck.fingercut.game.model.web.WebType;
import com.spiderluck.fingercut.utils.Savable;
import com.spiderluck.fingercut.utils.Vector2;

public class Game implements Savable, Web.WebObserver, SpiderSet.SpiderObserver {

	private enum Keys {
		Web,
		Finger,
		SpiderManager,
		Meta
	}

	private Finger finger;

	private Web web;

	private MetaDataHelper metaDataHelper;

	private SpiderSet spiderSet;

	public Game() {
		finger = new Finger();
		metaDataHelper = new MetaDataHelper();
		spiderSet = new SpiderSet();
		spiderSet.setObserver(this);

		prepareLevel();
	}

	public Game(JSONObject json) throws JSONException {

		metaDataHelper = new MetaDataHelper(new MetaData(json.getJSONObject(Keys.Meta.toString())));
		web = WebFactory.getInstance().recreate(json.getJSONObject(Keys.Web.toString()));
		web.setObserver(this);
		finger = new Finger(json.getJSONObject(Keys.Finger.toString()));
		spiderSet = new SpiderSet(json.getJSONObject(Keys.SpiderManager.toString()), web);
		spiderSet.setObserver(this);
	}

	@Override
	public JSONObject toJSON() throws JSONException {

		JSONObject state = new JSONObject();

		state.put(Keys.Web.toString(), web.toJSON());
		state.put(Keys.Finger.toString(), finger.toJSON());
		state.put(Keys.SpiderManager.toString(), spiderSet.toJSON());
		state.put(Keys.Meta.toString(), metaDataHelper.getMetaData().toJSON());

		return state;
	}

	public boolean isFinished() {
		return metaDataHelper.getMetaData().isFinished();
	}

	public MetaDataHelper getMetaDataHelper() {
		return metaDataHelper;
	}

	public MetaData getMetaData() {
		return metaDataHelper.getMetaData();
	}

	public Web getWeb() {
		return web;
	}

	public void setWeb(Web web) {
		this.web = web;
	}

	public SpiderSet getSpiderSet() {
		return spiderSet;
	}

	public Finger getFinger() {
		return finger;
	}

	public void update(float dt, Vector2 gravity, RectF gameArea) {
		web.update(dt, gravity, gameArea);

		spiderSet.update(dt, gravity, gameArea);

		if (spiderSet.getNumSpiders() == 0) {
			metaDataHelper.levelUp();
		}

		finger.update(dt);

		//check game over conditions
		if (!isFinished() && !finger.isBitten()) {
			if (spiderSet.areAnyInContactWith(finger)) {
				finger.setBitten(true);
				metaDataHelper.die();

				if (isFinished()) {
					finger.cancelTracking();
				}
			}
		}
	}

	public void prepareLevel() {
		int level = metaDataHelper.getMetaData().getLevel();
		WebType webType = getWebType();

		web = WebFactory.getInstance().create(webType);
		web.setObserver(this);

		spiderSet.populate(level, web);
	}

	public WebType getWebType() {
		int level = metaDataHelper.getMetaData().getLevel();
		//levels start from 1, enums from 0
		return WebType.values()[(level - 1) % WebType.values().length];
	}

	@Override
	public void onSpringBroken(Spring spring) {
		if (!isFinished()) {
			finger.cancelTracking();
			metaDataHelper.addScore(1);
		}

		spiderSet.onSpringUnAvailable(spring);
	}

	@Override
	public void onSpringOut(Spring spring) {
		spiderSet.onSpringUnAvailable(spring);
	}

	@Override
	public void onSpiderOut(Spider spider) {
		if (!isFinished()) {
			metaDataHelper.addScore(10);
		}
	}
}
