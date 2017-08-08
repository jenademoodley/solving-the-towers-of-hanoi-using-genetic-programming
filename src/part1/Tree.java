package part1;

import java.util.Random;
import java.util.Stack;

public class Tree {
	// Variables
	private String[] terms = { "LP", "MP", "RP" };
	private String[] functions = { "MOVE" };
	private int[] arityArr = { 2 };
	private String[] primitives = { "MOVE", "LP", "MP", "RP" };
	private int[] primArr = { 2, 0, 0, 0 };
	protected TreeNode<String> root = null;
	protected int count = 0;
	private int depth;
	private int fitness = 0;
	private int hits = 0;

	// Default Constructor
	public Tree() {

	}

	public Tree(Tree tree) {
		root = createNewNode(tree.root.element, null, 1, tree.root.arity);
		Stack<TreeNode<String>> parent = new Stack<>();
		Stack<TreeNode<String>> current = new Stack<>();
		current.push(root);
		parent.push(tree.root);

		while (!parent.isEmpty()) {
			TreeNode<String> c = current.pop();
			TreeNode<String> p = parent.pop();

			if (p.arity != 0) {
				int index = 0;
				while (index < p.arity) {
					TreeNode<String> child = createNewNode(
							p.getChild(index).element, c, c.depth + 1,
							p.getChild(index).arity);
					c.setChild(child, index);
					current.push(c.getChild(index));
					parent.push(p.getChild(index));
					index++;
				}
			}

		}

		addID(root);
	}

