package lldexamples.wallet;

//The digital wallet should allow users to create an account and manage their personal information.
//Users should be able to add and remove payment methods, such as credit cards or bank accounts.
//The digital wallet should support fund transfers between users and to external accounts.
//The system should handle transaction history and provide a statement of transactions.
//The digital wallet should support multiple currencies and perform currency conversions.
//The system should ensure the security of user information and transactions.
//The digital wallet should handle concurrent transactions and ensure data consistency.
//The system should be scalable to handle a large number of users and transactions.

//User
//Transaction
//PaymentMethods : CreditCard, DebitCard, UPI, Wallet
//Wallet : addMoney(), transferMoney(), payMoney()
//CurrencyConvertor

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

enum TransactionStatus {
    STARTED, PROCESSING, SUCCEEDED, FAILED
};

enum Currency {
    INR, USD, EUR
}

class User{
    String userID;
    String name;
    ArrayList<PaymentMethod> paymentMethods;
    Wallet wallet;
    ArrayList<Transaction> transactionHistory;
    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
        paymentMethods = new ArrayList<PaymentMethod>();
        wallet = new Wallet();
        this.transactionHistory = new ArrayList<Transaction>();
        addPaymentMethod(wallet);
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethods.add(paymentMethod);
    }

    public void transactionInfo(TransactionStatus status) {
        System.out.println("Your Transaction is : " + status);
    }
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}

class Transaction{
    TransactionStatus status;
    User fromUser;
    User toUser;
    int amount;
    Date timestamp;
    public Transaction(User fromUser, User toUser, int amount) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
        this.timestamp = new Date();
        this.status = TransactionStatus.STARTED;
    }
    public void setTransactionStatus(TransactionStatus status) {
        this.status = status;
        fromUser.transactionInfo(status);
        toUser.transactionInfo(status);
    }

}
interface PaymentMethod{
    void payMoney(int amount, User user,Transaction transaction);
}

class CurrencyConvertor{
    static HashMap<Currency, Double> supportedCurrencies = new HashMap<>();

    static {
        supportedCurrencies.put(Currency.USD, 1.0);
        supportedCurrencies.put(Currency.INR, 87.0);
        supportedCurrencies.put(Currency.EUR, 0.92);
    }
    public static double getConvertedCurrency(Currency fromCurrency, Currency toCurrency, double amount) {
        return (supportedCurrencies.get(toCurrency)/supportedCurrencies.get(fromCurrency)) * amount;
    }
}
class Wallet implements PaymentMethod{
    volatile int balance;
    public Wallet(){
        super();
        this.balance = 0;
    }
    @Override
    public synchronized void payMoney(int amount, User user, Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PROCESSING);
        if(balance < amount){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            System.out.println("Insufficient balance.");
            return;
        }
        this.subtractMoney(amount);
        user.wallet.addMoney(amount);
        transaction.setTransactionStatus(TransactionStatus.SUCCEEDED);
    }
    public synchronized void addMoney(int amount){
        this.balance += amount;
    }
    public synchronized void subtractMoney(int amount){
        if(this.balance - amount >=0) {
            this.balance -= amount;
        }
        else{
            System.out.println("You don't have enough money to make the transaction");
            throw new RuntimeException("You don't have enough money to make the transaction");
        }
    }

}

class CreditCard implements PaymentMethod{

    @Override
    public void payMoney(int amount, User user,Transaction transaction) {
        System.out.println("Processing payment via Credit Card...");
        transaction.setTransactionStatus(TransactionStatus.SUCCEEDED);
    }
}
class DebitCard implements PaymentMethod{

    @Override
    public void payMoney(int amount, User user,Transaction transaction) {
        System.out.println("Processing payment via Dbeit Card...");
        transaction.setTransactionStatus(TransactionStatus.SUCCEEDED);
    }
}

class BeingPayment{
    public static void beginPayment(User fromUser, User toUser, int amount, PaymentMethod paymentMethod, App app){
        if(!app.users.contains(toUser) || !app.users.contains(fromUser)) {
            System.out.println("User(s) is not in the wallet");
        }
        Transaction transaction = new Transaction(fromUser, toUser, amount);
        fromUser.addTransaction(transaction);
        toUser.addTransaction(transaction);
        paymentMethod.payMoney(amount, toUser, transaction);
    }
}

class App{
    HashSet<User> users;
    public App(){
        users = new HashSet<User>();
    }
    public void addUser(User user){
        users.add(user);
    }
    public void removeUser(User user){
        users.remove(user);
    }

    public void beginPayment(User fromUser, User toUser, int amount, PaymentMethod paymentMethod){
        BeingPayment.beginPayment(fromUser, toUser, amount, paymentMethod, this);
    }
}

public class DigitalWallet {
    public static void main(String[] args) {

    }
}
