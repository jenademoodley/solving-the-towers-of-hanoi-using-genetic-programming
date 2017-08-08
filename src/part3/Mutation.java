package part3;

import java.util.Random;

import part1.Tree;
import part1.TreeNode;

public class Mutation {

	// Creates offspring using mutation method
	public static Tree mutate(Tree parent, int size, Random random) {

		int d = random.nextInt(size) + 1;

		int max = 0;
		// Chooses a node at random to mutate
		int id = random.nextInt(parent.getNumNodes());

		while (parent.findNode(id).getDepth() > d)
			id = random.nextInt(parent.getNumNodes());

		// Node to mutate
		TreeNode<String> localRoot = parent.findNode(id);

		if (id == 0) {
			max = random.nextInt(d) + 1;
			Tree tree = new Tree(max, 2, random);
			localRoot.setNode(tree.getRoot());
		}

		else if (id > 0) {
			TreeNode<String> temp = localRoot.getParent();
			localRoot.setElement("");
			int num = d - temp.getDepth();
			max = random.nextInt(num) + 1;

			while (max >= d)
				max = random.nextInt(num) + 1;

			Tree t = new Tree(max, 2, random);

			boolean found = false;
			int index = 0;

			while (index < temp.getArity() || !found) {
				if (temp.getChild(index).getElement().equals("")) {
					found = true;
					t.getRoot().setParent(temp);
					temp.setChild(t.getRoot(), index);
				}
				index++;
			}

		}

		parent.resetID();
		parent.setDepth(parent.findNode(id).getDepth() + max - 1);

		return parent;

	}

}
