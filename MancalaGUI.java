package Mancala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MancalaGUI extends JFrame {

    private MancalaGame gioco;
    private PitPanel[] buchePanels;
    private StorePanel store1, store2;
    private JLabel turnoLabel;

    public MancalaGUI() {
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

        // Store Giocatore 2
        store2 = new StorePanel(13);
        store2.setBackground(orangeTheme);
        add(store2, BorderLayout.EAST);

        // Store Giocatore 1
        store1 = new StorePanel(6);
        store1.setBackground(orangeTheme);
        add(store1, BorderLayout.WEST);

        // Buche Giocatore 2 (12..7)
        for (int i = 12; i >= 7; i--) {
            buchePanels[i] = new PitPanel(i);
            boardPanel.add(buchePanels[i]);
        }

        // Buche Giocatore 1 (0..5)
        for (int i = 0; i <= 5; i++) {
            buchePanels[i] = new PitPanel(i);
            boardPanel.add(buchePanels[i]);
        }

        turnoLabel = new JLabel("Turno: Giocatore " + gioco.getGiocatoreCorrente(), SwingConstants.CENTER);
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnoLabel.setOpaque(true);
        turnoLabel.setBackground(orangeTheme);

        add(turnoLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        aggiornaTabellone();
    }

    // ---------------------------------------------------------
    // PANNELLO BUCA
    // ---------------------------------------------------------
    private class PitPanel extends JPanel {
        private int index;

        public PitPanel(int index) {
            this.index = index;
            setPreferredSize(new Dimension(110, 110));
            setBackground(new Color(255, 200, 120));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!gioco.isPartitaFinita()) {
                        if (gioco.faiMossa(index + 1)) {
                            aggiornaTabellone();
                        }
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = getWidth();
            int h = getHeight();

            // Numero della buca sopra il cerchio
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Buca " + (index + 1), w / 2 - 25, 15);

            // Cerchio della buca
            g.setColor(Color.ORANGE.darker());
            g.fillOval(10, 25, w - 20, h - 35);

            // Pietre ordinate in griglia
            int pietre = gioco.getCampo()[index];
            drawStones(g, pietre, 10, 25, w - 20, h - 35);
        }
    }

    // ---------------------------------------------------------
    // PANNELLO STORE
    // ---------------------------------------------------------
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

    // ---------------------------------------------------------
    // DISEGNO PIETRE ORDINATE
    // ---------------------------------------------------------
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

    // ---------------------------------------------------------
    private void aggiornaTabellone() {
        for (int i = 0; i < 14; i++) {
            if (i != 6 && i != 13) buchePanels[i].repaint();
        }
        store1.repaint();
        store2.repaint();

        if (gioco.isPartitaFinita()) {
            turnoLabel.setText("Partita finita! Vincitore: " + gioco.getVincitore());
        } else {
            turnoLabel.setText("Turno: Giocatore " + gioco.getGiocatoreCorrente());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MancalaGUI gui = new MancalaGUI();
            gui.setVisible(true);
        });
    }
}
