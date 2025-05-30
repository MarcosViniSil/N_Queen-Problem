public class App {
    public static void main(String[] args) throws Exception {
        
        final int N_POPULATION = 100;      
        final int N_ELITE = 1;              
        final int N_GENERATION = 2000;    
        final int N_DIMENSION = 4;          

        final double valueExpectedStyblinski = (-39.16 * N_DIMENSION);
        
        StyblinskiTangIndividualFactory styblinski = new StyblinskiTangIndividualFactory(N_DIMENSION);
        DixonPriceIndividualFactory dixon = new DixonPriceIndividualFactory(N_DIMENSION);
        LangermannIndividualFactory langermann = new LangermannIndividualFactory(N_DIMENSION);
        
        //GeneticAlgorithm.execute(N_POPULATION,N_ELITE,styblinski,N_GENERATION,valueExpectedStyblinski,0.4);
        //GeneticAlgorithm.execute(N_POPULATION,N_ELITE,dixon,N_GENERATION,0,0.06);
        GeneticAlgorithm.execute(N_POPULATION,N_ELITE,langermann,N_GENERATION,-4.1,0.01);

        
    }
}