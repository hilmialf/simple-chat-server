package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static int port;
    public static void main(String[] args) {
        port = args.length == 0 ? 8000 : Integer.parseInt(args[0]);
        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ){
            String message;
            while((message = in.readLine()) != null){
                System.out.println(message);
                out.println(message);
                if("quit".equals(message)){
                    System.out.println("Disconnecting client..." + clientSocket.getPort());
                    System.out.println("Terminating program...");
                    break;
                }
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}