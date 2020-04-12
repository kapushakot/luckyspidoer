package com.spiderluck.fingercut.game.model.web.graph;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import com.spiderluck.fingercut.utils.Savable;

public class Node implements Savable {
	private static int nextId;

	private int id;

	protected LinkedList<Edge> edges;

	private enum Keys {
		Id
	}

	public Node() {
		id = nextId;
		nextId++;
		edges = new LinkedList<>();
	}

	public Node(JSONObject json) throws JSONException {
		edges = new LinkedList<>();
		id = json.getInt(Keys.Id.toString());
	}


	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(Keys.Id.toString(), id);
		return json;
	}

	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	public void removeEdge(Edge edge) {
		edges.remove(edge);
	}

	public LinkedList<Edge> getEdges() {
		return edges;
	}

	public Edge getEdgeTo(Node v2) {
		for (Edge e : edges) {
			if (e.next(this) == v2) {
				return e;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}
}
