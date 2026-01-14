package Mancala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class MancalaGUI extends JFrame {

    private MancalaGame gioco;
    private PitPanel[] buchePanels;
    private StorePanel store1, store2;
    private JLabel turnoLabel;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private int myPlayerNumber = 0;
    private boolean mioTurno = false;

    public MancalaGUI() {

        try {
            socket = new Socket("localhost", 5555);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errore connessione server");
            e.printStackTrace();
        }

        gioco = new MancalaGame();
        buchePanels = new PitPanel[14];

        setTitle("Mancala Game");
        setSize(1100, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Color orangeTheme = new Color(255, 140, 0);
        getContentPane().setBackground(orangeTheme);

        JPanel boardPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        boardPanel.setBackground(orangeTheme);

        store2 = new StorePanel(13);
        store2.setBackground(orangeTheme);
        add(store2, BorderLayout.EAST);

        store1 = new StorePanel(6);
        store1.setBackground(orangeTheme);
        add(store1, BorderLayout.WEST);

        for (int i = 12; i >= 7; i--) {
            buchePanels[i] = new PitPanel(i);
            boardPanel.add(buchePanels[i]);
        }

        for (int i = 0; i <= 5; i++) {
            buchePanels[i] = new PitPanel(i);
            boardPanel.add(buchePanels[i]);
        }

        turnoLabel = new JLabel("Connessione in corso...", SwingConstants.CENTER);
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnoLabel.setOpaque(true);
        turnoLabel.setBackground(orangeTheme);

        add(turnoLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        aggiornaTabellone();

        new Thread(() -> {
            try {
                String line;
                while ((line = input.readLine()) != null) {

                    // Identificazione giocatore
                    if (line.startsWith("PLAYER")) {
                        myPlayerNumber = Integer.parseInt(line.split(" ")[1]);
                        turnoLabel.setText("Sei il Giocatore " + myPlayerNumber);
                        continue;
                    }

                    // Turno
                    if (line.startsWith("Tocca a te")) {
                        int turno = Integer.parseInt(line.replaceAll("\\D+", ""));
                        if (turno == myPlayerNumber) {
                            mioTurno = true;
                            turnoLabel.setText("Tocca a te!");
                        } else {
                            mioTurno = false;
                            turnoLabel.setText("In attesa del Giocatore " + turno);
                        }
                        continue;
                    }

                    // Messaggi di attesa espliciti
                    if (line.startsWith("In attesa")) {
                        turnoLabel.setText(line);
                        mioTurno = false;
                        continue;
                    }

                    // Campo aggiornato
                    if (line.startsWith("[")) {
                        int[] campo = parseCampo(line);
                        gioco.setCampo(campo);
                        SwingUtilities.invokeLater(this::aggiornaTabellone);
                        continue;
                    }

                    // Fine partita
                    if (line.startsWith("Vincitore")) {
                        turnoLabel.setText(line);
                        mioTurno = false;
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class PitPanel extends JPanel {
        private int index;

        public PitPanel(int index) {
            this.index = index;
            setPreferredSize(new Dimension(110, 110));
            setBackground(new Color(255, 200, 120));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // invia la mossa SOLO se è il mio turno
                    if (mioTurno && myPlayerNumber != 0) {
                        output.println(index + 1);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = getWidth();
            int h = getHeight();

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Buca " + (index + 1), w / 2 - 25, 15);

            g.setColor(Color.ORANGE.darker());
            g.fillOval(10, 25, w - 20, h - 35);

            int pietre = gioco.getCampo()[index];
            drawStones(g, pietre, 10, 25, w - 20, h - 35);
        }
    }

    private class StorePanel extends JPanel {
        private int index;

        public StorePanel(int index) {
            this.index = index;
            setPreferredSize(new Dimension(120, 300));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = getWidth();
            int h = getHeight();

            g.setColor(Color.ORANGE.darker());
            g.fillRect(20, 20, w - 40, h - 40);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Store " + (index == 6 ? "1" : "2"), w / 2 - 30, 15);

            int pietre = gioco.getCampo()[index];
            drawStones(g, pietre, 20, 20, w - 40, h - 40);
        }
    }

    private void drawStones(Graphics g, int count, int x, int y, int width, int height) {
        int cols = 5;
        int spacing = 18;

        int row = 0;
        int col = 0;

        for (int i = 0; i < count; i++) {
            int px = x + 10 + col * spacing;
            int py = y + 10 + row * spacing;

            g.setColor(new Color(80, 80, 80));
            g.fillOval(px, py, 12, 12);

            col++;
            if (col >= cols) {
                col = 0;
                row++;
            }
        }
    }

    private void aggiornaTabellone() {
        for (int i = 0; i < 14; i++) {
            if (i != 6 && i != 13) buchePanels[i].repaint();
        }
        store1.repaint();
        store2.repaint();
    }

    private int[] parseCampo(String s) {
        s = s.replace("[", "").replace("]", "");
        String[] parts = s.split(",");
        int[] campo = new int[14];
        for (int i = 0; i < 14; i++) {
            campo[i] = Integer.parseInt(parts[i].trim());
        }
        return campo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MancalaGUI gui = new MancalaGUI();
            gui.setVisible(true);
        });
    }
}
