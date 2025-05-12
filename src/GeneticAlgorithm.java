import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class GeneticAlgorithm {

    private int nPopulation;
    private int nElite;
    private NQueensIndividualFactory nQueens;
    private int nGeneration;
    private boolean isMin;

    public GeneticAlgorithm(int nPopulation, int nElite, NQueensIndividualFactory nQueens, int nGeneration,
            boolean isMin) {
        this.nPopulation = nPopulation;
        this.nElite = nElite;
        this.nQueens = nQueens;
        this.nGeneration = nGeneration;
        this.isMin = isMin;

    }

    public Individual execute() {
        List<Individual> initialPopulation = this.createInitialPopulation();

        for (int i = 0; i < this.nGeneration; i++) {
            Random random = new Random();

            for (Individual Individual : initialPopulation) {
                NQueensIndividual actualQueens = (NQueensIndividual) Individual;
                System.out.println("Geração " + i + " encaixe: "+actualQueens.getFitness());
                this.printQueens(actualQueens);
            }
            
            var individualCandidate = this.sortPopulationBasedOnFitness(initialPopulation).get(0);
            if (individualCandidate != null && individualCandidate instanceof NQueensIndividual) {
                NQueensIndividual queen = (NQueensIndividual) individualCandidate;
                if (queen.getFitness() == 0) {
                    System.out.println("Solução encontrada na geração " + i);
                    this.printQueens(queen);
                    return queen;
                }
            }
            List<Individual> offspring = this.createOffspring(initialPopulation);

            List<Individual> mutants = this.createMutants(initialPopulation);

            List<Individual> join = this.makeJoin(initialPopulation, offspring, mutants);

            List<Individual> newPopulation = new ArrayList<>();

            for (int k = 0; k < nElite; k++) {
                newPopulation.add(join.remove(k));
            }

            int remainPopulation = this.nPopulation - this.nElite;
            remainPopulation = Math.min(remainPopulation, join.size());
            int sumFitness = 0;
            for (Individual Individual : join) {
                sumFitness += Individual.getFitness();
            }

            for (int r = 0; r < remainPopulation; r++) {
                int spin = random.nextInt(sumFitness);
                int cumulativeSum = 0;

                Iterator<Individual> iterator = join.iterator();
                while (iterator.hasNext()) {
                    Individual Individual = iterator.next();
                    cumulativeSum += Individual.getFitness();

                    if (spin < cumulativeSum) {
                        newPopulation.add(Individual);
                        sumFitness -= Individual.getFitness();
                        iterator.remove();
                        break;
                    }
                }
            }

            initialPopulation = newPopulation;

        }
        return null;
    }

    public void printQueens(NQueensIndividual queen) {
        NQueensIndividual actualQueen = (NQueensIndividual) bestQueen;
        int[] genes = actualQueen.getGenes();
        System.out.print("Posição das rainhas: ");
        for (int gene : genes) {
            System.out.print(gene + " ");
        }
        System.out.println();
        System.out.println();

    }

    public List<Individual> createInitialPopulation() {
        List<Individual> initialPopulation = new ArrayList<>(this.nPopulation);

        for (int i = 0; i < nPopulation; i++) {
            initialPopulation.add(this.nQueens.getInstance());
        }

        return initialPopulation;
    }

    public List<Individual> makeJoin(List<Individual> initialPopulation, List<Individual> offspring,
            List<Individual> mutants) {

        List<Individual> join = new ArrayList<>(nPopulation * 3);
        join.addAll(initialPopulation);
        join.addAll(offspring);
        join.addAll(mutants);

        join = this.sortPopulationBasedOnFitness(join);

        return join;
    }

    public List<Individual> sortPopulationBasedOnFitness(List<Individual> join) {
        join.sort((a, b) -> {
            if (a.getFitness() < b.getFitness()) {
                return -1;
            } else if (a.getFitness() > b.getFitness()) {
                return 1;
            } else {
                return 0;
            }
        });

        return join;
    }

    public List<Individual> createOffspring(List<Individual> initialPopulation) {
        List<Individual> initialPopulationAux = this.generateAuxPopulation(initialPopulation);

        List<Individual> offspring = new ArrayList<>();

        while (initialPopulationAux.size() >= 2) {

            int indexParent1 = this.generateRandomValueBetween(initialPopulationAux.size());
            Individual parent1 = initialPopulationAux.remove(indexParent1);

            int indexParent2 = this.generateRandomValueBetween(initialPopulationAux.size());
            Individual parent2 = initialPopulationAux.remove(indexParent2);

            List<Individual> children = parent1.crossover(parent2);
            offspring.addAll(children);

        }

        return offspring;
    }

    public List<Individual> createMutants(List<Individual> initialPopulation) {
        List<Individual> mutants = new ArrayList<>(this.nPopulation);

        for (Individual Individual : initialPopulation) {
            mutants.add(Individual.mutate());
        }

        return mutants;
    }

    public int generateRandomValueBetween(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public List<Individual> generateAuxPopulation(List<Individual> initialPopulation) {
        List<Individual> initialPopulationAux = new ArrayList<>(this.nPopulation);
        initialPopulationAux.addAll(initialPopulation);

        return initialPopulationAux;
    }
}
