import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MancalaGUI extends JFrame {
    private MancalaGame gioco;
    private PitPanel[] buchePanels;
    private StorePanel store1, store2;
    private JLabel turnoLabel;

    public MancalaGUI() {
        gioco = new MancalaGame();
        buchePanels = new PitPanel[14];

        setTitle("Mancala Game");
        setSize(1000, 400);
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
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnoLabel.setForeground(Color.BLACK);
        turnoLabel.setBackground(orangeTheme);
        turnoLabel.setOpaque(true);

        add(turnoLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        aggiornaTabellone();
    }

    private class PitPanel extends JPanel {
        private int index;

        public PitPanel(int index) {
            this.index = index;
            setPreferredSize(new Dimension(100, 100));
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
            g.setColor(Color.ORANGE.darker());
            g.fillOval(10, 10, getWidth() - 20, getHeight() - 20);

            int pietre = gioco.getCampo()[index];
            Random rand = new Random();
            for (int i = 0; i < pietre; i++) {
                int x = 20 + rand.nextInt(getWidth() - 40);
                int y = 20 + rand.nextInt(getHeight() - 40);
                g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                g.fillOval(x, y, 10, 10);
            }

            g.setColor(Color.BLACK);
            g.drawString("Buca " + (index + 1), 10, 15);
        }
    }

    private class StorePanel extends JPanel {
        private int index;

        public StorePanel(int index) {
            this.index = index;
            setPreferredSize(new Dimension(100, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.ORANGE.darker());
            g.fillRect(20, 20, getWidth() - 40, getHeight() - 40);

            int pietre = gioco.getCampo()[index];
            Random rand = new Random();
            for (int i = 0; i < pietre; i++) {
                int x = 30 + rand.nextInt(getWidth() - 60);
                int y = 30 + rand.nextInt(getHeight() - 60);
                g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                g.fillOval(x, y, 10, 10);
            }

            g.setColor(Color.BLACK);
            g.drawString("Store " + (index == 6 ? "1" : "2"), 10, 15);
        }
    }

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
