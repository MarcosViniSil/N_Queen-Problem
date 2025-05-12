public class NQueensIndividualFactory implements IndividualFactory {
    
    private int n;

    public NQueensIndividualFactory(int n){
        this.n = n;
    }


    public NQueensIndividual getInstance(){
        return new NQueensIndividual(n);
    }

}
