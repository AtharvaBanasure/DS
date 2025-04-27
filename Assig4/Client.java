// Java program imitating a client process
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class Client {

    // function to send current time to server repeatedly
    public static void startSendingTime(Socket slaveClient) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                OutputStream outputStream = slaveClient.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                while (true) {
                    String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                    writer.println(currentTime);
                    System.out.println("Recent time sent successfully");
                    Thread.sleep(5000); // 5 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // function to receive synchronized time from server
    public static void startReceivingTime(Socket slaveClient) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                InputStream inputStream = slaveClient.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Synchronized time at client: " + line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // function to initiate the client connection
    public static void initiateSlaveClient(int port) {
        try {
            Socket slaveClient = new Socket("127.0.0.1", port);
            System.out.println("Connected to the server\n");
            System.out.println("Starting to send time to server...");
            startSendingTime(slaveClient);

            System.out.println("Starting to receive synchronized time from server...");
            startReceivingTime(slaveClient);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Driver function
    public static void main(String[] args) {
        initiateSlaveClient(8080);
    }
}

