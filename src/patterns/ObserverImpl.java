package patterns;

import java.util.ArrayList;

interface Observer {
    void update(int value);
}
interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

class ConcreteSubject implements Subject {
    private ArrayList<Observer> observers = new ArrayList<Observer>();
    private int state;

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }
    public void setState(int state) {
        this.state = state;
        notifyObservers();
    }
}

class ConcreteObserver implements Observer {
    private int state;

    @Override
    public void update(int value) {
        this.state = value;
        System.out.println("Observer state changed to " + this.state);
    }
}

public class ObserverImpl {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        Observer observer1 = new ConcreteObserver();
        Observer observer2 = new ConcreteObserver();
        subject.registerObserver(observer1);
        subject.registerObserver(observer2);
        subject.setState(1);
        subject.setState(2);
    }
}
