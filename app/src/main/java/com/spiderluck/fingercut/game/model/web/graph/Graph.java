package com.spiderluck.fingercut.game.model.web.graph;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.spiderluck.fingercut.utils.Savable;

public abstract class Graph implements Savable {
	protected ArrayList<Node> nodes;
	protected ArrayList<Edge> edges;

	public enum Keys {
		Nodes,
		Edges
	}

	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject state = new JSONObject();

		JSONArray nodeStates = new JSONArray();
		JSONArray edgeStates = new JSONArray();

		for (Node node : nodes) {
			JSONObject nodeState = node.toJSON();
			nodeStates.put(nodeState);
		}

		for (Edge edge : edges) {
			JSONObject edgeState = edge.toJSON();
			edgeStates.put(edgeState);
		}

		state.put(Keys.Nodes.toString(), nodeStates);
		state.put(Keys.Edges.toString(), edgeStates);

		return state;
	}

	public void onRemoveEdge(Edge edge) {
		Node n1 = edge.getNode1();
		n1.removeEdge(edge);
		if (n1.getEdges().size() == 0) {
			nodes.remove(n1);
		}

		Node n2 = edge.getNode2();
		n2.removeEdge(edge);
		if (n2.getEdges().size() == 0) {
			nodes.remove(n2);
		}
	}

	public Edge getRandomEdge() {
		int i = (int) Math.floor(Math.random() * edges.size());
		return edges.get(i);
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	private LinkedList<Node> previousToPath(Node found, HashMap<Node, Node> previous) {
		LinkedList<Node> path = new LinkedList<>();
		while (found != null) {
			path.addFirst(found);
			found = previous.get(found);
		}
		return path;
	}

	/**
	 * Non-optimal implementation of BFS
	 *
	 * @param start    start node
	 * @param toFind   end node
	 * @param maxLevel max hops number or -1
	 * @return List of Nodes from start (included) to toFind (included) if path exists or
	 * null if path doesn't exist or has 0 elements
	 */
	public LinkedList<Node> findPathToNode(Node start, Node toFind, int maxLevel) {
		if (start == toFind || toFind == null || start == null) {
			return null;
		}
		HashMap<Node, Node> previous = new HashMap<>();
		HashMap<Node, Integer> levels = new HashMap<>();
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(start);
		previous.put(start, null);
		levels.put(start, 0);
		while (!queue.isEmpty()) {
			Node v = queue.remove();
			if (v == toFind) {
				return previousToPath(v, previous);
			}

			int level = levels.get(v);
			if (level > maxLevel && maxLevel != -1) {
				return null;
			}

			for (Edge e : v.getEdges()) {
				Node next = e.next(v);
				if (!previous.containsKey(next)) {
					queue.add(next);
					previous.put(next, v);
					levels.put(next, level + 1);
				}
			}
		}
		return null;
	}

	//warning: inefficient
	public Node getNodeWithId(int id) {
		for (Node node : nodes) {
			if (node.getId() == id) {
				return node;
			}
		}
		return null;
	}
}
