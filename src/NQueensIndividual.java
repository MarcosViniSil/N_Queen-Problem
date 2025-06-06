import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NQueensIndividual implements Individual  {

    private int genes[];
    private int n;

    public NQueensIndividual(int n) {
        this.n = n;
        this.genes = new int[this.n];
        this.createPopulation();
    }

    @Override
    public List<Individual > crossover(Individual individual) {
            
        NQueensIndividual parent2 = (NQueensIndividual) individual;

        int crossoverPoint1 = this.generateRandomValue(this.n);
        int crossoverPoint2 = this.generateRandomValue(this.n);

        int[] child1Genes = new int[this.n];
        int[] child2Genes = new int[this.n];

        for (int i = 0; i < this.n; i++) {
            if (i < crossoverPoint1 || i > crossoverPoint2) {
                child1Genes[i] = this.genes[i];
                child2Genes[i] = parent2.genes[i];
            } else {
                child1Genes[i] = parent2.genes[i];
                child2Genes[i] = this.genes[i];
            }
        }

        return this.createOffspring(child1Genes, child2Genes);

    }

    @Override
    public Individual mutate() {
        final double PROBABILITY = 0.2;

        Random random = new Random();

        int[] mutatedGenes = this.genes.clone();
        boolean atLeastOneMutation = false;

        for (int i = 0; i < this.n; i++) {
            if (random.nextDouble() < PROBABILITY) {
                mutatedGenes[i] = this.generateRandomValue(this.n);
                atLeastOneMutation = true;
            }
        }

        if (!atLeastOneMutation) {
            int randomIndex = random.nextInt(this.n);
            mutatedGenes[randomIndex] = this.generateRandomValue(this.n);
        }

        NQueensIndividual mutant = new NQueensIndividual(this.n);
        mutant.setGenes(mutatedGenes);

        return mutant;
    }

    @Override
    public int getFitness() {
        // O calculo do fitness se da pela quantidade de colisoes, por exemplo, 1
        // colisão, fitness -> 1 , 0 colisões, fitness ->0
        // portanto, quanto mais próximo de 0 o fitness, melhor

        int linesCollision = this.getLineCollisions();
        int diagonalCollision = this.getDiagonalCollisions();
        return linesCollision + diagonalCollision;
    }

    public int getDiagonalCollisions() {
        int collisions = 0;
        int[] mainDiagonal = new int[2 * this.n - 1];
        int[] secDiagonal = new int[2 * this.n - 1];

        for (int col = 0; col < this.n; col++) {
            int row = this.genes[col];
            int mainIndex = col - row + (this.n - 1);
            int secIndex = col + row;

            collisions += mainDiagonal[mainIndex]++;
            collisions += secDiagonal[secIndex]++;
        }

        return collisions;
    }

    public int getLineCollisions() {
        int collisions = 0;
        int[] line = new int[this.n];

        for (int i = 0; i < this.n; i++) {
            int row = this.genes[i];

            collisions += line[row]++;
        }

        return collisions;
    }

    public List<Individual> createOffspring(int[] child1Genes, int[] child2Genes) {
        List<Individual > offspring = new ArrayList<>();

        NQueensIndividual child1 = new NQueensIndividual(this.n);
        NQueensIndividual child2 = new NQueensIndividual(this.n);

        child1.setGenes(child1Genes);
        child2.setGenes(child2Genes);

        offspring.add(child1);
        offspring.add(child2);

        return offspring;
    }

    public void createPopulation() {
        for (int i = 0; i < this.n; i++) {
            this.genes[i] = this.generateRandomValue(this.n);
        }
    }

    public int generateRandomValue(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    public int[] getGenes() {
        return genes;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
