package lldexamples.splitwise;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


interface Subject{
    public void addUser(User user);
    public void removeUser(User user);
    public void notifyObservers();
}

interface Observer{
    public void update();
}

class User implements Observer{

    private int id;
    private String name;
    private ArrayList<Group> groups;
    private HashMap<User, Integer> individualBalance;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.groups = new ArrayList<Group>();
        this.individualBalance = new HashMap<User, Integer>();
    }
    @Override
    public void update() {
        System.out.println("Balance updated");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }

    public HashMap<User, Integer> getIndividualBalance() {
        return individualBalance;
    }

    public void setIndividualBalance(HashMap<User, Integer> individualBalance) {
        this.individualBalance = individualBalance;
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }
}

class Group implements Subject{
    private int id;
    private String name;
    private ArrayList<User> users;
    public Group(int id, String name, ArrayList<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
        for(User u : users) {
            u.addGroup(this);
        }
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ArrayList<User> getUsers() {
        return users;
    }
    public void addUser(User user) {
        users.add(user);
        user.addGroup(this);
    }
    public void removeUser(User user) {
        users.remove(user);
        user.removeGroup(this);
    }

    @Override
    public void notifyObservers() {
        for(User u : users) {
            u.update();
        }
    }

    public void getBalance() {
        for(User from : users) {
            for(User to : users) {
                if(from==to)continue;
                if(from.getIndividualBalance().containsKey(to) && from.getIndividualBalance().get(to)>0) {
                    System.out.println(from.getName()+" will get "+from.getIndividualBalance().get(to) + " from "+to.getName());
                }
                if(from.getIndividualBalance().containsKey(to) && from.getIndividualBalance().get(to)<0) {
                    System.out.println(from.getName()+" have to give "+ Math.abs(from.getIndividualBalance().get(to)) + " to "+to.getName());
                }
            }
        }
    }

}

abstract class SplitStrategy{
    public abstract HashMap<User,Integer> calculateBalance(User whoPaid, ArrayList<User> users, int amount);
}

class EqualSplitStrategy extends SplitStrategy {
    @Override
    public HashMap<User, Integer> calculateBalance(User whoPaid, ArrayList<User> users, int amount) {
        HashMap<User,Integer> balance = new HashMap<User,Integer>();
        for(User user : users) {
            if(user == whoPaid)continue;
            balance.put(user,amount/users.size());
        }
        return balance;
    }
}

class PercentSplitStrategy extends SplitStrategy {
    private HashMap<User,Integer> percentSplit = new HashMap<User,Integer>();
    public PercentSplitStrategy(HashMap<User,Integer> percentSplit) {
        this.percentSplit = percentSplit;
    }

    @Override
    public HashMap<User, Integer> calculateBalance(User whoPaid, ArrayList<User> users, int amount) {
        HashMap<User,Integer> balance = new HashMap<User,Integer>();
        for(User user : users) {
            if(user == whoPaid)continue;
            balance.put(user, (int) (amount*percentSplit.get(user)*0.01));
        }
        return balance;
    }
}

class SplitContext{
    private SplitStrategy strategy;
    public SplitContext(SplitStrategy strategy) {
        this.strategy = strategy;
    }

    public HashMap<User, Integer> split(User whoPaid, ArrayList<User> users, int amount) {
        return strategy.calculateBalance(whoPaid, users, amount);
    }
}

class Transaction{
    public static void accountNewTransaction(User whoPaid, HashMap<User,Integer> users) {
        for(Map.Entry<User, Integer> entry : users.entrySet()) {
            if(entry.getKey() == whoPaid)continue;
            if(whoPaid.getIndividualBalance().containsKey(entry.getKey())) {
                int updatedAmount = whoPaid.getIndividualBalance().get(entry.getKey()) + entry.getValue();
                whoPaid.getIndividualBalance().remove(entry.getKey());
                whoPaid.getIndividualBalance().put(entry.getKey(), updatedAmount);
            }
            else {
                whoPaid.getIndividualBalance().put(entry.getKey(), entry.getValue());
            }
        }

        for(Map.Entry<User, Integer> entry : users.entrySet()) {
            if(entry.getKey() == whoPaid)continue;
            if(entry.getKey().getIndividualBalance().containsKey(whoPaid)) {
                int updatedAmount = entry.getKey().getIndividualBalance().get(whoPaid) - entry.getValue();
                entry.getKey().getIndividualBalance().remove(whoPaid);
                entry.getKey().getIndividualBalance().put(whoPaid, updatedAmount);
            }
            else {
                entry.getKey().getIndividualBalance().put(whoPaid, -1*entry.getValue());
            }
        }
    }
    public static void settle(User whoPaid, User whoReceived, int amount) {
        int updatedAmount = whoPaid.getIndividualBalance().get(whoReceived) + amount;
        if(whoPaid.getIndividualBalance().containsKey(whoReceived)) {whoPaid.getIndividualBalance().remove(whoReceived);}
        whoPaid.getIndividualBalance().put(whoReceived, updatedAmount);

        updatedAmount = whoReceived.getIndividualBalance().get(whoPaid) - amount;
        if(whoReceived.getIndividualBalance().containsKey(whoPaid)) {whoReceived.getIndividualBalance().remove(whoPaid);}
        whoReceived.getIndividualBalance().put(whoPaid, updatedAmount);
    }
}

class App{
    private static volatile App instance;
    private ArrayList<Group> groups;

    private App(){
        groups = new ArrayList<Group>();
    }

    public static App getInstance(){
        if(instance==null){
            synchronized(App.class){
                if(instance==null){
                    instance = new App();
                }
            }
        }
        return instance;
    }
    void split(User whoPaid, Group group, int amount, SplitStrategy strategy) {
            HashMap<User, Integer> splitMap = new SplitContext(strategy).split(whoPaid,group.getUsers(),amount);
            group.notifyObservers();
            Transaction.accountNewTransaction(whoPaid,splitMap);
    }

    void settle(User whoPaid, User whoReceived, int amount) {
        Transaction.settle(whoPaid, whoReceived, amount);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }
    public void removeGroup(Group group) {
        groups.remove(group);
    }
    public Group getGroup(int id) {
        for(Group g : groups) {
            if(g.getId()==id)return g;
        }
        return null;
    }
}

public class Splitwise {
    public static void main(String[] args) {
        App app = App.getInstance();
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(1, "Vaibhav"));
        users.add(new User(2, "Bobby"));
        users.add(new User(3, "Ritesh"));
        app.addGroup(new Group(1,"Trip", users));

        app.split(users.get(0), app.getGroup(1), 90, new EqualSplitStrategy());
        app.split(users.get(1), app.getGroup(1), 120, new EqualSplitStrategy());
        app.getGroup(1).getBalance();
        app.settle(users.get(2), users.get(1), 40);
        System.out.println("After settling up");
        app.getGroup(1).getBalance();
    }
}

// UML available in the same folder
