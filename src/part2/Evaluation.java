package part2;

import java.util.Stack;

import part1.Tree;
import part1.TreeNode;

public class Evaluation {

	private static Stack<Integer> LP;
	private static Stack<Integer> MP;
	private static Stack<Integer> RP;

	private static boolean isPole(String element) {
		if (element.equals("LP") || element.equals("MP")
				|| element.equals("RP"))
			return true;
		return false;
	}

	public static void displayPoles(Tree tree, int rings) {
		LP = new Stack<>();
		MP = new Stack<>();
		RP = new Stack<>();
		for (int i = rings; i >= 1; i--)
			LP.push(i);

		evaluate(tree.getRoot());

		for (int i = rings; i >= 1; i--) {
			if (LP.size() == i)
				System.out.print(LP.pop() + "\t");

			else
				System.out.print("\t");

			if (MP.size() == i)
				System.out.print(MP.pop() + "\t");

			else
				System.out.print("\t");

			if (RP.size() == i)
				System.out.print(RP.pop());

			else
				System.out.print("\t");

			System.out.println();
		}

		System.out.println("LP\tMP\tRP");
	}

	public static int getFitness(Tree tree, int rings) {
		LP = new Stack<>();
		MP = new Stack<>();
		RP = new Stack<>();
		for (int i = rings; i >= 1; i--)
			LP.push(i);

		evaluate(tree.getRoot());

		Stack<Integer> stack = new Stack<>();
		int fitness = rings;

		while (!RP.isEmpty())
			stack.push(RP.pop());

		while (!stack.isEmpty()) {
			int top = stack.pop();

			if (top == fitness) {
				fitness--;
			}

			else
				break;
		}
		return fitness;
	}

	public static void stepByStep(Tree tree, int rings) {
		LP = new Stack<>();
		MP = new Stack<>();
		RP = new Stack<>();
		for (int i = rings; i >= 1; i--)
			LP.push(i);

		// evaluate(tree.getRoot());
		stepByStep(tree.getRoot());
	}

	private static String stepByStep(TreeNode<String> root) {
		if (root == null)
			return "";
		String left = "";
		String right = "";
		if (root.getArity() != 0) {
			left = stepByStep(root.getChild(0));
			right = stepByStep(root.getChild(1));
		}

		if (isPole(root.getElement()))
			return root.getElement();

		else
			return step(left, right);

	}

	private static String step(String left, String right) {

		if (left.equals(right)) {
			return left;
		}

		else {

			Stack<Integer> leftStack = null;
			Stack<Integer> rightStack = null;
			switch (left) {
			case "LP":
				leftStack = LP;
				break;

			case "MP":
				leftStack = MP;
				break;

			case "RP":
				leftStack = RP;
				break;
			}

			switch (right) {
			case "LP":
				rightStack = LP;
				break;

			case "MP":
				rightStack = MP;
				break;

			case "RP":
				rightStack = RP;
				break;
			}

			int from = 0;
			int to = 0;

			if (leftStack.size() == 0) {
				return left;
			}

			else if (leftStack.size() != 0 && rightStack.size() == 0) {
				from = leftStack.pop();
				rightStack.push(from);
				System.out.println(left + "->" + right);
				return right;
			}

			else if (leftStack.size() != 0 && rightStack.size() != 0) {
				from = leftStack.peek();
				to = rightStack.peek();

				if (from > to) {
					return left;
				}

				else {
					from = leftStack.pop();
					rightStack.push(from);
					System.out.println(left + "->" + right);
					return right;
				}
			}

		}
		return left;
	}

	// Evaluates Expression Tree
	private static String evaluate(TreeNode<String> root) {
		if (root == null)
			return "";
		String left = "";
		String right = "";
		if (root.getArity() != 0) {
			left = evaluate(root.getChild(0));
			right = evaluate(root.getChild(1));
		}

		if (isPole(root.getElement()))
			return root.getElement();

		else
			return getResult(left, right);

	}

	private static String getResult(String left, String right) {

		if (left.equals(right)) {
			return left;
		}

		else {

			Stack<Integer> leftStack = null;
			Stack<Integer> rightStack = null;
			switch (left) {
			case "LP":
				leftStack = LP;
				break;

			case "MP":
				leftStack = MP;
				break;

			case "RP":
				leftStack = RP;
				break;
			}

			switch (right) {
			case "LP":
				rightStack = LP;
				break;

			case "MP":
				rightStack = MP;
				break;

			case "RP":
				rightStack = RP;
				break;
			}

			int from = 0;
			int to = 0;

			if (leftStack.size() == 0) {
				return left;
			}

			else if (leftStack.size() != 0 && rightStack.size() == 0) {
				from = leftStack.pop();
				rightStack.push(from);
				return right;
			}

			else if (leftStack.size() != 0 && rightStack.size() != 0) {
				from = leftStack.peek();
				to = rightStack.peek();

				if (from > to) {
					return left;
				}

				else {
					from = leftStack.pop();
					rightStack.push(from);
					return right;
				}
			}

		}
		return left;
	}

}
