package misc;

import static java.lang.Thread.sleep;

class RateLimit{
    private long startTime;
    private int requestsAllowed;
    private volatile int totalRequests;
    public RateLimit(int requestAllowed){
        this.requestsAllowed = requestAllowed;
        startTime = System.currentTimeMillis();
        totalRequests = 0;
    }
    public void request(){
        synchronized(this){
            long currentTime = System.currentTimeMillis();
            if(currentTime - startTime > 1000){
                startTime = currentTime;
                totalRequests = 0;
            }
            if(totalRequests<requestsAllowed){
                totalRequests++;
                System.out.println("Request accepted");
            }
            else{
                System.out.println("Request rejected");
            }
        }
    }
}

public class RateLimiter {
    public static void main(String[] args) throws InterruptedException {
       RateLimit limiter = new RateLimit(2);
       limiter.request();
       sleep(500);
       limiter.request();
        sleep(100);
        limiter.request();
        sleep(100);
        limiter.request();
       sleep(1000);
       limiter.request();
    }
}
