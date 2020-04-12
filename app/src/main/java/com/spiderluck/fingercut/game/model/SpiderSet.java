package com.spiderluck.fingercut.game.model;

import android.graphics.RectF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

import com.spiderluck.fingercut.game.model.web.Particle;
import com.spiderluck.fingercut.game.model.web.Spring;
import com.spiderluck.fingercut.game.model.web.Web;
import com.spiderluck.fingercut.utils.Savable;
import com.spiderluck.fingercut.utils.Vector2;

public class SpiderSet implements Savable {

	public interface SpiderObserver {
		void onSpiderOut(Spider spider);
	}

	private SpiderObserver observer;

	private LinkedList<Spider> spiders;

	private enum Keys {
		Spiders,
	}

	public SpiderSet() {
		spiders = new LinkedList<>();
	}

	public SpiderSet(JSONObject json, Web web) throws JSONException {
		spiders = new LinkedList<>();
		JSONArray spidersData = json.getJSONArray(Keys.Spiders.toString());
		for (int i = 0; i < spidersData.length(); i++) {
			JSONObject spiderState = spidersData.getJSONObject(i);
			Spider spider = new Spider(web, spiderState);
			spiders.add(spider);
		}
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject state = new JSONObject();
		JSONArray spidersData = new JSONArray();

		for (Spider spider : spiders) {
			JSONObject spiderState = spider.toJSON();
			spidersData.put(spiderState);
		}

		state.put(Keys.Spiders.toString(), spidersData);
		return state;
	}

	public void setObserver(SpiderObserver observer) {
		this.observer = observer;
	}

	public void populate(int number, Web web) {
		spiders.clear();
		for (int i = 0; i < number; i++) {
			spiders.add(new Spider(web));
		}
	}

	public LinkedList<Spider> getSpiders() {
		return spiders;
	}

	public int getNumSpiders() {
		return spiders.size();
	}

	public void onParticlePulled(Particle pulled) {
		for (Spider spider : spiders) {
			spider.onParticlePulled(pulled);
		}
	}

	public void onSpringUnAvailable(Spring spring) {
		for (Spider spider : spiders) {
			spider.onSpringUnAvailable(spring);
		}
	}

	public void update(float dt, Vector2 gravity, RectF gameArea) {
		Iterator<Spider> it = spiders.iterator();
		while (it.hasNext()) {
			Spider spider = it.next();
			try {
				spider.update(dt, gravity, gameArea);
			} catch (Spider.OutException e) {
				observer.onSpiderOut(spider);
				it.remove();
			}
		}
	}

	public boolean areAnyInContactWith(Finger finger) {
		for (Spider spider : spiders) {
			if (finger.isInContactWith(spider)) {
				return true;
			}
		}

		return false;
	}
}
