import java.util.List;

public interface Unit {
    
    public List<Unit> crossover(Unit unit);

    public Unit mutate();

    public int getFitness();

}
