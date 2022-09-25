package simple_chat_server_client.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import simple_chat_server_client.utils.Utils;

public class Client {
    public static void main(String[] args) {
        try( Socket socket = new Socket("localhost", 8000);){
            System.out.println("Connected to " + socket.getInetAddress() + ":" + socket.getPort());
            Thread toServerThread = new Thread(()->{
                try(
                        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                        OutputStream out = socket.getOutputStream();
                ){
                    String messageToSend;
                    while((messageToSend = stdIn.readLine()) != null){
                        Utils.sendMessage(out, messageToSend.getBytes(StandardCharsets.UTF_8));
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

            });

            Thread fromServerThread = new Thread(() -> {
                try(InputStream in = socket.getInputStream()){
                    ByteArrayOutputStream out;
                    while(true){
                        out = Utils.readFromInputStream(in);
                        System.out.println(out.toString());
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
