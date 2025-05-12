public class App {
    public static void main(String[] args) throws Exception {
        
        final int N_POPULATION = 40;
        final int N_ELITE = 10;
        final int N_GENERATION = 3000;
        final int N_QUEENS = 10;

        NQueensIndividualFactory unitFactory = new NQueensIndividualFactory(N_QUEENS);
        GeneticAlgorithm ga = new GeneticAlgorithm(N_POPULATION, N_ELITE, unitFactory, N_GENERATION, false);
        ga.execute();

    }
}
