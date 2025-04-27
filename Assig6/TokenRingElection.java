 import java.util.Scanner;

public class TokenRingElection {

    static class Process {
        int id;
        boolean active;

        Process(int id) {
            this.id = id;
            this.active = true;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Take number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        Process[] processes = new Process[n];

        // Initialize processes with their IDs
        System.out.println("Enter process IDs:");
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(sc.nextInt());
        }

        // Take initiator (who starts the election)
        System.out.print("Enter initiator process index (0 to " + (n-1) + "): ");
        int initiator = sc.nextInt();

        // Start election
        int[] token = new int[n];
        int index = initiator;
        int count = 0;

        System.out.println("\nElection Messages:");

        // Passing the token around the ring
        do {
            token[count++] = processes[index].id;
            int next = (index + 1) % n;
            System.out.println("Process " + processes[index].id + " sends token to Process " + processes[next].id);
            index = next;
        } while (index != initiator);

        // Find the maximum ID in the token
        int leader = token[0];
        for (int i = 1; i < count; i++) {
            if (token[i] > leader) {
                leader = token[i];
            }
        }

        System.out.println("\nLeader Election Complete!");
        System.out.println("Process " + leader + " is elected as Leader.");

        sc.close();
    }
}

