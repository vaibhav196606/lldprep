package patterns;

interface Button{
    void click();
}

interface Checkbox{
    void click();
}

class DarkButton implements Button{
    @Override
    public void click() {
        System.out.println("Dark button clicked");
    }
}

class LightButton implements Button{
    @Override
    public void click() {
        System.out.println("Light button clicked");
    }
}

class DarkCheckbox implements Checkbox{
    @Override
    public void click() {
        System.out.println("Dark checkbox clicked");
    }
}

class LightCheckbox implements Checkbox{
    @Override
    public void click() {
        System.out.println("Light checkbox clicked");
    }
}

interface UI{
    public Button getButton();
    public Checkbox getCheckbox();
}

class DarkUI implements UI{
    public Button getButton() {
        return new DarkButton();
    }
    public Checkbox getCheckbox() {
        return new DarkCheckbox();
    }
}

class LightUI implements UI{
    public Button getButton() {
        return new LightButton();
    }
    public Checkbox getCheckbox() {
        return new DarkCheckbox();
    }
}

class Application{
    public Button button;
    public Checkbox checkbox;
    public UI ui;
    Application(UI ui){
        this.ui = ui;
        button = ui.getButton();
        checkbox = ui.getCheckbox();
    }
}

public class AbstractFactoryImpl {
    public static void main(String[] args) {
        Application app = new Application(new LightUI());
        app.button.click();
    }
}
