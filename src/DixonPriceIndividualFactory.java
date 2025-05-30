public class DixonPriceIndividualFactory implements IndividualFactory {
    private int n;

    public DixonPriceIndividualFactory(int n){
        this.n = n;
    }


    public DixonPriceIndividual getInstance(){
        return new DixonPriceIndividual(n);
    }

    public DixonPriceIndividual createIndividual(){
        return DixonPriceIndividual;
    }
}
