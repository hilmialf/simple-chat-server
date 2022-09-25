package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private int port;
    private final MultiQueue<String> multiQueue =  new MultiQueue<>();
    public Server(){
        this(8000);
    }
    public Server(int port){
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public static void main(String[] args) {
        Server server = args.length == 0 ? new Server() : new Server(Integer.parseInt(args[0]));
        System.out.println("Running server in port " + server.getPort());
        server.run();
    }

    public void run(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            // Main loop thread to accept incoming connection only.
            // The handling is done in different threads.
            Map<String,ClientHandler> clientMap = new HashMap<>();
            Socket clientSocket;
            ClientHandler clientHandler;
            while(true){
                clientSocket = serverSocket.accept();
                clientHandler = new ClientHandler(clientSocket, multiQueue);
                clientMap.put(clientHandler.getNickname(), clientHandler);
                multiQueue.put("client connected " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
                clientHandler.run();
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
