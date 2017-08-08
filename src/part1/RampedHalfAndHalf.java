package part1;

import java.util.ArrayList;
import java.util.Random;

public class RampedHalfAndHalf {

	// Creates a population based on the Ramped half and Half method
	public static ArrayList<Tree> ramped(int size, int depth, Random random) {

		ArrayList<Tree> tree = new ArrayList<Tree>();
		// Integer Division, enables population size to be correct even for odd
		// numbers
		// Grow always would have the extra tree
		int grow = (size + 1) / 2;
		int full = size / 2;
		for (int i = 2; i <= depth; i++) {
			for (int j = 0; j < (grow / (depth - 1)); j++) {
				Tree t = new Tree(i, 0, random);
				tree.add(t);
			}

			if ((i - 2) < grow % (depth - 1)) {
				Tree t = new Tree(i, 0, random);
				tree.add(t);
			}

			for (int j = 0; j < (full / (depth - 1)); j++) {
				Tree t = new Tree(i, 1, random);
				tree.add(t);
			}

			if ((i - 2) < full % (depth - 1)) {
				Tree t = new Tree(i, 1, random);
				tree.add(t);
			}

		}

		return tree;
	}

}
