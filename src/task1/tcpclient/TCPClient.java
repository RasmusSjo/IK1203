package task1.tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port) throws IOException {
        // Create a socket and open a connection to the server
        // with the given hostname and port
        Socket clientSocket = new Socket(hostname, port);

        ByteArrayOutputStream serverOutputStream = new ByteArrayOutputStream();
        // Read the server output one byte at the time
        int temp = clientSocket.getInputStream().read();
        while (temp != -1) {
            serverOutputStream.write(temp);
            temp = clientSocket.getInputStream().read();
        }

        // Close the stream and the connection to the server
        serverOutputStream.close();
        clientSocket.close();

        return serverOutputStream.toByteArray();
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        // Create a socket and open a connection to the server
        // with the given hostname and port
        Socket clientSocket = new Socket(hostname, port);

        // Write the given input to the server
        clientSocket.getOutputStream().write(toServerBytes);

        ByteArrayOutputStream serverOutputStream = new ByteArrayOutputStream();
        // Read the server output one byte at the time
        int temp = clientSocket.getInputStream().read();
        while (temp != -1) {
            serverOutputStream.write(temp);
            temp = clientSocket.getInputStream().read();
        }

        // Close the stream and the connection to the server
        serverOutputStream.close();
        clientSocket.close();

        return serverOutputStream.toByteArray();
    }
}
