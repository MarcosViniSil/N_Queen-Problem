public class App {
    public static void main(String[] args) throws Exception {
        
        final int N_POPULATION = 20;
        final int N_ELITE = 2;
        final int N_GENERATION = 2000;
        final int N_QUEENS = 4;

        NQueensIndividualFactory unitFactory = new NQueensIndividualFactory(N_QUEENS);
        GeneticAlgorithm ga = new GeneticAlgorithm(N_POPULATION, N_ELITE, unitFactory, N_GENERATION, false);
        ga.execute();

    }
}
