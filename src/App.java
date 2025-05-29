public class App {
    public static void main(String[] args) throws Exception {
        
        final int N_POPULATION = 70;      
        final int N_ELITE = 1;              
        final int N_GENERATION = 2000;    
        final int N_DIMENSION = 4;          

        final double valueExpected = (-39.16599 * N_DIMENSION);

        StyblinskiTangIndividualFactory unitFactory = new StyblinskiTangIndividualFactory(N_DIMENSION);
        
        Individual guess = GeneticAlgorithm.execute(N_POPULATION,N_ELITE,unitFactory,N_GENERATION,valueExpected);

        
    }
}