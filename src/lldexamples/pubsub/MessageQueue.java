package lldexamples.pubsub;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

enum Status{
    INITIATED, DELIVERING, DELIVERED, FAILED
}

class Message{
    String title;
    String body;
    Status status;
    public Message(String title, String body){
        this.title = title;
        this.body = body;
        this.status = Status.INITIATED;
    }
    public void setStatus(Status status){
        this.status = status;
    }
}

class Subscriber{
    String name;
    ArrayList<Topic> topics;
    ArrayList<Message> messages;
    public Subscriber(String name){
        this.name = name;
        topics = new ArrayList<Topic>();
        messages = new ArrayList<Message>();
    }

    public void addTopic(Topic topic){
        topics.add(topic);
        topic.addSubscriber(this);
    }
    public void removeTopic(Topic topic){
        topics.remove(topic);
        topic.removeSubscriber(this);
    }
    public void notifyMessage(Message message){
        messages.add(message);
    }
}

class Publisher{
    String name;
    ArrayList<Topic> topics;
    public Publisher(String name){
        this.name = name;
        topics = new ArrayList<Topic>();
    }
    public void addTopic(Topic topic){
        topics.add(topic);
    }
    public void removeTopic(Topic topic){
        topics.remove(topic);
    }

    public void publish(Message message){
        for(Topic topic : topics){
            boolean isAdded = topic.addMessage(message);
            if(isAdded){
                System.out.println("Message has been added to the topic: " + topic.name);
            }
            else{
                System.out.println("Message has not been added to the topic: " + topic.name+ " as the queue is overloaded");
            }
        }
    }
}

class Topic{
    String name;
    ArrayList<Subscriber> subscribers;
    BlockingQueue<Message> messages;
    int capacity;
    int pollingIntervalInSeconds;
    public Topic(String name, int capacity, int pollingIntervalInSeconds){
        this.name = name;
        messages = new ArrayBlockingQueue<>(capacity);
        subscribers = new ArrayList<Subscriber>();
        this.capacity = capacity;
        this.pollingIntervalInSeconds = pollingIntervalInSeconds;
    }
    public void addSubscriber(Subscriber subscriber){
        subscribers.add(subscriber);
    }
    public void removeSubscriber(Subscriber subscriber){
        subscribers.remove(subscriber);
    }
    public boolean addMessage(Message message){
        return messages.offer(message);
    }
    public void startMessageDelivery(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        synchronized(this) {
                            wait(pollingIntervalInSeconds * 1000L);
                        }
                        Message message = messages.poll();
                        if(message != null) {
                            deliverMessage(message);
                        }
                    } catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        thread.start();
    }
    public void deliverMessage(Message message) {
            message.setStatus(Status.DELIVERING);
            for(Subscriber subscriber : subscribers){
                subscriber.notifyMessage(message);
            }
            message.setStatus(Status.DELIVERED);
    }
}

 class MessagingQueue {
    HashMap<String, Subscriber> subscribers;
    HashMap<String, Publisher> publishers;
    HashMap<String, Topic> topics;


    private MessagingQueue(){
        subscribers = new HashMap<String, Subscriber>();
        publishers = new HashMap<String, Publisher>();
        topics = new HashMap<String, Topic>();
    }

    private static MessagingQueue instance;
    public static MessagingQueue getInstance(){
        if(instance == null){
            synchronized(MessagingQueue.class) {
                if (instance == null) {
                    instance = new MessagingQueue();
                }
            }
        }
        return instance;
    }

    public void addSubscriber(String subscriberName){
        subscribers.put(subscriberName, new Subscriber(subscriberName));
    }

    public void addPublisher(String publisherName){
        publishers.put(publisherName, new Publisher(publisherName));
    }
    public void addTopic(String topicName, int capacity, int pollingIntervalInSeconds){
        topics.put(topicName, new Topic(topicName, capacity, pollingIntervalInSeconds));
    }

}


public class MessageQueue {
    public static void main(String[] args) {

    }
}
