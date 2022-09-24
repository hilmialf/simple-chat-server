package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 8000)){
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String messageFromServer;
            while((messageFromServer = in.readLine()) != null){
                System.out.println(messageFromServer);
                if("quit".equals(messageFromServer)){
                    System.out.println("Terminating program...");
                    break;
                }
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
