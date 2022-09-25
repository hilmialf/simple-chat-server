package simple_chat_server_client.server;

import java.util.ArrayDeque;
import java.util.Deque;

/*
* Naive implementation of safe message queue
 */
public class SafeMessageQueue<T> implements MessageQueue<T>{
    private final Deque<T> messageQ = new ArrayDeque<>();
    SafeMessageQueue(){}

    @Override
    public synchronized void put(T msg){
        messageQ.add(msg);
        this.notifyAll();
    }

    @Override
    public synchronized T take(){
        while(messageQ.isEmpty()){
            try{
                this.wait();
            } catch (InterruptedException e){
                //noop
            }
        }
        return messageQ.remove();
    }

}
