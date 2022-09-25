package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import utils.Utils;

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
            try(OutputStream out = socket.getOutputStream()){
                String toBeSend;
                while(true){
                    toBeSend = clientMessages.take();
                    System.out.println(this.nickname + " " + toBeSend);
                    Utils.sendMessage(out, toBeSend.getBytes(StandardCharsets.UTF_8));
                }
            }catch (IOException e){
                this.disconnect();
                e.printStackTrace();
            }
        });

        Thread fromClientThread = new Thread(()->{
            try(InputStream in = socket.getInputStream()){
                ByteArrayOutputStream out;
                while(true){
                    out = Utils.readFromInputStream(in);
                    this.multiQueue.put(out.toString());
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
