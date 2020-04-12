package com.spiderluck.fingercut.game.model.web;



import org.json.JSONException;
import org.json.JSONObject;

import com.spiderluck.fingercut.game.model.web.graph.Node;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class Particle extends Node {
	private VectoringMyGraf pos, prevPos;
	private boolean pinned;

	private static final float dampingFactor = 0.99f;

	private enum Keys {
		Pos,
		PrevPos,
		Pinned
	}

	public Particle(float x, float y, boolean pinned) {
		super();
		pos = new VectoringMyGraf(x, y);
		prevPos = new VectoringMyGraf(x, y);
		this.pinned = pinned;
	}

	public Particle(JSONObject json) throws JSONException {
		super(json);
		pos = new VectoringMyGraf(json.getJSONObject(Keys.Pos.toString()));
		prevPos = new VectoringMyGraf(json.getJSONObject(Keys.PrevPos.toString()));
		pinned = json.getBoolean(Keys.Pinned.toString());
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject state = super.toJSON();

		state.put(Keys.Pos.toString(), pos.toJSON());
		state.put(Keys.PrevPos.toString(), prevPos.toJSON());
		state.put(Keys.Pinned.toString(), pinned);

		return state;
	}

	public void update(float dt, VectoringMyGraf gravity) {
		if (!pinned) {

			// Position Verlet integration method
			float nx, ny;
			nx = pos.X + (pos.X - prevPos.X) * dampingFactor + gravity.X * dt * dt;
			ny = pos.Y + (pos.Y - prevPos.Y) * dampingFactor + gravity.Y * dt * dt;

			prevPos.set(pos);

			pos.set(nx, ny);

			// Simple Euler integration method
			//velocity.add(Vector2.scale(gravity, dt));
			//position.add(Vector2.scale(velocity, dt));
		} else {
			pos.set(prevPos);
		}
	}

	public VectoringMyGraf getPos() {
		return pos;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public void setPinnedPos(VectoringMyGraf pos) {
		this.pos.set(pos);
		prevPos.set(pos);
	}
}
