// Java program imitating a clock server
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class Server {

    // Data structure to store client data
    static class ClientInfo {
        Socket socket;
        Date clockTime;
        long timeDifferenceMillis;

        ClientInfo(Socket socket, Date clockTime, long timeDifferenceMillis) {
            this.socket = socket;
            this.clockTime = clockTime;
            this.timeDifferenceMillis = timeDifferenceMillis;
        }
    }

    static ConcurrentHashMap<String, ClientInfo> clientData = new ConcurrentHashMap<>();

    // Thread to receive time from each client
    public static void startReceivingClockTime(Socket connector, String address) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                InputStream inputStream = connector.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Date clientTime = sdf.parse(line);
                    long timeDifference = new Date().getTime() - clientTime.getTime();
                    clientData.put(address, new ClientInfo(connector, clientTime, timeDifference));
                    System.out.println("Client data updated: " + address);
                    Thread.sleep(5000); // 5 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Thread to accept new client connections
    public static void startConnecting(ServerSocket masterServer) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            while (true) {
                try {
                    Socket connector = masterServer.accept();
                    String address = connector.getInetAddress().getHostAddress() + ":" + connector.getPort();
                    System.out.println(address + " got connected successfully");
                    startReceivingClockTime(connector, address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // function to get average clock difference
    public static long getAverageClockDiff() {
        long sum = 0;
        int count = 0;
        for (ClientInfo client : clientData.values()) {
            sum += client.timeDifferenceMillis;
            count++;
        }
        if (count == 0) return 0;
        return sum / count;
    }

    // Thread to synchronize all client clocks
    public static void synchronizeAllClocks() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (true) {
                try {
                    System.out.println("\nNew synchronization cycle started.");
                    System.out.println("Number of clients to be synchronized: " + clientData.size());
                    if (clientData.size() > 0) {
                        long avgDiff = getAverageClockDiff();
                        for (Map.Entry<String, ClientInfo> entry : clientData.entrySet()) {
                            try {
                                Socket clientSocket = entry.getValue().socket;
                                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                                Date synchronizedTime = new Date(System.currentTimeMillis() + avgDiff);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                writer.println(sdf.format(synchronizedTime));
                            } catch (Exception e) {
                                System.out.println("Error sending synchronized time to: " + entry.getKey());
                            }
                        }
                    } else {
                        System.out.println("No clients to synchronize.");
                    }
                    Thread.sleep(5000); // 5 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // function to initiate the server
    public static void initiateClockServer(int port) {
        try {
            ServerSocket masterServer = new ServerSocket(port);
            System.out.println("Clock Server started successfully.\n");
            System.out.println("Starting to make connections...\n");
            startConnecting(masterServer);
            System.out.println("Starting synchronization parallelly...\n");
            synchronizeAllClocks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Driver function
    public static void main(String[] args) {
        initiateClockServer(8080);
    }
}

