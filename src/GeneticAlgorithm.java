import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    public static Individual execute(int nPopulation, int nElite, IndividualFactory individualFactory, int nGeneration,
            double targetFitness) {
        List<Individual> initialPopulation = new ArrayList<>(nPopulation);

        for (int k = 0; k < nPopulation; k++) {
            initialPopulation.add(individualFactory.getInstance());
        }

        for (int i = 0; i < nGeneration; i++) {
            final int generation = i;

            initialPopulation.stream().forEach(
                    e -> {
                        System.out.println();
                        System.out.print("Geração: " + generation + ". Valores: ");
                        var individual = (StyblinskiTangIndividual) e;
                        System.out.print("[");
                        for (int j = 0; j < individual.getGenes().length; j++) {
                            System.out.printf("%.2f ", individual.getGenes()[j]);
                        }
                        
                        System.out.printf("%s %s %.3f", "]", "Fitness", individual.getFitness());
                        System.out.println();
                    }

            );

            List<Individual> initialPopulationAux = new ArrayList<>(nPopulation);
            initialPopulationAux.addAll(initialPopulation);

            Random random = new Random();
            List<Individual> offspring = new ArrayList<>();

            while (initialPopulationAux.size() >= 2) {
                int indexParent1 = random.nextInt(initialPopulationAux.size());
                Individual parent1 = initialPopulationAux.remove(indexParent1);

                int indexParent2 = random.nextInt(initialPopulationAux.size());
                Individual parent2 = initialPopulationAux.remove(indexParent2);

                List<Individual> children = parent1.crossover(parent2);
                offspring.addAll(children);
            }

            List<Individual> mutants = new ArrayList<>(nPopulation);
            for (Individual individual : initialPopulation) {
                mutants.add(individual.mutate());
            }

            List<Individual> join = new ArrayList<>(nPopulation * 3);
            join.addAll(initialPopulation);
            join.addAll(offspring);
            join.addAll(mutants);

            List<Individual> newPopulation = new ArrayList<>();

            join.sort((a, b) -> Double.compare(a.getFitness(), b.getFitness()));

            for (int k = 0; k < nElite; k++) {
                newPopulation.add(join.remove(k));
            }

            int remainPopulation = nPopulation - nElite;
            remainPopulation = Math.min(remainPopulation, join.size());
            double sumFitness = 0;

            double worstFitness = join.stream().mapToDouble(Individual::getFitness).max().orElse(0);

            for (Individual individual : join) {
                sumFitness += (worstFitness - individual.getFitness());
            }

            for (int r = 0; r < remainPopulation; r++) {
                double spin = random.nextDouble(sumFitness);
                double cumulativeSum = 0;

                Iterator<Individual> iterator = join.iterator();
                while (iterator.hasNext()) {
                    Individual individual = iterator.next();
                    cumulativeSum += (worstFitness - individual.getFitness());

                    if (spin <= cumulativeSum) {
                        newPopulation.add(individual);
                        sumFitness -= (worstFitness - individual.getFitness());
                        iterator.remove();
                        break;
                    }
                }
            }

            initialPopulation = newPopulation;

            StyblinskiTangIndividual bestIndividual = (StyblinskiTangIndividual) initialPopulation.get(0);
            double valueActual = bestIndividual.getFitness();
            
            final double TOLERANCE = 0.4;
            
            if (valueActual <= targetFitness || Math.abs(valueActual - targetFitness) < TOLERANCE) {
                
                System.out.println("\nSolução encontrada");
                System.out.print("Geração: " + generation + ". Valores: ");
                System.out.print("[ ");
                for (int j = 0; j < bestIndividual.getGenes().length; j++) {
                    System.out.printf("%.2f ", bestIndividual.getGenes()[j]);
                }
                System.out.printf("%s %s %.3f", "]", "Fitness", bestIndividual.getFitness());
                System.out.println();
                return initialPopulation.get(0);
            }

        }

        return initialPopulation.get(0);
    }
}