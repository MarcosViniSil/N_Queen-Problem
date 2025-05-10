import java.util.ArrayList;

public interface Unit {
    
    public ArrayList<Unit> crossover(Unit unit);

    public Unit mutate();

    public int getFitness();

}
