package com.spiderluck.fingercut.game.model.web.graph;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import com.spiderluck.fingercut.utils.Savable;

public class Edge implements Savable {
	protected Node node1, node2;

	public enum Keys {
		Node1,
		Node2
	}

	public Edge(Node v1, Node v2) {
		node1 = v1;
		node2 = v2;

		node1.addEdge(this);
		node2.addEdge(this);
	}

	public Edge(Map<Integer, Node> nodeMap, JSONObject json) throws JSONException {
		node1 = nodeMap.get(json.getInt(Keys.Node1.toString()));
		node2 = nodeMap.get(json.getInt(Keys.Node2.toString()));

		if(node1 == null || node2 == null) {
			throw new JSONException("Cannot find node(s)");
		}

		node1.addEdge(this);
		node2.addEdge(this);
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();

		json.put(Keys.Node1.toString(), node1.getId());
		json.put(Keys.Node2.toString(), node2.getId());

		return json;
	}

	public Node next(Node prev) {
		if (prev == node1) {
			return node2;
		} else {
			return node1;
		}
	}

	public Node getNode1() {
		return node1;
	}

	public Node getNode2() {
		return node2;
	}
}
