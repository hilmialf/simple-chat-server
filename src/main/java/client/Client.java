package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) {
        try( Socket socket = new Socket("localhost", 8000);){
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            Thread toServerThread = new Thread(()->{
                try(
                        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ){
                    String messageToSend;
                    while((messageToSend = stdIn.readLine()) != null){
                        out.println(messageToSend);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

            });

            Thread fromServerThread = new Thread(() -> {
                try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
                    String messageFromServer;
                    while((messageFromServer = in.readLine()) != null){
                        System.out.println(messageFromServer);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            });

            toServerThread.start();
            fromServerThread.start();

            // Reason why join is needed
            // https://stackoverflow.com/questions/36635595/socket-closed-as-soon-as-passed-into-new-thread
            toServerThread.join();
            fromServerThread.join();
        }
        catch (IOException|InterruptedException e){
            e.printStackTrace();
        }
    }
}
