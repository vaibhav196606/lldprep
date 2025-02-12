package patterns;

interface VendingMachineState{
    public void insertCoin(VendingMachine machine);
    public void selectProduct(VendingMachine machine);
    public void getProduct(VendingMachine machine);
}

class HasNoCoinState implements VendingMachineState{

    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin has inserted");
        machine.setState(new HasCoinState());
    }

    @Override
    public void selectProduct(VendingMachine machine) {
        System.out.println("Insert coin first");
    }

    @Override
    public void getProduct(VendingMachine machine) {
        System.out.println("Insert coin first");
    }
}

class HasCoinState implements VendingMachineState{

    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin has inserted already");
    }

    @Override
    public void selectProduct(VendingMachine machine) {
        System.out.println("Product has selected");
        machine.setState(new ProductSelectedState());
    }

    @Override
    public void getProduct(VendingMachine machine) {
        System.out.println("Select product first");
    }
}

class ProductSelectedState implements VendingMachineState{

    @Override
    public void insertCoin(VendingMachine machine) {
        System.out.println("Coin has inserted already");
    }

    @Override
    public void selectProduct(VendingMachine machine) {
        System.out.println("Product has selected already");
    }

    @Override
    public void getProduct(VendingMachine machine) {
        System.out.println("Product received");
        machine.setState(new HasCoinState());
    }
}

class VendingMachine{
    private VendingMachineState vendingMachineState;
    public VendingMachine(){
        this.vendingMachineState = new HasNoCoinState();
    }
    public void setState(VendingMachineState vendingMachineState){
        this.vendingMachineState = vendingMachineState;
    }
    public void insertCoin(){
        this.vendingMachineState.insertCoin(this);
    }
    public void selectProduct(){
        this.vendingMachineState.selectProduct(this);
    }
    public void getProduct(){
        this.vendingMachineState.getProduct(this);
    }
}

public class StateImpl {
    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine();
        machine.insertCoin();
        machine.getProduct(); //should not work, as the product is not selected
        machine.selectProduct();
        machine.getProduct();
    }
}
