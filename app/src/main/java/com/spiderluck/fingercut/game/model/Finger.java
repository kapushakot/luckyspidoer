package com.spiderluck.fingercut.game.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.spiderluck.fingercut.game.model.web.Particle;
import com.spiderluck.fingercut.game.model.web.Web;
import com.spiderluck.fingercut.utils.Savable;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class Finger implements Savable {
	private boolean bitten;
	private float timeToHeal;

	private VectoringMyGraf position;
	private float radius;
	private Particle selectedParticle;

	private static final float healTime = 1.0f;

	private enum Keys {
		Bitten,
		TimeToHeal
	}

	public Finger() {
		bitten = false;
		timeToHeal = 0.0f;

		position = null;
		radius = 0.1f;
		selectedParticle = null;

		setBitten(false);
	}

	public Finger(JSONObject json) throws JSONException {
		bitten = json.getBoolean(Keys.Bitten.toString());
		timeToHeal = (float) json.getDouble(Keys.TimeToHeal.toString());

		position = null;
		radius = 0.1f;
		selectedParticle = null;

		setBitten(false);
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject state = new JSONObject();

		state.put(Keys.Bitten.toString(), bitten);
		state.put(Keys.TimeToHeal.toString(), timeToHeal);

		return state;
	}

	public void update(float dt) {
		if (bitten) {
			timeToHeal -= dt;

			if (timeToHeal <= 0) {
				setBitten(false);
			}
		}
	}

	public void setBitten(boolean bitten) {
		this.bitten = bitten;
		if (this.bitten) {
			timeToHeal = healTime;
			cancelDragging();
		}
	}

	public boolean isInContactWith(Spider spider) {
		return position != null && VectoringMyGraf.length(VectoringMyGraf.sub(position, spider.getPosition())) < radius;
	}

	public boolean isBitten() {
		return bitten;
	}

	public VectoringMyGraf getPosition() {
		return position;
	}

	public float getRadius() {
		return radius;
	}

	public void startTracking(VectoringMyGraf touch, Web web, SpiderSet spiderSet) {
		position = touch;
		if (!bitten) {
			Particle particle = web.selectParticleInRange(touch, radius);
			if (particle != null) {
				spiderSet.onParticlePulled(particle);
				selectedParticle = particle;
				selectedParticle.setPinned(true);
			}
		}
	}

	public void continueTracking(VectoringMyGraf touch) {
		if (position != null) {
			if (selectedParticle != null) {
				VectoringMyGraf move = VectoringMyGraf.sub(touch, position);
				VectoringMyGraf particlePos = selectedParticle.getPos();
				particlePos.add(move);
				selectedParticle.setPinnedPos(particlePos);
			}
			position.set(touch);
		}
	}

	public void cancelTracking() {
		cancelDragging();
		position = null;
	}

	public void cancelDragging() {
		if (selectedParticle != null) {
			selectedParticle.setPinned(false);
			selectedParticle = null;
		}
	}
}