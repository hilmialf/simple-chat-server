package server;

import java.util.HashSet;
import java.util.Set;

public class MultiQueue<T>{
    private Set<MessageQueue<T>> outputs = new HashSet<>(); //TODO

    public synchronized void register(MessageQueue<T> q){
        outputs.add(q);
    }

    public synchronized void deregister(MessageQueue<T> q){
        outputs.remove(q);
    }

    public synchronized void put(T msg){
        for(MessageQueue<T> q : outputs){
            q.put(msg);
            System.out.println("Successfully put (" + msg +  ") to queue " + q );
        }
    }
}
