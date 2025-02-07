package patterns;

import java.util.Observable;
import java.util.Observer;

class WeatherData extends Observable {
    private int temperature;
    private int humidity;

    public void setMeasurements(int temperature, int humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
        measurementsChanged();
    }
    public void measurementsChanged() {
        setChanged();
        notifyObservers();
    }
    public int getTemperature() {
        return temperature;
    }
    public int getHumidity() {
        return humidity;
    }
}

class WeatherObserver implements Observer {
    private Observable observable;
    private int temperature;
    private int humidity;
    public WeatherObserver(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof WeatherData) {
            WeatherData weatherData = (WeatherData) o;
            this.temperature = weatherData.getTemperature();
            this.humidity = weatherData.getHumidity();
            display();
        }
    }
    private void display() {
        System.out.println("Temperature: " + temperature + " Humidity: " + humidity);
    }

}

public class ObserverInBuilt {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        WeatherObserver weatherObserver1 = new WeatherObserver(weatherData);
        WeatherObserver weatherObserver2 = new WeatherObserver(weatherData);
        weatherData.setMeasurements(10,20);
    }
}
