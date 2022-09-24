package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 8000)){
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput, messageFromServer;
            while((userInput = stdIn.readLine()) != null){
                out.println(userInput);
                messageFromServer = in.readLine();
                System.out.println(messageFromServer + " is acknowledged by the server");
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