	// Parametised Constructor. Creates a tree according to user input
	public Tree(int depth, int type, Random random) {

		setDepth(depth);

		// Creates Grow Tree
		if (type == 0)
			growTree(random);

		// Creates Full Tree
		else if (type == 1)
			fullTree(random);

		// Special Case. Can only be done in Mutation Class
		else if (type == 2)
			mutateTree(depth, random);

	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getHits() {
		return hits;
	}

	// Outputs Tree in Prefix Notation
	public void display() {
		System.out.print("Tree:\t\t");
		preOrder(root);
		System.out.println();
	}

	// Mutator method for the root
	public void setRoot(TreeNode<String> root) {
		this.root = root;
	}

	// Resets counter for ID. Must be private
	private void resetCount() {
		count = 0;
	}

	// Accessor method for the root
	public TreeNode<String> getRoot() {
		return root;
	}

	// Mutator method for fitness of the tree
	public void setFitness(int f) {
		this.fitness = f;
	}

	// Mutator method for depth of tree
	public void setDepth(int depth) {
		this.depth = depth;
	}

	// Accessor method for depth of tree
	public int getDepth() {
		return depth;
	}

	// Accessor method for fitness of the tree
	public int getFitness() {
		return fitness;
	}

	// Returns Number of nodes in the tree
	public int getNumNodes() {
		return count;
	}

	// Public declaration of findNode()
	public TreeNode<String> findNode(int id) {
		return findNode(root, id);
	}

	// finds the Node of a tree for a given ID
	private TreeNode<String> findNode(TreeNode<String> localRoot, int id) {
		Stack<TreeNode<String>> stack = new Stack<>();
		stack.push(localRoot);
		TreeNode<String> node = null;
		while (!stack.isEmpty()) {
			// Pops root, recursively visits all right nodes, then all left.
			// Order doesn't matter
			TreeNode<String> temp = stack.pop();
			if (temp.id == id) {
				// If Node is found, empty the stack, breaking the loop
				node = temp;
				stack.clear();
			} else if (temp.arity != 0) {
				int index = 0;

				while (index < temp.arity) {
					stack.push(temp.getChild(index));
					index++;
				}
			}
		}

		return node;
	}

	// resets the ID's of all Nodes in the tree
	public void resetID() {
		resetCount();
		addID(root);
		resetDepth();
	}

	// Readjusts all the ID's to be in correct order and numbering
	private void addID(TreeNode<String> localRoot) {

		int index = 0;
		localRoot.id = count;
		count++;

		while (index < localRoot.arity) {
			addID(localRoot.getChild(index));
			index++;
		}
	}

	// Correctly adjusts the depths of all nodes of the tree
	private void resetDepth() {
		int d = 0;
		Stack<TreeNode<String>> stack = new Stack<>();
		root.depth = 1;
		d = root.depth;
		stack.push(root);
		while (!stack.isEmpty()) {
			TreeNode<String> temp = stack.pop();
			if (temp.arity != 0) {
				int index = 0;
				while (index < temp.arity) {

					temp.getChild(index).depth = temp.depth + 1;
					stack.push(temp.getChild(index));

					if (d <= temp.depth + 1)
						d = temp.depth + 1;
					index++;
				}
			}

		}

		setDepth(d);
	}

	// Creates a tree based on the full method
	private void fullTree(Random random) {
		Stack<TreeNode<String>> stack = new Stack<>();
		// Only creates a tree for a minimum depth of two
		if (depth > 1) {
			int r = random.nextInt(functions.length);
			root = createNewNode(functions[r], null, 1, arityArr[r]);

			stack.push(root);
			while (!stack.isEmpty()) {
				TreeNode<String> temp = stack.pop();
				if (temp.depth == (depth - 1)) {
					// All bottom most nodes must be terminals in a full tree
					int index = 0;
					while (index < temp.arity) {
						r = random.nextInt(terms.length);
						temp.setChild(
								createNewNode(terms[r], temp, temp.depth + 1, 0),
								index);
						index++;
					}

				} else {
					// All but the bottom most nodes must be functions in a full
					// tree
					int index = 0;
					while (index < temp.arity) {
						r = random.nextInt(functions.length);
						temp.setChild(
								createNewNode(functions[r], temp,
										temp.depth + 1, arityArr[r]), index);
						stack.push(temp.getChild(index));
						index++;
					}

				}
			}
		}
		addID(root);
	}

	// Creates a tree based on the grow method
	private void growTree(Random random) {
		Stack<TreeNode<String>> stack = new Stack<>();

		if (depth > 1) {
			// Depth must be greater than two
			int r = random.nextInt(functions.length);
			root = createNewNode(functions[r], null, 1, arityArr[r]);

			stack.push(root);

			while (!stack.isEmpty()) {
				TreeNode<String> temp = stack.pop();
				// Terminals can't have children
				if (!((temp.element.equals("X")) || (temp.element.equals("1")))) {

					// Bottom most nodes must be terminals (If not completed
					// already)
					if (temp.depth == (depth - 1)) {
						int index = 0;
						while (index < temp.arity) {
							r = random.nextInt(terms.length);
							temp.setChild(
									createNewNode(terms[r], temp,
											temp.depth + 1, 0), index);
							index++;
						}

					} else {
						// Grow Tree can produce any symbol for any node
						int index = 0;
						while (index < temp.arity) {
							r = random.nextInt(primitives.length);
							temp.setChild(
									createNewNode(primitives[r], temp,
											temp.depth + 1, primArr[r]), index);
							stack.push(temp.getChild(index));
							index++;
						}
					}
				}

			}
		}
		addID(root);
	}

	// Special Tree creation used in mutation
	private void mutateTree(int depth, Random random) {
		// Same as grow tree except it allows the root to be a terminal
		Stack<TreeNode<String>> stack = new Stack<>();
		if (depth == 1) {
			int r = random.nextInt(terms.length);
			root = createNewNode(terms[r], null, 1, 0);
		}

		else if (depth > 1) {
			int r = random.nextInt(functions.length);
			root = createNewNode(functions[r], null, 1, arityArr[r]);

			stack.push(root);

			while (!stack.isEmpty()) {
				TreeNode<String> temp = stack.pop();
				// Terminals can't have children
				if (!((temp.element.equals("X")) || (temp.element.equals("1")))) {

					// Bottom most nodes must be terminals (If not completed
					// already)
					int index = 0;
					while (index < temp.arity) {
						r = random.nextInt(terms.length);
						temp.setChild(
								createNewNode(terms[r], temp, temp.depth + 1, 0),
								index);
						index++;
					}

				} else {
					// Grow Tree can produce any symbol for any node
					int index = 0;
					while (index < temp.arity) {
						r = random.nextInt(primitives.length);
						temp.setChild(
								createNewNode(primitives[r], temp,
										temp.depth + 1, primArr[r]), index);
						stack.push(temp.getChild(index));
						index++;
					}
				}
			}

		}
		addID(root);
	}

	// creates a new node in the tree
	private TreeNode<String> createNewNode(String e, TreeNode<String> p, int d,
			int a) {
		return new TreeNode<String>(0, e, p, d, a);
	}

	// Used to display the tree
	private void preOrder(TreeNode<String> localRoot) {
		if (localRoot != null && localRoot.depth != 0) {
			System.out.print(localRoot.element + "\t");
			int index = 0;

			while (index < localRoot.arity) {
				preOrder(localRoot.getChild(index));
				index++;
			}

		}
	}

	private String temp;

	public String toString() {
		temp = "Tree:\t\t";
		preOrderString(root);
		temp += "\n";
		return temp;
	}

	private void preOrderString(TreeNode<String> localRoot) {
		if (localRoot != null && localRoot.depth != 0) {
			temp += (localRoot.element + "\t");
			int index = 0;

			while (index < localRoot.arity) {
				preOrderString(localRoot.getChild(index));
				index++;
			}
		}
	}

}
