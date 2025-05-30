public class LangermannIndividualFactory implements IndividualFactory {
    private int n;

    public LangermannIndividualFactory(int n){
        this.n = n;
    }


    public LangermannIndividual getInstance(){
        return new LangermannIndividual(n);
    }

    public LangermannIndividual createIndividual(){
        return LangermannIndividual;
    }
}
