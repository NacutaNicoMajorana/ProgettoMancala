package Mancala;
import java.io.*;
import java.net.*;

public class MancalaClient {
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 5555);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        int myPlayer = 0;

        String line;
        while ((line = input.readLine()) != null) {

            if (line.startsWith("PLAYER")) {
                myPlayer = Integer.parseInt(line.split(" ")[1]);
                System.out.println("Sei il Giocatore " + myPlayer);
                continue;
            }

            if (line.startsWith("Tocca a te")) {
                System.out.println(line);
                continue;
            }

            if (line.startsWith("In attesa")) {
                System.out.println(line);
                continue;
            }

            if (line.equals("MOVE")) {
                System.out.print("Scegli una buca (1-14): ");
                output.println(console.readLine());
                continue;
            }

            if (line.startsWith("[")) {
                System.out.println("Campo aggiornato: " + line);
                continue;
            }

            if (line.startsWith("Vincitore")) {
                System.out.println(line);
                break;
            }

            System.out.println(line);
        }

        socket.close();
    }
}
