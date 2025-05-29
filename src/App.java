public class App {
    public static void main(String[] args) throws Exception {
        
        final int N_POPULATION = 100;      
        final int N_ELITE = 1;              
        final int N_GENERATION = 2000;    
        final int N_DIMENSION = 4;          

        StyblinskiTangIndividualFactory unitFactory = new StyblinskiTangIndividualFactory(N_DIMENSION);
        GeneticAlgorithm ga = new GeneticAlgorithm(N_POPULATION, N_ELITE, unitFactory, N_GENERATION, true,N_DIMENSION);
        ga.execute();

    }
}
