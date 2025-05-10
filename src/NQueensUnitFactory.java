public class NQueensUnitFactory implements UnitFactory {
    
    private int n;

    public NQueensUnitFactory(int n){
        this.n = n;
    }


    public Unit getInstance(){
        return new NQueensUnit(n);
    }

}
