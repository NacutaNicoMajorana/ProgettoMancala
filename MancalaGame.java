import java.util.Arrays;

public class MancalaGame {

    private int[] campo;              // 14 caselle: 0..13
    private int giocatoreCorrente;
    private boolean partitaFinita;

    public MancalaGame() {
        campo = new int[14]; // indici 0..13
        for (int i = 0; i < 14; i++) {
            if (i != 6 && i != 13) { // 6 = store giocatore 1, 13 = store giocatore 2
                campo[i] = 4;
            }
        }
        giocatoreCorrente = 1;
        partitaFinita = false;
    }

    public boolean faiMossa(int bucaInput) {
        // bucaInput è quello che scrive il giocatore (1..14)
        int buca = bucaInput - 1; // converto in indice array (0..13)

        if (partitaFinita) return false;
        if (!mossaValida(buca)) return false;

        int pietre = campo[buca];
        campo[buca] = 0;
        int indice = buca;

        while (pietre > 0) {
            indice = (indice + 1) % 14; // cicla da 0 a 13
            // Evita di mettere pietre nello store avversario
            if (giocatoreCorrente == 1 && indice == 13) continue;
            if (giocatoreCorrente == 2 && indice == 6) continue;

            int precedente = campo[indice];
            campo[indice]++;
            pietre--;

            // cattura valida: la buca era vuota prima e ora ha 1 pietra
            if (pietre == 0) { // solo sull'ultima pietra
                if (giocatoreCorrente == 1 && indice >= 0 && indice <= 5 && campo[indice] == 1 && precedente == 0) {
                    int opposta = 12 - indice;
                    campo[6] += campo[opposta] + 1;
                    campo[indice] = 0;
                    campo[opposta] = 0;
                }
                if (giocatoreCorrente == 2 && indice >= 7 && indice <= 12 && campo[indice] == 1 && precedente == 0) {
                    int opposta = 12 - indice;
                    campo[13] += campo[opposta] + 1;
                    campo[indice] = 0;
                    campo[opposta] = 0;
                }
            }
        }

        // seconda mossa se finisci nel tuo store
        if ((giocatoreCorrente == 1 && indice == 6) || (giocatoreCorrente == 2 && indice == 13)) {
            controllaFinePartita(); // verifica comunque
            return true;
        }

        // controllo fine partita prima di cambiare turno
        controllaFinePartita();
        if (!partitaFinita) {
            giocatoreCorrente = (giocatoreCorrente == 1) ? 2 : 1;
        }
        return true;
    }

    private boolean mossaValida(int buca) {
        if (giocatoreCorrente == 1 && buca >= 0 && buca <= 5) return campo[buca] > 0;
        if (giocatoreCorrente == 2 && buca >= 7 && buca <= 12) return campo[buca] > 0;
        return false;
    }

    private void controllaFinePartita() {
        boolean lato1Vuoto = true;
        boolean lato2Vuoto = true;

        for (int i = 0; i <= 5; i++) {
            if (campo[i] != 0) lato1Vuoto = false;
        }
        for (int i = 7; i <= 12; i++) {
            if (campo[i] != 0) lato2Vuoto = false;
        }

        if (lato1Vuoto || lato2Vuoto) {
            if (lato1Vuoto) {
                for (int i = 7; i <= 12; i++) {
                    campo[13] += campo[i];
                    campo[i] = 0;
                }
            } else {
                for (int i = 0; i <= 5; i++) {
                    campo[6] += campo[i];
                    campo[i] = 0;
                }
            }
            partitaFinita = true;
        }
    }

    public int[] getCampo() {
        return Arrays.copyOf(campo, campo.length);
    }

    public int getGiocatoreCorrente() {
        return giocatoreCorrente;
    }

    public boolean isPartitaFinita() {
        return partitaFinita;
    }

    public String getVincitore() {
        if (!partitaFinita) return "In corso";
        if (campo[6] > campo[13]) return "Giocatore 1";
        if (campo[13] > campo[6]) return "Giocatore 2";
        return "Pareggio";
    }
}
