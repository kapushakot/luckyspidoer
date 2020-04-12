package com.spiderluck.fingercut.game.model;


import android.graphics.RectF;
import android.util.Log;

import androidx.core.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Random;

import com.spiderluck.fingercut.game.model.web.Particle;
import com.spiderluck.fingercut.game.model.web.Spring;
import com.spiderluck.fingercut.game.model.web.graph.Edge;
import com.spiderluck.fingercut.game.model.web.graph.Graph;
import com.spiderluck.fingercut.game.model.web.graph.Node;
import com.spiderluck.fingercut.utils.Modulo;
import com.spiderluck.fingercut.utils.Savable;
import com.spiderluck.fingercut.utils.VectoringMyGraf;

public class Spider implements Savable {
	private int mode;
	private Graph graph;

	private Spring spring;

	private float springPercent;

	private Particle target;

	private static final float SPEED_NORMAL = 0.25f;
	private static final float SPEED_FURIOUS = 0.5f;

	private Random generator;
	private static final int MODE_RANDOM = 0;

	private static final int MODE_ATTACK = 1;
	private static final int MODE_FALLING = 2;

	private LinkedList<Node> path;

	private static final int fingerDetectDistance = 15;

	private VectoringMyGraf position;
	private VectoringMyGraf velocity;
	private float rotation;

	private static VectoringMyGraf upVector = new VectoringMyGraf(0.0f, 1.0f);

	private enum Keys {
		Mode,
		Target,
		PrevTarget,
		SpringPercent,
		Position,
		Velocity,
		Rotation
	}

	public class OutException extends Exception {
	}

	public Spider(Graph graph) {
		this.graph = graph;

		spring = (Spring) graph.getRandomEdge();
		target = spring.getParticle2();

		mode = MODE_RANDOM;

		generator = new Random();

		path = null;

		position = new VectoringMyGraf();
		velocity = new VectoringMyGraf();
		rotation = 0;
	}

	public Spider(Graph graph, JSONObject json) throws JSONException {
		this.graph = graph;

		mode = json.getInt(Keys.Mode.toString());
		if (mode != MODE_FALLING) {
			target = (Particle) graph.getNodeWithId(json.getInt(Keys.Target.toString()));
			Node prevTarget = graph.getNodeWithId(json.getInt(Keys.PrevTarget.toString()));
			spring = (Spring) prevTarget.getEdgeTo(target);
			springPercent = (float) json.getDouble(Keys.SpringPercent.toString());
			position = new VectoringMyGraf();
			velocity = new VectoringMyGraf();
		} else {
			position = new VectoringMyGraf(json.getJSONObject(Keys.Position.toString()));
			velocity = new VectoringMyGraf(json.getJSONObject(Keys.Velocity.toString()));
		}

		rotation = (float) json.getDouble(Keys.Rotation.toString());

		generator = new Random();

		// TODO
		path = null;
		if (mode == MODE_ATTACK) {
			mode = MODE_RANDOM;
		}
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject state = new JSONObject();

		state.put(Keys.Mode.toString(), mode);
		if (mode != MODE_FALLING) {
			state.put(Keys.Target.toString(), target.getId());
			state.put(Keys.PrevTarget.toString(), spring.next(target).getId());
			state.put(Keys.SpringPercent.toString(), springPercent);
		} else {
			state.put(Keys.Position.toString(), position.toJSON());
			state.put(Keys.Velocity.toString(), velocity.toJSON());
		}
		state.put(Keys.Rotation.toString(), rotation);

		return state;
	}

	private Pair<Particle, Spring> nextTargetAtRandom() {

		LinkedList<Edge> list = new LinkedList<>(target.getEdges());
		if (list.size() > 1) {
			list.remove(spring);
		}
		int i = generator.nextInt(list.size());
		Spring nextSpring = (Spring) list.get(i);

		if (nextSpring.getParticle1() == target) {
			return new Pair<>(nextSpring.getParticle2(), nextSpring);
		} else {
			return new Pair<>(nextSpring.getParticle1(), nextSpring);
		}
	}

	private Pair<Particle, Spring> nextTargetOnPath() {
		Node nextTarget = path.removeFirst();

		Spring nextSpring = (Spring) target.getEdgeTo(nextTarget);
		if (nextSpring == null) {
			switchToRandom();
			return nextTargetAtRandom();
		}
		return new Pair<>((Particle) nextTarget, nextSpring);
	}

	private void findNextTarget() {
		Pair<Particle, Spring> pair = nextTargetAtRandom();

		if (mode == MODE_ATTACK) {
			if (path.isEmpty()) {
				switchToRandom();
			} else {
				pair = nextTargetOnPath();
			}
		}

		target = pair.first;
		spring = pair.second;
		springPercent = 0.0f;
	}

	public void update(float dt, VectoringMyGraf gravity, RectF gameArea) throws OutException {
		if (mode != MODE_FALLING) {
			float len = spring.length();

			float speed = SPEED_NORMAL;
			if (mode == MODE_ATTACK) {
				speed = SPEED_FURIOUS;
			}

			// Avoid committing suicide
			if (!target.getPos().isInBounds(gameArea)) {
				//reverse
				target = (Particle) spring.next(target);
				springPercent = 1.0f - springPercent;
				switchToRandom();
			}

			springPercent += (speed * dt) / len;

			if (springPercent >= 1.0f) {
				findNextTarget();
				springPercent = 0.0f;
			}

			VectoringMyGraf newPosition = spring.getInterpolatedPosition(springPercent, target);
			velocity = VectoringMyGraf.scale(VectoringMyGraf.sub(newPosition, position), 1 / dt);
			position = newPosition;

			float desiredRot = spring.getAngle(upVector, target);

			float diffRot = Modulo.angleDifference(rotation, desiredRot);

			rotation += diffRot / 10.0f;
		} else {
			velocity.add(VectoringMyGraf.scale(gravity, dt));
			position.add(VectoringMyGraf.scale(velocity, dt));
		}

		if (!position.isInBounds(gameArea)) {
			throw new OutException();
		}
	}

	public VectoringMyGraf getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	private void switchToRandom() {
		mode = MODE_RANDOM;
		path = null;
	}

	private void switchToFalling() {
		mode = MODE_FALLING;
		spring = null;
		target = null;
		path = null;
	}

	private void switchToAttack(Node fingerNode) {
		LinkedList<Node> newPath = graph.findPathToNode(target, fingerNode, fingerDetectDistance);
		if (newPath != null) {
			Node node = newPath.removeFirst();
			if (node != target) {
				Log.e(getClass().getName(), "Bad path");
			}
			path = newPath;
			mode = MODE_ATTACK;
		} else {
			switchToRandom();
		}
	}

	public void onSpringUnAvailable(Spring unAvailableSpring) {
		switch (mode) {
			case MODE_RANDOM:
				if (unAvailableSpring == spring) {
					switchToFalling();
				}
				break;
			case MODE_ATTACK:
				if (unAvailableSpring == spring) {
					switchToFalling();
				} else {
					//We need to change path, as old may be invalid
					if (path.isEmpty()) {
						switchToRandom();
					} else {
						switchToAttack(path.getLast());
					}
				}
				break;
			case MODE_FALLING:
				break;
		}
	}

	public void onParticlePulled(Particle particle) {
		switch (mode) {
			case MODE_RANDOM:
				switchToAttack(particle);
				break;
			case MODE_ATTACK:
				if (particle != target) {
					switchToAttack(particle);
				}
				break;
			case MODE_FALLING:
				break;
		}
	}
}
