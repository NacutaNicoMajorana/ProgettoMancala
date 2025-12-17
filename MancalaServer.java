import java.io.*;
import java.net.*;
import java.util.Arrays;

public class MancalaServer {
    public static void main(String[] args) throws IOException {
        int port = 5555;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server avviato sulla porta " + port);

        Socket player1 = serverSocket.accept();
        PrintWriter output1 = new PrintWriter(player1.getOutputStream(), true);
        BufferedReader input1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
        System.out.println("Giocatore 1 connesso");

        Socket player2 = serverSocket.accept();
        PrintWriter output2 = new PrintWriter(player2.getOutputStream(), true);
        BufferedReader input2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
        System.out.println("Giocatore 2 connesso");

        MancalaGame gioco = new MancalaGame();

        while (!gioco.isPartitaFinita()) {
            PrintWriter outputCorrente = gioco.getGiocatoreCorrente() == 1 ? output1 : output2;
            BufferedReader inputCorrente = gioco.getGiocatoreCorrente() == 1 ? input1 : input2;

            outputCorrente.println("Tocca a te, Giocatore " + gioco.getGiocatoreCorrente());
            outputCorrente.println("MOVE");

            String linea = inputCorrente.readLine();
            if (linea == null) break;
            try {
                int mossa = Integer.parseInt(linea.trim());
                if (!gioco.faiMossa(mossa)) {
                    outputCorrente.println("Mossa non valida");
                }
            } catch (NumberFormatException e) {
                outputCorrente.println("Input non valido");
            }

            output1.println(Arrays.toString(gioco.getCampo()));
            output2.println(Arrays.toString(gioco.getCampo()));
            stampaTabellone(gioco.getCampo());
        }

        output1.println("Vincitore: " + gioco.getVincitore());
        output2.println("Vincitore: " + gioco.getVincitore());

        serverSocket.close();
    }

    private static void stampaTabellone(int[] campo) {
        System.out.println("======================================");
        System.out.print("Giocatore 2:   ");
        for (int i = 12; i >= 7; i--) {
            System.out.print("[" + (i+1) + ":" + campo[i] + "] ");
        }
        System.out.println("   Store2(14): " + campo[13]);

        System.out.print("Giocatore 1:   ");
        for (int i = 0; i <= 5; i++) {
            System.out.print("[" + (i+1) + ":" + campo[i] + "] ");
        }
        System.out.println("   Store1(7): " + campo[6]);
        System.out.println("======================================");
    }
}
