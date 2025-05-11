import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class Ag {

    public static Unit execute(int nPopulation, int nElite, NQueensUnitFactory nQueens, int nGeneration,
            boolean isMin) {
        List<Unit> initialPopulation = new ArrayList<>(nPopulation);

        for (int i = 0; i < nPopulation; i++) {
            initialPopulation.add(nQueens.getInstance());
        }

        for (int i = 0; i < nGeneration; i++) {
            List<Unit> initialPopulationAux = new ArrayList<>(nPopulation);
            initialPopulationAux.addAll(initialPopulation);

            Random random = new Random();
            List<Unit> offspring = new ArrayList<>();

            while (initialPopulationAux.size() >= 2) {

                int indexParent1 = random.nextInt(initialPopulationAux.size());
                Unit parent1 = initialPopulationAux.remove(indexParent1);

                int indexParent2 = random.nextInt(initialPopulationAux.size());
                Unit parent2 = initialPopulationAux.remove(indexParent2);

                List<Unit> children = parent1.crossover(parent2);
                offspring.addAll(children);

            }

            List<Unit> mutants = new ArrayList<>(nPopulation);
            for (Unit unit : initialPopulation) {
                mutants.add(unit.mutate());
            }

            List<Unit> join = new ArrayList<>(nPopulation * 3);
            join.addAll(initialPopulation);
            join.addAll(offspring);
            join.addAll(mutants);

            List<Unit> newPopulation = new ArrayList<>();

            join.sort((a, b) -> {
                if (a.getFitness() < b.getFitness()) {
                    return -1;
                } else if (a.getFitness() > b.getFitness()) {
                    return 1;
                } else {
                    return 0;
                }
            });

            for (int k = 0; k < nElite; k++) {
                newPopulation.add(join.remove(k));
            }

            int remainPopulation = nPopulation - nElite;
            remainPopulation = Math.min(remainPopulation, join.size());
            int sumFitness = 0;
            for (Unit unit : join) {
                sumFitness += unit.getFitness();
            }

            for (int r = 0; r < remainPopulation; r++) {
                int spin = random.nextInt(sumFitness);
                int cumulativeSum = 0;

                Iterator<Unit> iterator = join.iterator();
                while (iterator.hasNext()) {
                    Unit unit = iterator.next();
                    cumulativeSum += unit.getFitness();

                    if (spin < cumulativeSum) {
                        newPopulation.add(unit);
                        sumFitness -= unit.getFitness();
                        iterator.remove();
                        break;
                    }
                }
            }

            initialPopulation = newPopulation;
        }
       return null;
    }
}
