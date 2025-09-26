import java.util.Scanner;

public class TicTacToe {
    static final int COMPUTER = 1;
    static final int HUMAN = 2;
    static final int SIDE = 3;
    static final char COMPUTERMOVE = 'O';
    static final char HUMANMOVE = 'X';

    static final Scanner scanner = new Scanner(System.in);

   
    static void showBoard(char[][] board) {
        System.out.println();
        System.out.printf("\t\t\t %c | %c | %c \n", board[0][0], board[0][1], board[0][2]);
        System.out.println("\t\t\t-----------");
        System.out.printf("\t\t\t %c | %c | %c \n", board[1][0], board[1][1], board[1][2]);
        System.out.println("\t\t\t-----------");
        System.out.printf("\t\t\t %c | %c | %c \n\n", board[2][0], board[2][1], board[2][2]);
    }

    
    static void showInstructions() {
        System.out.println("\nChoose a cell numbered from 1 to 9 as below and play\n");
        System.out.println("\t\t\t 1 | 2 | 3 ");
        System.out.println("\t\t\t-----------");
        System.out.println("\t\t\t 4 | 5 | 6 ");
        System.out.println("\t\t\t-----------");
        System.out.println("\t\t\t 7 | 8 | 9 \n");
    }

    static void initialise(char[][] board) {
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++)
                board[i][j] = ' ';
    }

    static void declareWinner(int whoseTurn) {
        if (whoseTurn == COMPUTER)
            System.out.println("COMPUTER has won");
        else
            System.out.println("HUMAN has won");
    }

   
    static char checkWinner(char[][] board) {
        
        for (int i = 0; i < SIDE; i++) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return board[i][0];
        }
     
        for (int j = 0; j < SIDE; j++) {
            if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j])
                return board[0][j];
        }
       
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return board[0][0];
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return board[0][2];
        return ' ';
    }

    static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++)
                if (board[i][j] == ' ')
                    return false;
        return true;
    }

    
    static int minimax(char[][] board, int depth, boolean isMaximizing) {
        char winner = checkWinner(board);
        if (winner == COMPUTERMOVE) {
            return 10 - depth; 
        }
        if (winner == HUMANMOVE) {
            return -10 + depth;
        }
        if (isBoardFull(board)) {
            return 0;
        }

        if (isMaximizing) {
            int bestVal = Integer.MIN_VALUE;
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = COMPUTERMOVE;
                        int value = minimax(board, depth + 1, false);
                        board[i][j] = ' ';
                        bestVal = Math.max(bestVal, value);
                    }
                }
            }
            return bestVal;
        } else {
            int bestVal = Integer.MAX_VALUE;
            for (int i = 0; i < SIDE; i++) {
                for (int j = 0; j < SIDE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = HUMANMOVE;
                        int value = minimax(board, depth + 1, true);
                        board[i][j] = ' ';
                        bestVal = Math.min(bestVal, value);
                    }
                }
            }
            return bestVal;
        }
    }

  
    static int bestMove(char[][] board, int moveCount) {
        int bestScore = Integer.MIN_VALUE;
        int bestR = -1, bestC = -1;

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = COMPUTERMOVE;
                    int score = minimax(board, moveCount + 1, false);
                    board[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        bestR = i;
                        bestC = j;
                    }
                }
            }
        }

       
        if (bestR == -1) {
            for (int i = 0; i < SIDE; i++)
                for (int j = 0; j < SIDE; j++)
                    if (board[i][j] == ' ')
                        return i * SIDE + j;
        }
        return bestR * SIDE + bestC;
    }

    
    static int getValidUserMove(char[][] board) {
        while (true) {
            System.out.print("\n\nEnter the position = ");
            String line = scanner.nextLine();
            if (line == null || line.trim().isEmpty()) {
                System.out.println("You must enter something.");
                continue;
            }
            line = line.trim();
            char ch = line.charAt(0);
            if (!Character.isDigit(ch)) {
                System.out.println("You must enter a number from 1 to 9!");
                continue;
            }
            int n = ch - '0';
            if (n < 1 || n > 9) {
                System.out.println("Invalid position (must be 1-9).");
                continue;
            }
            int index = n - 1;
            int row = index / SIDE;
            int col = index % SIDE;
            if (board[row][col] != ' ') {
                System.out.println("That grid position is already taken!");
                continue;
            }
            return index;
        }
    }

    static void playTicTacToe(int whoseTurn) {
        char[][] board = new char[SIDE][SIDE];
        initialise(board);
        showInstructions();
        showBoard(board);

        int moveCount = 0;

        while (true) {
            if (checkWinner(board) != ' ' || isBoardFull(board)) break;

            if (whoseTurn == COMPUTER) {
                int n = bestMove(board, moveCount);
                int x = n / SIDE;
                int y = n % SIDE;
                board[x][y] = COMPUTERMOVE;
                System.out.printf("COMPUTER has put a %c in cell %d\n\n", COMPUTERMOVE, n + 1);
                showBoard(board);
                moveCount++;
                whoseTurn = HUMAN;
            } else {
                System.out.print("You can insert in the following positions: ");
                for (int i = 0; i < SIDE; i++)
                    for (int j = 0; j < SIDE; j++)
                        if (board[i][j] == ' ')
                            System.out.print((i * SIDE + j + 1) + " ");
                int n = getValidUserMove(board);
                int x = n / SIDE;
                int y = n % SIDE;
                board[x][y] = HUMANMOVE;
                System.out.printf("\nHUMAN has put a %c in cell %d\n\n", HUMANMOVE, n + 1);
                showBoard(board);
                moveCount++;
                whoseTurn = COMPUTER;
            }
        }

        char winner = checkWinner(board);
        if (winner == ' ') {
            System.out.println("It's a draw");
        } else {
            if (winner == COMPUTERMOVE)
                declareWinner(COMPUTER);
            else
                declareWinner(HUMAN);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n-------------------------------------------------------------------\n");
        System.out.println("\t\t\t Tic-Tac-Toe\n");
        System.out.println("-------------------------------------------------------------------\n");

        String cont;
        do {
            System.out.print("Do you want to start first? (y/n) : ");
            String choice = scanner.nextLine().trim();
            if (!choice.isEmpty() && (choice.charAt(0) == 'n' || choice.charAt(0) == 'N')) {
                playTicTacToe(COMPUTER);
            } else if (!choice.isEmpty() && (choice.charAt(0) == 'y' || choice.charAt(0) == 'Y')) {
                playTicTacToe(HUMAN);
            } else {
                System.out.println("Invalid choice (enter 'y' or 'n').");
            }

            System.out.print("\nDo you want to play again? (y/n) : ");
            cont = scanner.nextLine().trim();
        } while (!cont.isEmpty() && (cont.charAt(0) == 'y' || cont.charAt(0) == 'Y'));

        System.out.println("Thanks for playing!");
        
    }
}


