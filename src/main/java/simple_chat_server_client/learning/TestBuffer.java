package simple_chat_server_client.learning;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestBuffer {
    public static void main(String[] args) throws IOException {
        String testString = "very very long message very very long message very very long message very very long message very very long message very very long message ";
        InputStream in = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));


        // dynamically resize when necessary
        ByteArrayOutputStream out = new ByteArrayOutputStream(2);;


        int nRead;
        String read;
        byte[] buf = new byte[4];
        while((nRead = in.read(buf, 0, buf.length)) != -1){
            read = new String(buf, StandardCharsets.UTF_8);
            System.out.println(nRead);
            System.out.println(read);

            out.write(buf, 0, nRead);
        }

        System.out.println(out.toString());
        in.close();
        out.close();

    }
}
