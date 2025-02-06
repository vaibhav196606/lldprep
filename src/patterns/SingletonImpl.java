package patterns;

class Singleton{
    private static volatile Singleton instance;
    private Singleton(){
    }
    public static Singleton getInstance(){
        if(instance == null ){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

public class SingletonImpl {
    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
    }
}


