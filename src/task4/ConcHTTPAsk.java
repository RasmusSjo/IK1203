package task4;

import java.net.*;
import java.io.*;

public class ConcHTTPAsk {

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

            System.out.println("Creating a MyRunnable object...");
            MyRunnable runnable = new MyRunnable(connectionSocket);
            System.out.println("Creating a Thread object using the MyRunnable object...");
            Thread thread = new Thread(runnable);
            System.out.println("Calling the threads start method...");
            thread.start();
        }
    }
}
