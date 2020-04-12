package com.spiderluck.fingercut.game.model.web;



import android.graphics.RectF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import com.spiderluck.fingercut.game.model.web.graph.Edge;
import com.spiderluck.fingercut.game.model.web.graph.Node;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class Spring extends Edge {
	private float defaultLength;
	private Particle particle1, particle2;

	private static final float tearFactor = 3.5f;
	private static final float tensionFactor = 0.9f;

	public enum Keys {
		DefaultLength
	}

	public class BrokenException extends Exception {
	}

	public Spring(Particle particle1, Particle particle2) {
		super(particle1, particle2);
		this.particle1 = (Particle) node1;
		this.particle2 = (Particle) node2;

		defaultLength = length() * tensionFactor;
	}

	public Spring(Map<Integer, Node> nodeMap, JSONObject json) throws JSONException {
		super(nodeMap, json);
		this.particle1 = (Particle) node1;
		this.particle2 = (Particle) node2;

		defaultLength = (float) json.getDouble(Keys.DefaultLength.toString());
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject state = super.toJSON();

		state.put(Keys.DefaultLength.toString(), defaultLength);

		return state;
	}

	public float length() {
		VectoringMyGraf d = VectoringMyGraf.sub(particle1.getPos(), particle2.getPos());
		return d.length();
	}

	public void resolveVerlet() throws BrokenException {
		VectoringMyGraf p1 = particle1.getPos();
		VectoringMyGraf p2 = particle2.getPos();

		float lx = p1.X - p2.X;
		float ly = p1.Y - p2.Y;

		float currentLength = VectoringMyGraf.length(lx, ly);

		if (currentLength < defaultLength || currentLength == 0.0f) {
			return;
		}

		if (currentLength > defaultLength * tearFactor) {
			throw new BrokenException();
		}

		float diff = defaultLength - currentLength;

		lx /= currentLength;
		ly /= currentLength;

		float diffX = lx * diff * 0.5f;
		float diffY = ly * diff * 0.5f;

		//try to restore original length
		p1.add(diffX, diffY);
		p2.add(-diffX, -diffY);
	}

	public VectoringMyGraf getInterpolatedPosition(float a, Node end) {
		VectoringMyGraf p1 = particle1.getPos();
		VectoringMyGraf p2 = particle2.getPos();
		if (end == node2) {
			return VectoringMyGraf.lerp(p1, p2, a);
		} else {
			return VectoringMyGraf.lerp(p2, p1, a);
		}
	}

	public float getAngle(VectoringMyGraf base, Node end) {
		VectoringMyGraf p1 = particle1.getPos();
		VectoringMyGraf p2 = particle2.getPos();
		if (end == node2) {
			VectoringMyGraf v2 = VectoringMyGraf.sub(p1, p2);
			return VectoringMyGraf.angle(v2, base);
		} else {
			VectoringMyGraf v2 = VectoringMyGraf.sub(p2, p1);
			return VectoringMyGraf.angle(v2, base);
		}
	}

	public boolean isOut(RectF area) {
		return !(particle1.getPos().isInBounds(area) || particle2.getPos().isInBounds(area));
	}

	public Particle getParticle1() {
		return particle1;
	}

	public Particle getParticle2() {
		return particle2;
	}
}