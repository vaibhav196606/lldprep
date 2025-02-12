package lldexamples.vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

class Product{
    private String name;
    private int price;
    private int quantity;
    private int productId;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean inStock() {
        return quantity > 0;
    }
    public int getProductId() {
        return productId;
    }
    public Product(String name, int price, int quantity, int productId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
    }

}

interface TransactionState{
    public void insertCash(Transaction transaction,int amount);
    public void selectProduct(Transaction transaction,ArrayList<Pair>products);
    public void getProduct(Transaction transaction);
    public void getChange(Transaction transaction);
    public void cancelTransaction(Transaction transaction);
}

class HasNoCashState implements TransactionState{

    @Override
    public void insertCash(Transaction transaction, int amount) {
        transaction.setCashInserted(transaction.getCashInserted() + amount);
        transaction.setState(new HasCashState());
    }

    @Override
    public void selectProduct(Transaction transaction,ArrayList<Pair>products) {
        System.out.println("Insert cash first");
    }

    @Override
    public void getProduct(Transaction transaction) {
        System.out.println("Insert cash first");
    }

    @Override
    public void getChange(Transaction transaction) {
        System.out.println("Insert cash first");
    }

    @Override
    public void cancelTransaction(Transaction transaction) {
        System.out.println("No transaction initiated yet to cancel");
    }
}

class HasCashState implements TransactionState{

    @Override
    public void insertCash(Transaction transaction, int amount) {
        System.out.println("Cash is inserted already");
    }

    @Override
    public void selectProduct(Transaction transaction, ArrayList<Pair>products) {
        System.out.println("Select the product");
        for(Product product : VendingMachine.getInstance().getProducts()){
            if(product.inStock()){
                System.out.println(product.getName()+" "+product.getPrice()+" "+product.getQuantity());
            }
        }
        for(Pair pair : products){
            transaction.addProduct(pair.productId,pair.quantity);
        }
        transaction.setState(new ProductSelectedState());
    }

    @Override
    public void getProduct(Transaction transaction) {
        System.out.println("Select product first");
    }

    @Override
    public void getChange(Transaction transaction) {
        System.out.println("Select product first");
    }

    @Override
    public void cancelTransaction(Transaction transaction) {
        System.out.println("Canceling transaction");
        transaction.setState(new CancelledState(transaction));
    }
}

class ProductSelectedState implements TransactionState{

    @Override
    public void insertCash(Transaction transaction, int amount) {
        System.out.println("Cash is inserted already");
    }

    @Override
    public void selectProduct(Transaction transaction,ArrayList<Pair>products) {
        System.out.println("Products are already selected");
    }

    @Override
    public void getProduct(Transaction transaction) {
        int cashInserted = transaction.getCashInserted();
        for(Pair productPair : transaction.getProductsSelected()){
            if(productPair.quantity > VendingMachine.getInstance().getProductsIdToProduct().get(productPair.productId).getQuantity()){
                System.out.println("Product selected is out of stock");
                transaction.setState(new CancelledState(transaction));
                break;
            }
            cashInserted -= productPair.quantity * VendingMachine.getInstance().getProductsIdToProduct().get(productPair.productId).getPrice();
            if(cashInserted <= 0){
                System.out.println("Cash is not enough");
                transaction.setState(new CancelledState(transaction));
                break;
            }
        }
        System.out.println("Product received");
        for(Pair productPair : transaction.getProductsSelected()){
            VendingMachine.getInstance().getProductsIdToProduct().get(productPair.productId).setQuantity(VendingMachine.getInstance().getProductsIdToProduct().get(productPair.productId).getQuantity()-productPair.quantity);
        }
        transaction.setCashInserted(cashInserted);
        transaction.setState(new ProductReceivedState());

    }

    @Override
    public void getChange(Transaction transaction) {
        System.out.println("Get product first");
    }

    @Override
    public void cancelTransaction(Transaction transaction) {
        System.out.println("Canceling transaction");
        transaction.setState(new CancelledState(transaction));
    }
}

class ProductReceivedState implements TransactionState{

