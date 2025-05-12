import java.util.List;

public interface Individual {
    
    public List<Individual> crossover(Individual unit);

    public Individual mutate();

    public int getFitness();

}
