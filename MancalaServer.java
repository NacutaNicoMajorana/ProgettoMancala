package Mancala;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class MancalaServer {
    public static void main(String[] args) throws IOException {

        int port = 5555;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server avviato sulla porta " + port);

        // Connessione Giocatore 1
        Socket player1 = serverSocket.accept();
        PrintWriter output1 = new PrintWriter(player1.getOutputStream(), true);
        BufferedReader input1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
        output1.println("GIOCATORE 1");
        System.out.println("Giocatore 1 connesso");

        // Connessione Giocatore 2
        Socket player2 = serverSocket.accept();
        PrintWriter output2 = new PrintWriter(player2.getOutputStream(), true);
        BufferedReader input2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
        output2.println("GIOCATORE 2");
        System.out.println("Giocatore 2 connesso");

        MancalaGame gioco = new MancalaGame();

        while (!gioco.isPartitaFinita()) {

            int turno = gioco.getGiocatoreCorrente();
            PrintWriter outCorr = turno == 1 ? output1 : output2;
            BufferedReader inCorr = turno == 1 ? input1 : input2;
            PrintWriter outAltro = turno == 1 ? output2 : output1;

            // Messaggi turno
            outCorr.println("Tocca a te, Giocatore " + turno);
            outCorr.println("MOVE");
            outAltro.println("In attesa del Giocatore " + turno);

            // Lettura mossa
            String linea = inCorr.readLine();
            if (linea == null) break;

            try {
                int mossa = Integer.parseInt(linea.trim());
                if (!gioco.faiMossa(mossa)) {
                    outCorr.println("Mossa non valida");
                }
            } catch (Exception e) {
                outCorr.println("Input non valido");
            }

            // Aggiornamento campo
            String campo = Arrays.toString(gioco.getCampo());
            output1.println(campo);
            output2.println(campo);
        }

        // Fine partita
        output1.println("Vincitore: " + gioco.getVincitore());
        output2.println("Vincitore: " + gioco.getVincitore());

        serverSocket.close();
    }
}
