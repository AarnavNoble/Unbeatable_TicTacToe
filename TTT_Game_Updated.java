import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TTT_Game_Updated extends JPanel {
    boolean choice = false;
    boolean computerWon = false;
    char playerChar = 'X';
    String playerName = "Player 1";
    JButton[] buttons = new JButton[9];

    public TTT_Game_Updated() {
        userChoice();
        setLayout(new GridLayout(3, 3));
        initializeButtons();
    }

    // Method to start the game
    public void startGame() {
        // If choice is true, the user chose to play against another player
        // If choice is false, the user chose to play against the computer
        if (!choice && playerChar == 'O') {
            // If playing against the computer, make the first move for the computer
            makeComputerMove();
        }
    }

    public void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setText("_");
            buttons[i].setBackground(Color.white);

            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton buttonClicked = (JButton) e.getSource();
                    buttonClicked.setText(String.valueOf(playerChar));
                    buttonClicked.setBackground(Color.red);
                    buttonClicked.setEnabled(false);

                    if (checkForWinner()) {
                        displayVictor();
                        return;
                    }

                    if (!checkForDraw()) {
                        if (choice) {
                            if (playerChar == 'X') {
                                playerChar = 'O';
                                playerName = "Player 2";
                                buttonClicked.setBackground(Color.MAGENTA);
                            } else {
                                playerChar = 'X';
                                playerName = "Player 1";
                                buttonClicked.setBackground(Color.ORANGE);
                            }
                        } else {
                            makeComputerMove();
                        }
                    } else {
                        displayVictor();
                    }
                }
            });
            add(buttons[i]);
        }
    }

    public void userChoice() {
        String[] options = { "Player vs Player", "Player vs Computer" };
        int paneDialog = JOptionPane.showOptionDialog(this, "Would you like to play against the computer, or another player?",
                "Please select an option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options,
                options[0]);

        if (paneDialog == 0) {
            choice = true;
        }
    }

    // Method to make the computer move using Minimax algorithm
    private void makeComputerMove() {
        int bestMove = findBestMove();
        buttons[bestMove].setText("O");
        buttons[bestMove].setBackground(Color.CYAN);
        buttons[bestMove].setEnabled(false);
        if (checkForWinner()) {
            computerWon = true;
            displayVictor();
        } else if (checkForDraw()) {
            displayVictor();
        }
    }

    // Method to find the best move for the computer using Minimax algorithm
    private int findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().charAt(0) == '_') {
                buttons[i].setText("O");
                int score = minimax(0, false);
                buttons[i].setText("_");

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    // Minimax algorithm implementation
    private int minimax(int depth, boolean isMaximizingPlayer) {
        if (checkForWinner()) {
            return isMaximizingPlayer ? -1 : 1;
        }

        if (checkForDraw()) {
            return 0;
        }

        int bestScore = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().charAt(0) == '_') {
                buttons[i].setText(isMaximizingPlayer ? "O" : "X");
                int score = minimax(depth + 1, !isMaximizingPlayer);
                buttons[i].setText("_");

                bestScore = isMaximizingPlayer ? Math.max(bestScore, score) : Math.min(bestScore, score);
            }
        }

        return bestScore;
    }

    // Method to display the game result
    public void displayVictor() {
        int paneDialog;
        if (checkForWinner()) {
            if (choice) {
                if (playerChar == 'X')
                    playerName = "Player 2";
                else
                    playerName = "Player 1";
            } else {
                if (computerWon)
                    playerName = "Computer";
                else
                    playerName = "Player";
            }
            JOptionPane pane = new JOptionPane();
            if (playerName.equals("Player 1") || playerName.equals("Player 2") || playerName.equals("Player")) {
                paneDialog = JOptionPane.showConfirmDialog(pane,
                        "Congratulations! " + playerName + " Wins! Would you like to play again?", "VICTORY!",
                        JOptionPane.YES_NO_OPTION);
            } else {
                paneDialog = JOptionPane.showConfirmDialog(pane,
                        "Oh No...The " + playerName + " Wins. Would you like to play again?", "DEFEAT.",
                        JOptionPane.YES_NO_OPTION);
            }
            if (paneDialog == JOptionPane.YES_OPTION) {
                computerWon = false;
                resetTheButtons();
                playerChar = 'X';
                playerName = "Player 1";
                if (!choice) {
                    makeComputerMove();
                }
            } else {
                System.exit(0);
            }
        } else if (checkForDraw()) {
            JOptionPane pane = new JOptionPane();
            paneDialog = JOptionPane.showConfirmDialog(pane, "FORCED DRAW! Would you like to play again?", "DRAW!",
                    JOptionPane.YES_NO_OPTION);

            if (paneDialog == JOptionPane.YES_OPTION) {
                resetTheButtons();
                playerChar = 'X';
                playerName = "Player 1";
                //if (!choice) {
                  //  makeComputerMove();
                //}
            } else {
                System.exit(0);
            }
        }
    }

    private void resetTheButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("_");
            buttons[i].setBackground(Color.white);
            buttons[i].setEnabled(true);
        }
    }

    // Method to check for a forced draw scenario
    public boolean checkForDraw() {
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().charAt(0) == '_') {
                return false;
            }
        }
        return true;
    }

    // Method to check for a winning scenario
    public boolean checkForWinner() {
        return (checkRows() || checkColumns() || checkDiagonals());
    }

    // Methods to check if any one of the three rows are filled by the same player
    private boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i].getText().equals(buttons[i + 1].getText()) && buttons[i].getText().equals(buttons[i + 2].getText())
                    && buttons[i].getText().charAt(0) != '_') {
                return true;
            }
            i = i + 2; // Jump to the next row
        }
        return false;
    }

    // Method to check if any one of the three columns are filled by the same player
    private boolean checkColumns() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i].getText().equals(buttons[i + 3].getText()) && buttons[i].getText().equals(buttons[i + 6].getText())
                    && buttons[i].getText().charAt(0) != '_') {
                return true;
            }
        }
        return false;
    }

    // Method to check if any one of the two diagonals are filled by the same player
    private boolean checkDiagonals() {
        return (buttons[0].getText().equals(buttons[4].getText()) && buttons[0].getText().equals(buttons[8].getText())
                && buttons[0].getText().charAt(0) != '_')
                || (buttons[2].getText().equals(buttons[4].getText()) && buttons[2].getText().equals(buttons[6].getText())
                        && buttons[2].getText().charAt(0) != '_');
    }
}