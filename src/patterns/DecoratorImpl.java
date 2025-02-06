package patterns;

interface Coffee{
    public String getDescription();
    public double getPrice();
}

class PlainCoffee implements Coffee{
    public String getDescription() {
        return "Coffee";
    }
    public double getPrice() {
        return 1.5;
    }
}

abstract class CoffeeDecorator implements Coffee{
    protected Coffee coffeeDecorator;
    public CoffeeDecorator(Coffee coffeeDecorator) {
        this.coffeeDecorator = coffeeDecorator;
    }
    public String getDescription() {
        return coffeeDecorator.getDescription();
    }
    public double getPrice() {
        return coffeeDecorator.getPrice();
    }

}

class MilkCoffee extends CoffeeDecorator{
    MilkCoffee(Coffee coffeeDecorator) {
        super(coffeeDecorator);
    }

    public String getDescription() {
        return coffeeDecorator.getDescription()+", Milk";
    }
    public double getPrice() {
        return coffeeDecorator.getPrice() + 0.5;
    }
}

class AlmondCoffee extends CoffeeDecorator{
    AlmondCoffee(Coffee coffeeDecorator) {
        super(coffeeDecorator);
    }

    public String getDescription() {
        return coffeeDecorator.getDescription()+", Almond";
    }
    public double getPrice() {
        return coffeeDecorator.getPrice() + 1.0;
    }
}

public class DecoratorImpl {
    public static void main(String[] args) {
        Coffee coffee = new AlmondCoffee(new MilkCoffee(new PlainCoffee()));
        System.out.println("Coffee : " + coffee.getDescription());
        System.out.println("Price : $ " + coffee.getPrice());
    }
}
