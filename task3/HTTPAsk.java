import java.net.*;
import java.io.*;

public class HTTPAsk {
    public static void main( String[] args) throws IOException {
        // Your code here
        int portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Server socket created on port " + portNumber);

        while (true) {
            // Wait for a client to connect to the server. The accept
            // method blocks until a connection is made.
            Socket connectionSocket = serverSocket.accept();

            InputStream clientRequest = connectionSocket.getInputStream();
            ByteArrayOutputStream clientRequestData = new ByteArrayOutputStream();

            // Receive data on the connection socket, data that is sent
            // by the client.
            int temp = clientRequest.read();
            while (temp != '\n') {
                clientRequestData.write(temp);

                temp = clientRequest.read();
            }

            OutputStream clientResponse = connectionSocket.getOutputStream();
            String responseData = "Hello World! This is a test!";
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: WebServer\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + responseData.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    responseData;

            // Send a response to the client.
            clientResponse.write(response.getBytes());


            //TCPClient tcpClient = new TCPClient(false, null, null);


            // Close the connection socket.
            connectionSocket.close();
        }
    }
}
