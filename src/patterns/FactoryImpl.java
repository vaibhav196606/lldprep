package patterns;

interface Character{
   public String getName();
   public int getAge();
   public void attack();
}

class Dragon implements Character{
    @Override
    public int getAge() {
        return 100;
    }
    public String getName() {
        return "Dragon";
    }

    @Override
    public void attack() {
        System.out.println("Roarrrrrr");
    }
}

class Marvel implements Character{
    @Override
    public int getAge() {
        return 60;
    }
    public String getName() {
        return "Marvel";
    }

    @Override
    public void attack() {
        System.out.println("Woshhhh");
    }
}

class CharacterFactory {
    public static Character getCharacter(String character) {
        switch (character) {
            case "Dragon":
                return new Dragon();
            case "Marvel":
                return new Marvel();
        }
        return null;
    }
}


public class FactoryImpl {
    public static void main(String[] args) {
        Character character = CharacterFactory.getCharacter("Dragon");
        System.out.println(character.getAge());
    }
}
