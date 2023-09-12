package task3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPClient {

    private final boolean shutdown;
    private final Integer timeout;
    private final Integer limit;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        // Create a socket and open a connection to the server
        // with the given hostname and port
        Socket clientSocket = new Socket(hostname, port);

        // Write the given input to the server
        clientSocket.getOutputStream().write(toServerBytes);

        // If shutdown is true then we should close the outgoing connection
        // to the server after sending all the bytes
        if (shutdown) {
            clientSocket.shutdownOutput();
        }

        // ByteStream for reading data from the server
        ByteArrayOutputStream serverOutputStream = new ByteArrayOutputStream();

        readData(clientSocket, serverOutputStream);

        // Close the stream and the connection to the server
        serverOutputStream.close();
        clientSocket.close();

        return serverOutputStream.toByteArray();
    }

    private void readData(Socket clientSocket, ByteArrayOutputStream serverOutput) throws IOException {
        // The read operation on the sockets OutputStream will throw an exception if it has to
        // wait longer than "timeout" ms to receive any bytes from the server (timeout = 0 means
        // that the timeout is set to infinity)
        clientSocket.setSoTimeout(timeout == null ? 0 : timeout);

        // Read the server output one byte at the time
        InputStream clientInputStream = clientSocket.getInputStream();
        int readBytes;
        // Set the buffer size to 1024 if limit is null, else set it to limit
        byte[] buffer = new byte[limit == null ? 1024 : limit];
        try {
            while ((readBytes = clientInputStream.read(buffer)) != -1) {
                serverOutput.write(buffer, 0, readBytes);
                // If we have read fewer bytes than the buffer-size we have read all data from the client
                // or if limit != null we always read limit bytes the first time (buffer.length = limit)
                if (readBytes < buffer.length || limit != null) break;
            }
        } catch (SocketTimeoutException e) {
            // Do nothing, the exception will make us exit the loop
        }
        clientInputStream.close();
    }
}
