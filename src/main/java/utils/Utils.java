package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Utils {
    /**
     * Assumes the first 4 bytes is the length of the data
     * @param in
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream readFromInputStream(InputStream in) throws IOException{
        // get length
        int len;
        byte[] lenBuf = new byte[4];
        in.read(lenBuf, 0, lenBuf.length);
        len = ByteBuffer.wrap(lenBuf).getInt();

        // read data
        byte[] buf = new byte[1024];
        int nRead;
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        while(len > 0){
            nRead = in.read(buf, 0, buf.length);
            len -= nRead;
            out.write(buf, 0, nRead);
        }
        out.flush();
        return out;
    }

    /**
     * Sends data that follows protocol
     * @param out
     * @param data
     * @throws IOException
     */
    public static void sendMessage(OutputStream out, byte[] data) throws IOException{
        out.write(ByteBuffer.allocate(4).putInt(data.length).array());
        out.write(data);
        out.flush();
    }
}
