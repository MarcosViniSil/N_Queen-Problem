import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class GeneticAlgorithm {

    private int nPopulation;
    private int nElite;
    private StyblinskiTangIndividualFactory nQueens;
    private int nGeneration;
    private boolean isMin;
    private List<Individual> offspring;
    private List<Individual> mutants;
    private List<Individual> join;
    private List<Individual> newPopulation;
    private List<Individual> initialPopulation;
    private Random random;
    int dimension;

    public GeneticAlgorithm(int nPopulation, int nElite, StyblinskiTangIndividualFactory nQueens, int nGeneration,
            boolean isMin, int dimension) {
        this.nPopulation = nPopulation;
        this.nElite = nElite;
        this.nQueens = nQueens;
        this.nGeneration = nGeneration;
        this.isMin = isMin;
        this.dimension = dimension;
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
            this.tournamentSelection();

            this.updatePopulation();

        }
        System.out.println("Solução não foi encontrada com " + this.nGeneration + " gerações");
        return null;
    }

    public boolean isSolutionFound() {
        var best = this.sortPopulationBasedOnFitness(this.initialPopulation).get(0);
        if (best instanceof StyblinskiTangIndividual) {
            StyblinskiTangIndividual st = (StyblinskiTangIndividual) best;
            double fitness = st.getFitness();
            double expected = -39.16617 * dimension;
            return fitness < expected + 0.1; 
        }
        return false;
    }

    public Individual getSolution() {
        return this.sortPopulationBasedOnFitness(this.initialPopulation).get(0);
    }

    public Individual printSolution(int generation) {
        Individual solution = this.getSolution();
        System.out.print("Solução encontrada na geração " + generation + ".");
        this.printQueens((StyblinskiTangIndividual) solution);
        return solution;
    }

    public void printBestDatasGeneration(int generation) {
        if (this.initialPopulation.isEmpty()) {
            return;
        }
        var individualCandidate = this.sortPopulationBasedOnFitness(this.initialPopulation).get(0);
        if (individualCandidate != null && individualCandidate instanceof StyblinskiTangIndividual) {
            StyblinskiTangIndividual st = (StyblinskiTangIndividual) individualCandidate;
            System.out.print("Geração " + generation + " - Melhor Fitness: " + st.getFitness());
            this.printQueens(st);
        }
    }

    public void printQueens(StyblinskiTangIndividual queen) {
        System.out.println();
        double[] genes = queen.getGenes();
        System.out.print(" Posição: [");
        for (int i = 0; i < genes.length; i++) {
            System.out.printf("%.3f", genes[i]);
            if (i < genes.length - 1)
                System.out.print(", ");
        }
        System.out.println("]");
    }

    public int getRemainPopulation() {
        int remainPopulation = this.nPopulation - this.nElite;
        remainPopulation = Math.min(remainPopulation, join.size());
        return remainPopulation;
    }

    public void rouletteWheelSelection() {
        this.join = this.sortPopulationBasedOnFitness(this.join);
        double sumFitness = this.getSumFitness();
        int remainPopulation = this.getRemainPopulation();
        this.newPopulation.clear();

        for (int r = 0; r < remainPopulation; r++) {
            double spin = this.random.nextDouble() * sumFitness;
            double cumulativeSum = 0;

            Iterator<Individual> iterator = this.join.iterator();
            while (iterator.hasNext()) {
                Individual individual = iterator.next();
                cumulativeSum += individual.getFitness();
                if (spin <= cumulativeSum) {
                    this.newPopulation.add(individual);
                    sumFitness -= individual.getFitness();
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public void tournamentSelection() {
        int remainPopulation = this.getRemainPopulation();
        this.newPopulation.clear();

        for (int i = 0; i < remainPopulation; i++) {
            List<Individual> tournament = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                int randomIndex = random.nextInt(join.size());
                tournament.add(join.get(randomIndex));
            }

            tournament.sort((a, b) -> Double.compare(a.getFitness(), b.getFitness()));
            this.newPopulation.add(tournament.get(0));
        }
    }

    public double getSumFitness() {
        double sumFitness = 0;
        for (Individual individual : this.join) {
            sumFitness += individual.getFitness();
        }
        return sumFitness;
    }

    public void insertEliteIntoNewPopulation() {
        if (join.isEmpty())
            return;

        this.join = this.sortPopulationBasedOnFitness(this.join);
        int eliteCount = Math.min(nElite, join.size());

        for (int k = 0; k < eliteCount; k++) {
            this.newPopulation.add(join.get(k));
        }

        for (int k = 0; k < eliteCount; k++) {
            join.remove(0);
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

    public List<Individual> sortPopulationBasedOnFitness(List<Individual> population) {
        population.sort((a, b) -> {
            double fitnessA = StyblinskiTangIndividual.styblinskiTang(((StyblinskiTangIndividual) a).getGenes());
            double fitnessB = StyblinskiTangIndividual.styblinskiTang(((StyblinskiTangIndividual) b).getGenes());
            return Double.compare(fitnessA, fitnessB);
        });
        return population;
    }

    public void createOffspring() {
        List<Individual> initialPopulationAux = this.generateAuxPopulation();
        this.offspring.clear();

        while (initialPopulationAux.size() >= 2) {
            int indexParent1 = this.generateRandomValueBetween(initialPopulationAux.size());
            Individual parent1 = initialPopulationAux.remove(indexParent1);

            int indexParent2 = this.generateRandomValueBetween(initialPopulationAux.size());
            Individual parent2 = initialPopulationAux.remove(indexParent2);

            List<Individual> children = parent1.crossover(parent2);
            this.offspring.addAll(children);
        }
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

        if (!this.newPopulation.isEmpty()) {
            this.initialPopulation.addAll(this.newPopulation);
        } else {
            this.createInitialPopulation();
        }
        this.initialPopulation.addAll(this.newPopulation);

        this.newPopulation.clear();
        this.mutants.clear();
        this.offspring.clear();
        this.join.clear();
    }
}
