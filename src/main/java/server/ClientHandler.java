package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClientHandler{
    static final Random random = new Random();
    private final Socket socket;
    private final MultiQueue<String> multiQueue;
    private String nickname;
    private final SafeMessageQueue<String> clientMessages = new SafeMessageQueue<>();
    public ClientHandler(Socket socket, MultiQueue<String> multiQueue){
        this.socket = socket;
        this.multiQueue = multiQueue;
        this.multiQueue.register(clientMessages);
        this.nickname = String.format("Anonymous_%05d", random.nextInt(99999));
        System.out.println("Instantiating handler for client " + this.nickname + " at " + this.socket.getInetAddress() + ":" + this.socket.getPort());
    }
    public void run(){
        Thread toClientThread = new Thread(()->{
            try(PrintWriter out = new PrintWriter(socket.getOutputStream(), true);){
                String toBeSend;
                while(true){
                    toBeSend = clientMessages.take();
                    System.out.println(this.nickname + " " + toBeSend);
                    out.println(toBeSend);
                }
            }catch (IOException e){
                this.disconnect();
                e.printStackTrace();
            }
        });

        Thread fromClientThread = new Thread(()->{
            try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                String msg;
                while((msg = in.readLine()) != null){
                    this.multiQueue.put(msg);
                }
            }catch (IOException e){
                this.disconnect();
                e.printStackTrace();
            }

        });

        // no need to join, the main server loop is still ongoing
        toClientThread.start();
        fromClientThread.start();
    }

    public String getNickname() {
        return nickname;
    }

    public Socket getSocket() {
        return socket;
    }

    public SafeMessageQueue<String> getClientMessages(){
        return clientMessages;
    }

    public void disconnect(){
        multiQueue.deregister(this.clientMessages);
        multiQueue.put("Client " + this.nickname + " is disconnected.");
    }
}
