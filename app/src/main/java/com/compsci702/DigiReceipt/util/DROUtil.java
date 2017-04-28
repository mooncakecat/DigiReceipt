package com.compsci702.DigiReceipt.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that contains Opaque Predicates
 */
public class DROUtil {

	private List<DRNode> list1 = new ArrayList<>(3);
	private List<DRNode> list2 = new ArrayList<>(3);

	private DRNode f, g, h;

	public DROUtil() {
		for (int i = 0; i < 3; i++) {
			insert(list1);
			insert(list2);
		}

		g = list1.get(move(list1.size()));
		h = list2.get(move(list2.size()));

		f = insert(list1);
		h = insert(list2);
	}

	private DRNode insert(List<DRNode> list) {
		DRNode node = new DRNode();
		list.add(node);
		node.node1 = list.get(move(list.size()));
		node.node2 = list.get(move(list.size()));
		return list.get(move(list.size()));
	}

	private int move(int size) {
		return (int) (Math.random() * (size - 1) + 0);
	}

	// this method gives us an unsure result i.e. can be true or false
	public boolean fEqualG() {
		return f == g;
	}

	// this method always returns false
	public boolean gEqualH() {
		return g == h;
	}

	// this method gives us an unsure result i.e. can be true or false
	public boolean fToken() {
		f.token = false;
		g.token = true;
		return f.token;
	}

	// this method always returns false
	public boolean fToken2() {
		f.token = true;
		h.token = false;
		return f.token;
	}
}
