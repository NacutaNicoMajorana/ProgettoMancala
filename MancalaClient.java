import java.io.*;
import java.net.*;

public class MancalaClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5555);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
            if (line.equals("MOVE")) {
                System.out.print("Scegli una buca (1-14): ");
                String scelta = console.readLine();
                output.println(scelta);
            }
        }
        socket.close();
    }
}
