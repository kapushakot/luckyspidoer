package com.spiderluck.fingercut.game.model.web;



import android.graphics.RectF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import com.spiderluck.fingercut.game.model.web.graph.Edge;
import com.spiderluck.fingercut.game.model.web.graph.Graph;
import com.spiderluck.fingercut.game.model.web.graph.Node;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class Web extends Graph {

	public interface WebObserver {
		void onSpringBroken(Spring spring);

		void onSpringOut(Spring spring);
	}

	private WebObserver observer;

	public Web(ArrayList<Node> particles, ArrayList<Edge> springs) {
		super(particles, springs);
	}

	public JSONObject toJSON() throws JSONException {
		return super.toJSON();
	}

	public void update(float dt, VectoringMyGraf gravity, RectF gameArea) {
		// Resolve springs and remove broken
		Iterator<Edge> it = edges.iterator();
		while (it.hasNext()) {
			Spring spring = (Spring) it.next();
			try {
				spring.resolveVerlet();
			} catch (Spring.BrokenException e) {
				observer.onSpringBroken(spring);
				onRemoveEdge(spring);
				it.remove();
			}
		}

		// Update nodes (and springs) positions
		for (Node node : nodes) {
			((Particle) node).update(dt, gravity);
		}

		// Remove springs that fallen out of game area
		it = edges.iterator();
		while (it.hasNext()) {
			Spring spring = (Spring) it.next();
			if (spring.isOut(gameArea)) {
				observer.onSpringOut(spring);
				onRemoveEdge(spring);
				it.remove();
			}
		}
	}

	public Particle selectParticleInRange(VectoringMyGraf clickPos, float r) {
		float minDistance = r;
		Particle chosen = null;
		for (Node node : nodes) {
			Particle p = (Particle) node;
			float d = VectoringMyGraf.length(VectoringMyGraf.sub(p.getPos(), clickPos));
			if (!p.isPinned() && d <= minDistance) {
				minDistance = d;
				chosen = p;
			}
		}

		return chosen;
	}

	public void setObserver(WebObserver wo) {
		observer = wo;
	}
}