package Mancala;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class MancalaServer {

    public static void main(String[] args) throws IOException {

        int port = 5555;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("server avviato sulla porta " + port);

        while (true) {   

            System.out.println("In attesa di due giocatori...");

            // --- CONNESSIONE GIOCATORE 1 ---
            Socket player1 = serverSocket.accept();
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            out1.println("PLAYER 1");
            System.out.println("Giocatore 1 connesso");

            // --- CONNESSIONE GIOCATORE 2 ---
            Socket player2 = serverSocket.accept();
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            out2.println("PLAYER 2");
            System.out.println("Giocatore 2 connesso");

            // --- NUOVA PARTITA ---
            System.out.println("Partita iniziata!");
            MancalaGame gioco = new MancalaGame();

            // --- LOOP PARTITA ---
            while (!gioco.isPartitaFinita()) {

                int turno = gioco.getGiocatoreCorrente();

                PrintWriter outCorr = turno == 1 ? out1 : out2;
                BufferedReader inCorr = turno == 1 ? in1 : in2;
                PrintWriter outAltro = turno == 1 ? out2 : out1;

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
                out1.println(campo);
                out2.println(campo);
            }
            

            // --- FINE PARTITA ---
            String vincitore = "Vincitore: " + gioco.getVincitore();
            out1.println(vincitore);
            out2.println(vincitore);

            System.out.println("Partita terminata. In attesa di nuovi giocatori...");

            
            player1.close();
            player2.close();
        }
    }
}
