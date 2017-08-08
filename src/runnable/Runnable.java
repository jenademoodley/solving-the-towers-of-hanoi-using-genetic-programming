package runnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import part1.RampedHalfAndHalf;
import part1.Tree;
import part2.Evaluation;
import part2.Tournament;
import part3.Crossover;
import part3.Mutation;
import part3.Reproduction;

public class Runnable {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		int runs = 1;
		while (runs <= 1) {
			long startTime = System.currentTimeMillis();
			ArrayList<Tree> trees = new ArrayList<>();

			long seed = 0;
			ArrayList<Tree> initialPopulation = new ArrayList<>();
			ArrayList<Tree> offspring = null;
			int noOfOffspring = 0;
			int size = 0;
			int depth = 0;
			int offspringDepth = 0;
			int method = 0;
			Tree winner = null;
			Tree winner2 = null;
			int crossoverSize = 0;
			int mutationSize = 0;
			int reproductionSize = 0;
			int tournament = 10;
			int crossoverRate = 0;
			int mutationRate = 0;
			int reproductionRate = 0;
			int rings = 3;
			long finishTime = 0;
			try {
				Scanner sc = new Scanner(new File("input.txt"));

				while (!sc.hasNextLong())
					sc.next();

				seed = sc.nextLong();
				System.out.println("Seed:\t\t" + seed);

				while (!sc.hasNextInt())
					sc.next();
				rings = sc.nextInt();
				System.out.println("Rings:\t\t" + rings);

				while (!sc.hasNextInt())
					sc.next();
				size = sc.nextInt();
				System.out.println("Population:\t\t" + size);

				while (!sc.hasNextInt())
					sc.next();
				depth = sc.nextInt();
				System.out.println("Initial Tree Depth:\t\t\t" + depth);

				while (!sc.hasNextInt())
					sc.next();
				offspringDepth = sc.nextInt();
				System.out.println("Maximum Offspring Depth:\t\t\t"
						+ offspringDepth);

				while (!sc.hasNextInt())
					sc.next();

				method = sc.nextInt();
				System.out.println("Method:\t\t\t" + method);

				while (!sc.hasNextInt())
					sc.next();

				tournament = sc.nextInt();
				System.out.println("Tournament Size:\t" + tournament);

				while (!sc.hasNextInt())
					sc.next();

				crossoverRate = sc.nextInt();
				System.out.println("Crossover Rate:\t\t" + crossoverRate + "%");

				while (!sc.hasNextInt())
					sc.next();

				mutationRate = sc.nextInt();
				System.out.println("Mutation Rate:\t\t" + mutationRate + "%");

				while (!sc.hasNextInt())
					sc.next();

				reproductionRate = sc.nextInt();
				System.out.println("Reproduction Rate:\t" + reproductionRate
						+ "%");

				while (!sc.hasNextInt())
					sc.next();

				noOfOffspring = sc.nextInt();
				System.out.println("Number of Generations:\t" + noOfOffspring
						+ "\n");

				sc.close();
			} catch (Exception e) {

			}

			Random random = new Random(seed);
			int sum = crossoverRate + mutationRate + reproductionRate;

			if (sum != 100) {
				System.out
						.println("Incorrect Operator Rates. Operator Rates do not equal to 100%");
				System.exit(0);
			}

			if (offspringDepth < depth) {
				System.out
						.println("Offspring Depth must be >= the initial tree depth");
				System.exit(0);
			}

			if (rings < 3 || rings > 7) {
				System.out.println("Rings must be >=3 and <=7");
				System.exit(0);
			}

			crossoverSize = (int) (size * ((crossoverRate * 1.0) / 100));

			if (crossoverSize % 2 != 0)
				crossoverSize++;

			mutationSize = (int) (int) (size * ((mutationRate * 1.0) / 100));

			reproductionSize = size - crossoverSize - mutationSize;

			System.out.println("Population for Crossover:\t" + crossoverSize);
			System.out.println("Population for Mutation:\t" + mutationSize);
			System.out.println("Population for Reproduction:\t"
					+ reproductionSize);

			switch (method) {
			case 1:
				for (int i = 0; i < size; i++) {
					trees.add(new Tree(depth, 0, random));
					initialPopulation.add(trees.get(i));
				}
				break;

			case 2:
				for (int i = 0; i < size; i++) {
					trees.add(new Tree(depth, 1, random));
					initialPopulation.add(trees.get(i));
				}
				break;

			case 3:
				trees = RampedHalfAndHalf.ramped(size, depth, random);
				for (int i = 0; i < trees.size(); i++)
					initialPopulation.add(trees.get(i));
				break;

			default:
				System.out.println("Invalid Option");
				break;
			}

			boolean hit = false;
			Tree success = null;
			Tree best = null;

			for (int i = 0; i < trees.size(); i++) {
				int fitness = Evaluation.getFitness(trees.get(i), rings);
				trees.get(i).setFitness(fitness);
				int hitsratio = rings - fitness;
				trees.get(i).setHits(hitsratio);
				if (hitsratio == rings) {
					hit = true;
					success = trees.get(i);
					finishTime = System.currentTimeMillis();
					break;
				}
			}

			best = trees.get(0);
			for (int i = 1; i < trees.size(); i++) {
				if (trees.get(i).getFitness() < best.getFitness())
					best = trees.get(i);
			}

			int counter = 1;

			while (counter <= noOfOffspring && !hit) {
				offspring = new ArrayList<>();
				counter++;

				for (int i = 0; i < reproductionSize; i++) {
					winner = selectParent(trees, tournament, seed);
					offspring.add(Reproduction.reproduce(new Tree(winner)));
				}

				for (int i = 0; i < mutationSize; i++) {
					winner = selectParent(trees, tournament, seed);
					offspring.add(Mutation.mutate(new Tree(winner),
							offspringDepth, random));
				}

				for (int i = 0; i < (crossoverSize / 2); i++) {
					winner = selectParent(trees, tournament, seed);
					winner2 = selectParent(trees, tournament, seed);
					Tree[] cross = Crossover.crossover(new Tree(winner),
							new Tree(winner2), offspringDepth, random);
					for (int j = 0; j < 2; j++)
						offspring.add(cross[j]);
				}

				for (int i = 0; i < offspring.size(); i++) {
					int fitness = Evaluation
							.getFitness(offspring.get(i), rings);
					offspring.get(i).setFitness(fitness);
					int hitsratio = rings - fitness;
					offspring.get(i).setHits(hitsratio);
					if (hitsratio == rings) {
						hit = true;
						success = offspring.get(i);
						finishTime = System.currentTimeMillis();
						break;
					}
				}

				trees = (ArrayList<Tree>) offspring.clone();
				best = trees.get(0);
				for (int i = 1; i < trees.size(); i++) {
					if (trees.get(i).getFitness() < best.getFitness())
						best = trees.get(i);
				}
				if (counter <= noOfOffspring)
					offspring = null;

				else
					finishTime = System.currentTimeMillis();
			}

			BufferedWriter bw = null;
			FileWriter fw = null;
			String FILENAME = rings + " Rings - Output\\output";
			int t = 1;
			try {

				File f = new File(FILENAME + t + ".txt");

				// if file doesn't exists, then create it

				while (f.exists()) {
					t++;
					f = new File(FILENAME + t + ".txt");
				}
				f.createNewFile();

				// true = append file
				fw = new FileWriter(f.getAbsoluteFile(), true);
				bw = new BufferedWriter(fw);

				System.out.println("Parameters");
				bw.write("Parameters\n");

				System.out.println("===========");
				bw.write("===========\n");

				System.out.println("Population Size:\t" + size);
				bw.write("Population Size:\t\t" + size + "\n");

				System.out.println("Initial Tree Depth:\t\t" + depth);
				bw.write("Tree Depth:\t\t\t\t" + depth + "\n");

				System.out.println("Maximum Offspring Size:\t\t"
						+ offspringDepth);
				bw.write("Maximum Offspring Size:\t\t\t\t" + offspringDepth
						+ "\n");

				System.out.print("Method:\t\t\t");
				bw.write("Method:\t\t\t\t\t");
				switch (method) {
				case 1:
					System.out.println("Grow Method");
					bw.write("Grow Method\n");
					break;
				case 2:
					System.out.println("Full Method");
					bw.write("Full Method\n");
					break;
				case 3:
					System.out.println("Ramped Half and Half Method");
					bw.write("Ramped Half and Half Method\n");
					break;
				}

				System.out.println("Tournament Size:\t" + tournament);
				bw.write("Tournament Size:\t\t" + tournament + "\n");

				System.out.println("Crossover Rate:\t" + crossoverRate + "%");
				bw.write("Crossover Rate:\t\t\t" + crossoverRate + "%\n");

				System.out.println("Mutation Rate:\t" + mutationRate + "%");
				bw.write("Mutation Rate:\t\t\t" + mutationRate + "%\n");

				System.out.println("Reproduction Rate:\t" + reproductionRate
						+ "%");
				bw.write("Reproduction Rate:\t\t" + reproductionRate + "%\n");

				System.out.println("Number of Generations:\t" + noOfOffspring);
				bw.write("Number of Generations:\t" + noOfOffspring + "\n\n");

				System.out.println("Seed used: " + ":\t" + seed);
				bw.write("Seed used: " + ":\t" + seed + "\n");

				System.out.println("Rings: " + ":\t" + rings + "\n");
				bw.write("Rings: " + ":\t" + rings + "\n\n");

				if (success == null) {
					System.out.println("No successes");
					bw.write("No successes\n");

					System.out.println("Best Solution");
					bw.write("Best Solution\n");

					System.out.println(best.toString());
					bw.write(best.toString());

					System.out.println("Fitness:\t" + best.getFitness());

					bw.write("Fitness:\t" + best.getFitness() + "\n");

					System.out.println("Hit Ratio:\t" + best.getHits());
					bw.write("Hit Ratio:\t" + best.getHits() + "\n\n");

					double accuracy = (double) (best.getHits() * 1.0 / rings * 100);
					System.out.println("Accuracy Rate:\t" + accuracy + "%");
					bw.write("Accuracy Rate:\t" + accuracy + "%" + "\n\n");

				} else {
					System.out.println("Success");
					bw.write("Success\n");
					System.out.println("Generation of Success: "
							+ (counter - 1));

					bw.write("Generation of Success: " + (counter - 1) + "\n");

					System.out.println(success.toString());
					bw.write(success.toString());

					System.out.println("Fitness:\t" + success.getFitness());

					bw.write("Fitness:\t" + success.getFitness() + "\n");

					System.out.println("Hit Ratio:\t" + success.getHits());
					bw.write("Hit Ratio:\t" + success.getHits() + "\n\n");

					double accuracy = 100.0;

					System.out.println("Accuracy Rate:\t" + accuracy + "%");
					bw.write("Accuracy Rate:\t" + accuracy + "%" + "\n\n");

					// Evaluation.displayPoles(success, rings);
					// Evaluation.stepByStep(success, rings);
					// System.out.println();
				}

				double time = (double) (finishTime - startTime) / 1000;
				System.out.println("Time Taken:\t" + time + " seconds");
				bw.write("Time Taken:\t" + time + " seconds");

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
			}
			runs++;
		}

	}

	// Selects Parent to undergo generic operations
	public static Tree selectParent(ArrayList<Tree> trees, int t, long seed) {
		int tournament = t;
		Tree winner = null;
		Tree[] pop1 = null;

		pop1 = Tournament.makeTournament(trees, tournament, seed);
		winner = Tournament.winner(pop1);
		return winner;
	}

}
