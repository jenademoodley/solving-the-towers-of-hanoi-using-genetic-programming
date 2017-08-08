package part2;

import java.util.ArrayList;
import java.util.Random;

import part1.Tree;

public class Tournament {

	// Makes the Tournament from the population
	public static Tree[] makeTournament(ArrayList<Tree> tree, int size,
			long seed) {
		Random random = new Random(seed);
		Tree[] population = new Tree[size];
		int i = 0;
		// Removes trees from population so that no tree is chosen twice for the
		// tournament
		while (i < size) {
			int index = random.nextInt(tree.size());
			population[i] = tree.get(index);
			// count.add(index);
			tree.remove(index);
			i++;
		}

		// Tournament is with selection so add removed trees to population again
		for (int j = 0; j < population.length; j++)
			tree.add(population[j]);

		return population;
	}

	// Calculates the winner of the Tournament. Winner has the LOWEST Raw
	// Fitness Value
	public static Tree winner(Tree[] tree) {
		int index = 0;
		float count = tree[0].getFitness();
		float temp = 0;
		for (int i = 0; i < tree.length; i++) {
			temp = tree[i].getFitness();
			if (count > temp) {
				count = temp;
				index = i;
			}
		}
		// win = index;
		return tree[index];
	}

}
