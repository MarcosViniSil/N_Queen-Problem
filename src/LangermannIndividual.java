import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class LangermannIndividual implements Individual {

    private double genes[];
    private int dimension;
    private final double LOWER_BOUND = 0;
    private final double UPPER_BOUND = 10.0;
    private final double ALPHA = 0.3;
    private static final int M = 5;
    private static final double[][] matrixA = {
        {3, 5},
        {5, 2},
        {2, 1},
        {1, 4},
        {7, 9}
    };

    private static final double[] vectorC = {1, 2, 5, 2, 3};

    public LangermannIndividual(int dimension) {
        if (dimension > 2) {
            dimension = 2;
        }
        this.dimension = dimension;
        this.genes = new double[dimension];
        this.randomizeGenes();
    }

    private void randomizeGenes() {
        Random rand = new Random();
        for (int i = 0; i < dimension; i++) {
            genes[i] = LOWER_BOUND + (UPPER_BOUND - LOWER_BOUND) * rand.nextDouble();
        }
    }

    @Override
    public List<Individual> crossover(Individual other) {
        LangermannIndividual parent2 = (LangermannIndividual) other;
        Random rand = new Random();

        double[] childGenes = new double[dimension];

        for (int i = 0; i < dimension; i++) {
            double x1 = this.genes[i];
            double x2 = parent2.genes[i];
            double cMin = Math.min(x1, x2);
            double cMax = Math.max(x1, x2);
            double I = cMax - cMin;

            double lower = cMin - ALPHA * I;
            double upper = cMax + ALPHA * I;

            childGenes[i] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, lower + (upper - lower) * rand.nextDouble()));
        }

        LangermannIndividual child = new LangermannIndividual(dimension);
        child.setGenes(childGenes);

        List<Individual> offspring = new ArrayList<>();
        offspring.add(child);
        return offspring;
    }

    @Override
    public Individual mutate() {
        final double MUTATION_RATE = 0.2;
        final double MUTATION_STDDEV = 0.1 * (UPPER_BOUND - LOWER_BOUND);

        Random rand = new Random();
        double[] mutatedGenes = genes.clone();

        for (int i = 0; i < dimension; i++) {
            if (rand.nextDouble() < MUTATION_RATE) {
                double noise = rand.nextGaussian() * MUTATION_STDDEV;
                mutatedGenes[i] += noise;
                mutatedGenes[i] = Math.max(LOWER_BOUND, Math.min(UPPER_BOUND, mutatedGenes[i]));
            }
        }

        LangermannIndividual mutant = new LangermannIndividual(dimension);
        mutant.setGenes(mutatedGenes);
        return mutant;
    }

    @Override
    public double getFitness() {
        double sum = 0.0;
        for (int i = 0; i < M; i++) {
            double sumSq = 0.0;
            for (int j = 0; j <this.dimension; j++) {
                double diff = this.genes[j] - matrixA[i][j];
                sumSq += diff * diff;
            }
            double term = vectorC[i] * Math.exp(-sumSq / Math.PI) * Math.cos(Math.PI * sumSq);
            sum += term;
        }
        return sum;
    }


    @Override
    public double[] getGenes() {
        return genes;
    }

    public void setGenes(double[] genes) {
        this.genes = genes;
    }

    public int getDimension() {
        return dimension;
    }
}
