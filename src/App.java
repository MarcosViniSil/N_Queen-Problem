public class App {
    public static void main(String[] args) throws Exception {
        final int N_POPULATION = 20;
        final int N_ELITE = 4;
        final int N_GENERATION = 1000;

        final int N = 4;
        NQueensUnitFactory unitFactory = new NQueensUnitFactory(N);
        var a = Ag.execute(N_POPULATION, N_ELITE, unitFactory, N_GENERATION, false);

    }
}
