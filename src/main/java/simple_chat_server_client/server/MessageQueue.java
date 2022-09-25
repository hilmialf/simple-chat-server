package simple_chat_server_client.server;

public interface MessageQueue<T>{
    /**
     * Place msg at the end of queue
     * @param msg
     */
    public void put(T msg);

    /**
     * Block until queue has message, then return the head of queue
     * @return
     */
    public T take();
}
