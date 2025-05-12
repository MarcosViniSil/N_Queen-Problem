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
    private List<Individual> offspring;
    private List<Individual> mutants;
    private List<Individual> join;
    private List<Individual> newPopulation;
    private List<Individual> initialPopulation;
    private Random random;

    public GeneticAlgorithm(int nPopulation, int nElite, NQueensIndividualFactory nQueens, int nGeneration,
            boolean isMin) {
        this.nPopulation = nPopulation;
        this.nElite = nElite;
        this.nQueens = nQueens;
        this.nGeneration = nGeneration;
        this.isMin = isMin;

        this.offspring = new ArrayList<>();
        this.mutants = new ArrayList<>(this.nPopulation);
        this.join = new ArrayList<>(this.nPopulation * 3);
        this.initialPopulation = new ArrayList<>(this.nPopulation);
        this.newPopulation = new ArrayList<>();

        this.random = new Random();

    }

    public Individual execute() {
        this.createInitialPopulation();

        for (int i = 0; i < this.nGeneration; i++) {

            this.printBestDatasGeneration(i);

            if (this.isSolutionFound()) {
                return this.printSolution(i);
            }

            this.createOffspring();
            this.createMutants();

            this.makeJoin();

            this.insertEliteIntoNewPopulation();
            this.rouletteWheelSelection();

            this.updatePopulation();

        }
        System.out.println("Solução não foi encontrada com " + this.nGeneration + " gerações");
        return null;
    }

    public boolean isSolutionFound() {
        var individualCandidate = this.sortPopulationBasedOnFitness(this.initialPopulation).get(0);
        if (individualCandidate != null && individualCandidate instanceof NQueensIndividual) {
            NQueensIndividual queen = (NQueensIndividual) individualCandidate;
            if (queen.getFitness() == 0) {
                return true;
            }

        }
        return false;
    }

    public Individual getSolution() {
        return this.sortPopulationBasedOnFitness(this.initialPopulation).get(0);
    }

    public Individual printSolution(int generation){
        Individual solution = this.getSolution();
        System.out.print("Solução encontrada na geração "+generation+".");
        this.printQueens((NQueensIndividual) solution);
        return solution;
    }

    public void printBestDatasGeneration(int generation) {
        var individualCandidate = this.sortPopulationBasedOnFitness(this.initialPopulation).get(0);
        if (individualCandidate != null && individualCandidate instanceof NQueensIndividual) {
            NQueensIndividual queen = (NQueensIndividual) individualCandidate;
            System.out.print("Geração " + generation + " encaixe: " + queen.getFitness()+".");
            this.printQueens(queen);
        }
    }

    public void printQueens(NQueensIndividual queen) {
        NQueensIndividual actualQueen = (NQueensIndividual) queen;
        int[] genes = actualQueen.getGenes();
        System.out.print(" Posição das rainhas: ");
        for (int gene : genes) {
            System.out.print(gene + " ");
        }
        System.out.println();

    }

    public int getRemainPopulation() {
        int remainPopulation = this.nPopulation - this.nElite;
        remainPopulation = Math.min(remainPopulation, join.size());
        return remainPopulation;
    }

    public void rouletteWheelSelection() {
        int remainPopulation = this.getRemainPopulation();
        int sumFitness = this.getSumFitness();

        for (int r = 0; r < remainPopulation; r++) {
            int spin = this.random.nextInt(sumFitness);
            int cumulativeSum = 0;

            Iterator<Individual> iterator = join.iterator();
            while (iterator.hasNext()) {
                Individual Individual = iterator.next();
                cumulativeSum += Individual.getFitness();

                if (spin < cumulativeSum) {
                    this.newPopulation.add(Individual);
                    sumFitness -= Individual.getFitness();
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public int getSumFitness() {
        int sumFitness = 0;
        for (Individual Individual : this.join) {
            sumFitness += Individual.getFitness();
        }

        return sumFitness;
    }

    public void insertEliteIntoNewPopulation() {
        for (int k = 0; k < nElite; k++) {
            this.newPopulation.add(join.remove(k));
        }
    }

    public void createInitialPopulation() {
        for (int i = 0; i < this.nPopulation; i++) {
            this.initialPopulation.add(this.nQueens.getInstance());
        }
    }

    public void makeJoin() {

        this.join.addAll(this.initialPopulation);
        this.join.addAll(this.offspring);
        this.join.addAll(this.mutants);

        join = this.sortPopulationBasedOnFitness(join);
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

    public List<Individual> createOffspring() {
        List<Individual> initialPopulationAux = this.generateAuxPopulation();

        this.offspring.clear();

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

    public void createMutants() {
        this.mutants.clear();

        for (Individual Individual : initialPopulation) {
            this.mutants.add(Individual.mutate());
        }
    }

    public int generateRandomValueBetween(int max) {
        return this.random.nextInt(max);
    }

    public List<Individual> generateAuxPopulation() {
        List<Individual> initialPopulationAux = new ArrayList<>(this.nPopulation);
        initialPopulationAux.addAll(this.initialPopulation);

        return initialPopulationAux;
    }

    public void updatePopulation() {
        this.initialPopulation.clear();
        this.initialPopulation.addAll(this.newPopulation);

        this.newPopulation.clear();
        this.mutants.clear();
        this.offspring.clear();
        this.join.clear();
    }
}
