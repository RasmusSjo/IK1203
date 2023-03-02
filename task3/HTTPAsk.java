import java.net.*;
import java.io.*;

public class HTTPAsk {

    private static final String HTTP_OK = "HTTP/1.1 200 OK\r\n\r\n";
    private static final String HTTP_BAD_REQUEST = "HTTP/1.1 400 Bad Request\r\n\r\n";
    private static final String HTTP_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n\r\n";
    private static final String HTTP_NOT_SUPPORTED = "HTTP/1.1 501 Not Implemented\r\n\r\n";

    private static int serverPort;

    public static void main(String[] args) throws IOException {
        // Check if the file is properly executed
        try {
            serverPort = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Usage: HTTPAsk port");
            System.exit(1);
        }

        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Server socket created on port " + serverPort + "\n");

        while (true) {
            // Wait for a client to connect to the server. The accept
            // method blocks until a connection is made.
            System.out.println("Waiting for a client to connect...");
            Socket connectionSocket = serverSocket.accept();
            System.out.println("A client has connected!\n");

            // Client output
            InputStream clientOutput = connectionSocket.getInputStream();
            ByteArrayOutputStream request = new ByteArrayOutputStream();

            // Server output
            OutputStream serverOutput = connectionSocket.getOutputStream();
            byte[] serverResponse;

            System.out.println("Reading data from the client...");
            int readBytes;
            byte[] buffer = new byte[1024];
            // Read all the data sent by the client
            while ((readBytes = clientOutput.read(buffer)) != -1) {
                request.write(buffer, 0, readBytes);
                if (readBytes < buffer.length) break;
            }
            System.out.println("Done reading data from client!\n");

            // Split the client data at the first empty line, i.e. the end of the header
            String requestHeader = request.toString().split("\r\n\r\n")[0];

            System.out.println("------Request Header------");
            System.out.println(requestHeader);
            System.out.println("----End Request Header----\n");

            String[] requestLineParts = requestHeader.split("\r")[0].split(" ");
            String reqMethod = requestLineParts[0];
            String reqPath = requestLineParts[1];
            String reqVersion = requestLineParts[2];

            // The server only need to recognize GET request, so we can ignore everything else
            if (!reqMethod.equals("GET")) {
                serverOutput.write(HTTP_NOT_SUPPORTED.getBytes());
                connectionSocket.close();
                return;
            }

            // Split the path at any of the characters ?, & and =
            String[] params = reqPath.split("[?&=]");

            System.out.println("The request path contains the following parameters:");
            for (int num = 1; num < params.length; num += 2) {
                System.out.println(params[num] + "=" + params[num + 1]);
            }
            System.out.println(); // Empty line

            String hostname = null;
            int port = -1;
            byte[] data = new byte[0];
            boolean shutdown = false;
            Integer timeout = null;
            Integer limit = null;

            // If params[0] isn't "/ask", i.e. the only resource our server has we have gotten a
            // request for a resource that we do not have
            String fullResponse = HTTP_NOT_FOUND;

            if (params[0].equals("/ask")) {
                for (int i = 1; i < params.length; i++) {
                    switch (params[i++]) {
                        case "hostname" -> hostname = params[i];
                        case "port" -> port = Integer.parseInt(params[i]);
                        case "string" -> data = (params[i] + "\r\n").getBytes();
                        case "shutdown" -> shutdown = params[i].equalsIgnoreCase("true");
                        case "limit" -> limit = Integer.parseInt(params[i]);
                        case "timeout" -> timeout = Integer.parseInt(params[i]);
                    }
                }

                TCPClient tcpClient = new TCPClient(shutdown, timeout, limit);
                System.out.print("TCPClient created with arguments: ");
                System.out.println(shutdown + " " + timeout + " " + limit);

                if (hostname != null && port != -1 && reqVersion.equals("HTTP/1.1")) {
                    try {
                        serverResponse = tcpClient.askServer(hostname, port, data);
                        System.out.print("Arguments to TCPClient.askServer: ");
                        System.out.println(hostname + " " + port + " " + new String(data) + "\n");

                        System.out.println("----Response from server----");
                        System.out.println(new String(serverResponse));
                        System.out.println("-------End response---------\n");
                        fullResponse = HTTP_OK + new String(serverResponse);
                    } catch (IOException e) {
                        fullResponse = HTTP_OK + "There was an error trying to connect to the server " +
                                "given the information you provided, try again.";
                    }
                }
                else fullResponse = HTTP_BAD_REQUEST;
            }

            System.out.println("-----Response to client-----");
            System.out.println(fullResponse);
            System.out.println("--------End response--------\n");

            // Write the response to the client
            serverOutput.write(fullResponse.getBytes());

            // Close the connection socket.
            connectionSocket.close();
            System.out.println("Connection to client is closed!\n");
        }
    }
}