    @Override
    public void insertCash(Transaction transaction, int amount) {
        System.out.println("Cash is inserted already");
    }

    @Override
    public void selectProduct(Transaction transaction,ArrayList<Pair>products) {
        System.out.println("Products are already selected ");
    }

    @Override
    public void getProduct(Transaction transaction) {
        System.out.println("Products are already received");
    }

    @Override
    public void getChange(Transaction transaction) {
        System.out.println("Change received:" + transaction.getCashInserted());
        transaction.setState(new HasNoCashState());
    }

    @Override
    public void cancelTransaction(Transaction transaction) {
        System.out.println("Transaction has been processed already");
    }
}

class CancelledState implements TransactionState{

    public CancelledState(Transaction transaction) {
        System.out.println("Geeting cash back : " + transaction.getCashInserted());
        transaction.setCashInserted(0);
        transaction.setState(new HasNoCashState());
    }
    @Override
    public void insertCash(Transaction transaction, int amount) {
        System.out.println("Transaction has been cancelled");
    }

    @Override
    public void selectProduct(Transaction transaction,ArrayList<Pair>products) {
        System.out.println("Transaction has been cancelled");
    }

    @Override
    public void getProduct(Transaction transaction) {
        System.out.println("Transaction has been cancelled");
    }

    @Override
    public void getChange(Transaction transaction) {
        System.out.println("Transaction has been cancelled");
    }

    @Override
    public void cancelTransaction(Transaction transaction) {
        System.out.println("Transaction has been cancelled");
    }
}

class Pair{
    public int productId;
    public int quantity;
    public Pair(int product, int quantity){
        this.productId = product;
        this.quantity = quantity;
    }
}

class Transaction{
    private TransactionState state;
    private int cashInserted;
    private ArrayList<Pair> productsSelected;

    Transaction(){
        productsSelected = new ArrayList<Pair>();
        state = new HasNoCashState();
    }

    public TransactionState getState() {
        return state;
    }
    public void setState(TransactionState state) {
        this.state = state;
    }

    public ArrayList<Pair> getProductsSelected() {
        return productsSelected;
    }

    public void addProduct(int productId, int quantity){
        productsSelected.add(new Pair(productId, quantity));
    }

    public int getCashInserted() {
        return cashInserted;
    }
    public void setCashInserted(int cashInserted) {
        this.cashInserted = cashInserted;
    }

    public void insertCash(int amount){
        state.insertCash(this, amount);
    }
    public void selectProduct(ArrayList<Pair> products){
        state.selectProduct(this, products);
    }
    public void getProduct(Transaction transaction){
        state.getProduct(transaction);
    }
    public void getChange(Transaction transaction){
        state.getChange(transaction);
    }
    public void cancelTransaction(Transaction transaction){
        state.cancelTransaction(transaction);
    }
}

class VendingMachine{
    private static VendingMachine instance;
    public ArrayList<Product> products;
    public HashMap<Integer, Product> productsIdToProduct;

    private VendingMachine() {
        products = new ArrayList<Product>();
        productsIdToProduct = new HashMap<Integer, Product>();
        products.add(new Product("Apple", 100, 100,1));
        products.add(new Product("Banana", 200, 200,2));
        products.add(new Product("Orange", 300, 300,3));
        for(Product product : products){
            productsIdToProduct.put(product.getProductId(), product);
        }
    }

    public static VendingMachine getInstance(){
        if(instance==null){
            synchronized (VendingMachine.class){
                if(instance==null){
                    instance = new VendingMachine();
                }
            }
        }
        return instance;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }

    public HashMap<Integer, Product> getProductsIdToProduct() {
        return productsIdToProduct;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

}

public class VendingMachApp {
    public static void main(String[] args) {
        Transaction transaction = new Transaction();
        transaction.insertCash(1000);
        ArrayList<Pair> products = new ArrayList<Pair>();
        products.add(new Pair(1, 1));
        products.add(new Pair(2, 1));
        transaction.selectProduct(products);
        transaction.getProduct(transaction);
        transaction.getChange(transaction);
    }
}

// UML available in the same folder