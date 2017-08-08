package part3;

import java.util.Random;

import part1.Tree;
import part1.TreeNode;

public class Crossover {

	// Creates offspring using crossover method

	public static Tree[] crossover(Tree tree1, Tree tree2, int size,
			Random random) {
		Tree[] tree;
		int offspringSize = random.nextInt(size) + 1;

		// Finds random crossover points in both parents
		int id1 = random.nextInt(tree1.getNumNodes());
		int id2 = random.nextInt(tree2.getNumNodes());

		Tree temp1 = new Tree(tree1);
		Tree temp3 = new Tree(tree1);

		Tree temp2 = new Tree(tree2);
		Tree temp4 = new Tree(tree2);

		TreeNode<String> node1 = temp1.findNode(id1);
		TreeNode<String> node3 = temp3.findNode(id1);

		TreeNode<String> node2 = temp2.findNode(id2);
		TreeNode<String> node4 = temp4.findNode(id2);

		node3.setNode(node2);
		node4.setNode(node1);

		temp3.resetID();
		temp4.resetID();

		// Repeat process if offspring size is greater than the specified size
		while (temp3.getDepth() > offspringSize
				|| temp4.getDepth() > offspringSize) {

			offspringSize = random.nextInt(size) + 1;

			id1 = random.nextInt(tree1.getNumNodes());
			id2 = random.nextInt(tree2.getNumNodes());
			temp1 = new Tree(tree1);
			temp3 = new Tree(tree1);

			temp2 = new Tree(tree2);
			temp4 = new Tree(tree2);

			node1 = temp1.findNode(id1);
			node3 = temp3.findNode(id1);

			node2 = temp2.findNode(id2);
			node4 = temp4.findNode(id2);

			node3.setNode(node2);
			node4.setNode(node1);

			temp3.resetID();
			temp4.resetID();

		}

		// Offspring returned in array form
		tree = new Tree[2];
		tree[0] = temp3;
		tree[1] = temp4;

		temp1 = null;
		temp2 = null;
		node1 = null;
		node2 = null;
		node3 = null;
		node4 = null;
		return tree;
	}

}
