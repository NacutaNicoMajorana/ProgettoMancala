import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MancalaGUI extends JFrame {
    private MancalaGame gioco;
    private JButton[] bucheButtons;
    private JLabel store1, store2, turnoLabel;

    public MancalaGUI() {
        gioco = new MancalaGame();
        bucheButtons = new JButton[14];

        setTitle("Mancala Game");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Colore arancione di base
        Color orangeTheme = new Color(255, 140, 0);

        JPanel boardPanel = new JPanel(new GridLayout(2, 7, 10, 10));
        boardPanel.setBackground(orangeTheme);

        // Store Giocatore 2 (indice 13)
        store2 = new JLabel("Store2: " + gioco.getCampo()[13], SwingConstants.CENTER);
        store2.setOpaque(true);
        store2.setBackground(orangeTheme.darker());
        boardPanel.add(store2);

        // Buche 12..7 (Giocatore 2)
        for (int i = 12; i >= 7; i--) {
            bucheButtons[i] = createPitButton(i);
            boardPanel.add(bucheButtons[i]);
        }

        // Store Giocatore 1 (indice 6)
        store1 = new JLabel("Store1: " + gioco.getCampo()[6], SwingConstants.CENTER);
        store1.setOpaque(true);
        store1.setBackground(orangeTheme.darker());
        boardPanel.add(store1);

        // Buche 0..5 (Giocatore 1)
        for (int i = 0; i <= 5; i++) {
            bucheButtons[i] = createPitButton(i);
            boardPanel.add(bucheButtons[i]);
        }

        turnoLabel = new JLabel("Turno: Giocatore " + gioco.getGiocatoreCorrente(), SwingConstants.CENTER);
        turnoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnoLabel.setForeground(Color.BLACK);
        turnoLabel.setBackground(orangeTheme);
        turnoLabel.setOpaque(true);

        add(turnoLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        aggiornaTabellone();
    }

    private JButton createPitButton(int index) {
        JButton button = new JButton("Buca " + (index+1));
        button.setBackground(new Color(255, 200, 120));
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gioco.isPartitaFinita()) {
                    if (gioco.faiMossa(index+1)) {
                        aggiornaTabellone();
                    }
                }
            }
        });
        return button;
    }

    private void aggiornaTabellone() {
        int[] campo = gioco.getCampo();
        for (int i = 0; i < 14; i++) {
            if (i != 6 && i != 13) {
                bucheButtons[i].setText("Buca " + (i+1) + " (" + campo[i] + ")");
            }
        }
        store1.setText("Store1: " + campo[6]);
        store2.setText("Store2: " + campo[13]);

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
