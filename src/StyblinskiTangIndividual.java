import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class StyblinskiTangIndividual implements Individual {

    private double genes[];
    private int dimension;
    private final double LOWER_BOUND = -5.0;
    private final double UPPER_BOUND = 5.0;
    private final double ALPHA = 0.3;

    public StyblinskiTangIndividual(int dimension) {
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
        StyblinskiTangIndividual parent2 = (StyblinskiTangIndividual) other;
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

        StyblinskiTangIndividual child = new StyblinskiTangIndividual(dimension);
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

        StyblinskiTangIndividual mutant = new StyblinskiTangIndividual(dimension);
        mutant.setGenes(mutatedGenes);
        return mutant;
    }

    @Override
    public double getFitness() {
        return StyblinskiTangIndividual.styblinskiTang(genes);
    }

    public static double styblinskiTang(double[] x) {
        double sum = 0.0;
        for (double xi : x) {
            sum += Math.pow(xi, 4) - 16 * Math.pow(xi, 2) + 5 * xi;
        }
        return 0.5 * sum;
    }

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