public class App {
    public static void main(String[] args) throws Exception {
        
        final int N_POPULATION = 40;
        final int N_ELITE = 20;
        final int N_GENERATION = 3000;
        final int N_QUEENS = 10;

        StyblinskiTangIndividualFactory unitFactory = new StyblinskiTangIndividualFactory(N_QUEENS);
        GeneticAlgorithm ga = new GeneticAlgorithm(N_POPULATION, N_ELITE, unitFactory, N_GENERATION, false);
        ga.execute();

    }
}
