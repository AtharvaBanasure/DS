import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TokenRingSite {

    private static boolean hasToken = false;
    private static int myPort;
    private static String nextIP;
    private static int nextPort;

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage: java TokenRingSite <myPort> <nextIP> <nextPort>");
            return;
        }

        myPort = Integer.parseInt(args[0]);
        nextIP = args[1];
        nextPort = Integer.parseInt(args[2]);

        Scanner sc = new Scanner(System.in);

        System.out.print("Do you have the initial token? (yes/no): ");
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("yes")) {
            hasToken = true;
        }

        // Start a thread to listen for incoming tokens
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(myPort);
                while (true) {
                    Socket socket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg = reader.readLine();
                    if (msg.equals("TOKEN")) {
                        hasToken = true;
                        System.out.println("\n[+] Token received!");
                    }
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Main thread handles token possession
        while (true) {
            if (hasToken) {
                // Critical Section
                System.out.println("\n[CRITICAL SECTION] You have entered Critical Section.");
                Thread.sleep(3000);  // simulate doing some work
                System.out.println("[CRITICAL SECTION] Exiting Critical Section.");

                // Send the token to next site
                sendToken();
                hasToken = false;
            }
            Thread.sleep(1000); // wait before checking again
        }
    }

    private static void sendToken() {
        try {
            Socket socket = new Socket(nextIP, nextPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("TOKEN");
            socket.close();
            System.out.println("[+] Token sent to next site (" + nextIP + ":" + nextPort + ")");
        } catch (IOException e) {
            System.out.println("[-] Failed to send token. Retrying...");
            e.printStackTrace();
        }
    }
}

