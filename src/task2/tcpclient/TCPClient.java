package task2.tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient {

    private boolean shutdown;
    private Integer timeout;
    private Integer limit;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port) throws IOException {
        // Create a socket and open a connection to the server
        // with the given hostname and port
        Socket clientSocket = new Socket(hostname, port);

        ByteArrayOutputStream serverOutputStream = new ByteArrayOutputStream();

        readData(clientSocket, serverOutputStream);

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
        // Counter to keep track of the number of bytes read from the server,
        // this is only used when the limit is != null
        int count = 0;
        int answerLimit = limit == null ? 1 : limit;

        // The read operation on the sockets OutputStream will throw an exception if it has to
        // wait longer than "timeout" ms to receive any bytes from the server (timeout = 0 means
        // that the timeout is set to infinity)
        clientSocket.setSoTimeout(timeout == null ? 0 : timeout);

        // Read the server output one byte at the time
        InputStream clientInputStream = clientSocket.getInputStream();
        int temp = 0;
        while (temp != -1 && count < answerLimit) {
            try {
                temp = clientInputStream.read();
                if (temp != -1) {
                    serverOutput.write(temp);
                }
            } catch (SocketTimeoutException e) {
                break;
            }

            if (limit != null) {
                count++;
            }
        }
        clientInputStream.close();
    }
}
