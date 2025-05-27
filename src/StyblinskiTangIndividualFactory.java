public interface StyblinskiTangIndividualFactory implements IndividualFactory {
    
    private int n;

    public StyblinskiTangIndividualFactory(int n){
        this.n = n;
    }


    public StyblinskiTangIndividual getInstance(){
        return new StyblinskiTangIndividual(n);
    }
}