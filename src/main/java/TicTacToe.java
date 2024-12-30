/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author HP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private JButton[][] buttons;
    private int gridSize = 3; // Default grid size
    private boolean isPlayerOneTurn = true;
    private boolean isSinglePlayer = false;
    private String player1Name = "Player 1";
    private String player2Name = "Player 2";
    private String playerSymbol = "X";
    private String opponentSymbol = "O";

    public TicTacToe() {
        showGameModeSelection();
    }

    private void showGameModeSelection() {
        JDialog dialog = new JDialog(this, "Select Game Mode", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Choose Game Mode and Grid Size:", SwingConstants.CENTER);
        JButton singlePlayerButton = new JButton("Single Player");
        JButton twoPlayerButton = new JButton("Two Players");

        JComboBox<String> gridSizeComboBox = new JComboBox<>(new String[]{"3 x 3", "4 x 4"});
        gridSizeComboBox.setSelectedIndex(0);

        dialog.add(label);
        dialog.add(gridSizeComboBox);
        dialog.add(singlePlayerButton);
        dialog.add(twoPlayerButton);

        ActionListener modeSelectionListener = e -> {
            isSinglePlayer = e.getSource() == singlePlayerButton;
            gridSize = gridSizeComboBox.getSelectedIndex() == 0 ? 3 : 4;
            dialog.dispose();
            showPlayerNameInput();
        };

        singlePlayerButton.addActionListener(modeSelectionListener);
        twoPlayerButton.addActionListener(modeSelectionListener);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showPlayerNameInput() {
        JDialog dialog = new JDialog(this, "Player Names", true);
        dialog.setSize(400, isSinglePlayer ? 200 : 300);
        dialog.setLayout(new GridLayout(isSinglePlayer ? 3 : 5, 1));

        JLabel player1Label = new JLabel("Enter Player 1 Name:", SwingConstants.CENTER);
        JTextField player1Field = new JTextField();

        dialog.add(player1Label);
        dialog.add(player1Field);

        if (!isSinglePlayer) {
            JLabel player2Label = new JLabel("Enter Player 2 Name:", SwingConstants.CENTER);
            JTextField player2Field = new JTextField();

            dialog.add(player2Label);
            dialog.add(player2Field);

            JButton nextButton = new JButton("Next");
            nextButton.addActionListener(e -> {
                player1Name = player1Field.getText().isEmpty() ? "Player 1" : player1Field.getText();
                player2Name = player2Field.getText().isEmpty() ? "Player 2" : player2Field.getText();
                dialog.dispose();
                showSymbolSelection();
            });
            dialog.add(nextButton);
        } else {
            JButton nextButton = new JButton("Next");
            nextButton.addActionListener(e -> {
                player1Name = player1Field.getText().isEmpty() ? "Player 1" : player1Field.getText();
                player2Name = "Computer";
                dialog.dispose();
                showSymbolSelection();
            });
            dialog.add(nextButton);
        }

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void showSymbolSelection() {
        JDialog dialog = new JDialog(this, "Choose Symbol", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new GridLayout(3, 1));

        JLabel label = new JLabel("Choose your symbol (Player 1):", SwingConstants.CENTER);
        JButton xButton = new JButton("Play as X");
        JButton oButton = new JButton("Play as O");

        dialog.add(label);
        dialog.add(xButton);
        dialog.add(oButton);

        ActionListener symbolSelectionListener = e -> {
            if (e.getSource() == xButton) {
                playerSymbol = "X";
                opponentSymbol = "O";
            } else {
                playerSymbol = "O";
                opponentSymbol = "X";
            }
            JOptionPane.showMessageDialog(dialog, player1Name + " will play as " + playerSymbol + " and starts first.");
            dialog.dispose();
            initializeGame();
        };

        xButton.addActionListener(symbolSelectionListener);
        oButton.addActionListener(symbolSelectionListener);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void initializeGame() {
        buttons = new JButton[gridSize][gridSize];
        setTitle("Tic Tac Toe");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(gridSize, gridSize));
        Font font = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
                gamePanel.add(buttons[i][j]);

                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleButtonClick(row, col));
            }
        }

        JPanel controlPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        JButton aboutButton = new JButton("?");

        resetButton.addActionListener(e -> resetGame());
        aboutButton.addActionListener(e -> showAboutDialog());

        controlPanel.add(resetButton);
        controlPanel.add(aboutButton);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void handleButtonClick(int row, int col) {
        if (!buttons[row][col].getText().equals("")) return;

        String currentSymbol = isPlayerOneTurn ? playerSymbol : opponentSymbol;
        buttons[row][col].setText(currentSymbol);
        buttons[row][col].setForeground(isPlayerOneTurn ? Color.BLUE : Color.RED);

        if (checkWin()) {
            showWinMessage();
            return;
        }

        isPlayerOneTurn = !isPlayerOneTurn;

        if (isSinglePlayer && !isPlayerOneTurn) {
            computerMove();
        }
    }

    private void computerMove() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(opponentSymbol);
                    buttons[i][j].setForeground(Color.RED);
                    if (checkWin()) {
                        showWinMessage();
                    }
                    isPlayerOneTurn = true;
                    return;
                }
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < gridSize; i++) {
            boolean rowWin = true, colWin = true;
            for (int j = 1; j < gridSize; j++) {
                rowWin &= buttons[i][j].getText().equals(buttons[i][j - 1].getText()) && !buttons[i][j].getText().isEmpty();
                colWin &= buttons[j][i].getText().equals(buttons[j - 1][i].getText()) && !buttons[j][i].getText().isEmpty();
            }
            if (rowWin || colWin) {
                highlightWin(rowWin ? buttons[i] : getColumnButtons(i));
                return true;
            }
        }

        boolean diag1 = true, diag2 = true;
        for (int i = 1; i < gridSize; i++) {
            diag1 &= buttons[i][i].getText().equals(buttons[i - 1][i - 1].getText()) && !buttons[i][i].getText().isEmpty();
            diag2 &= buttons[i][gridSize - i - 1].getText().equals(buttons[i - 1][gridSize - i].getText()) && !buttons[i][gridSize - i - 1].getText().isEmpty();
        }
        if (diag1) {
            highlightWin(getDiagonalButtons(true));
            return true;
        }
        if (diag2) {
            highlightWin(getDiagonalButtons(false));
            return true;
        }
        return false;
    }

    private void highlightWin(JButton[] winningButtons) {
        for (JButton button : winningButtons) {
            button.setBackground(Color.GREEN);
        }
    }

    private JButton[] getColumnButtons(int col) {
        JButton[] column = new JButton[gridSize];
        for (int i = 0; i < gridSize; i++) {
            column[i] = buttons[i][col];
        }
        return column;
    }

    private JButton[] getDiagonalButtons(boolean mainDiagonal) {
        JButton[] diagonal = new JButton[gridSize];
        for (int i = 0; i < gridSize; i++) {
            diagonal[i] = mainDiagonal ? buttons[i][i] : buttons[i][gridSize - i - 1];
        }
        return diagonal;
    }

    private void showWinMessage() {
        String winner = isPlayerOneTurn ? player1Name : player2Name;
        JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }

    private void resetGame() {
        isPlayerOneTurn = true;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Tic Tac Toe is a game where players take turns marking X or O in a grid.\n" +
                        "The goal is to get three (or four) in a row horizontally, vertically, or diagonally.",
                "About Tic Tac Toe", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}