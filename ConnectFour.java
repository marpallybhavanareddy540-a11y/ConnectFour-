import java.util.Scanner;

public class ConnectFour {

    static final int ROWS = 6;
    static final int COLS = 7;
    static final char EMPTY = '.';
    static final char P1 = 'X';
    static final char P2 = 'O';

    // ─── Board ────────────────────────────────────────────────────────────────

    static char[][] createBoard() {
        char[][] board = new char[ROWS][COLS];
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                board[r][c] = EMPTY;
        return board;
    }

    static void printBoard(char[][] board) {
        System.out.println();
        System.out.println(" 1  2  3  4  5  6  7");
        System.out.println("┌──┬──┬──┬──┬──┬──┬──┐");
        for (int r = 0; r < ROWS; r++) {
            System.out.print("│");
            for (int c = 0; c < COLS; c++) {
                System.out.print(" " + board[r][c] + "│");
            }
            System.out.println();
            if (r < ROWS - 1)
                System.out.println("├──┼──┼──┼──┼──┼──┼──┤");
        }
        System.out.println("└──┴──┴──┴──┴──┴──┴──┘");
        System.out.println();
    }

    // ─── Drop ─────────────────────────────────────────────────────────────────

    // Returns the row where the piece landed, or -1 if column is full
    static int dropPiece(char[][] board, int col, char player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                return row;
            }
        }
        return -1; // column full
    }

    // ─── Win Detection ────────────────────────────────────────────────────────

    static boolean checkWin(char[][] board, int row, int col, char player) {
        // directions: horizontal, vertical, diagonal ↘, diagonal ↗
        int[][] directions = { {0, 1}, {1, 0}, {1, 1}, {1, -1} };

        for (int[] dir : directions) {
            int count = 1;
            count += countInDirection(board, row, col, dir[0],  dir[1], player);
            count += countInDirection(board, row, col, -dir[0], -dir[1], player);
            if (count >= 4) return true;
        }
        return false;
    }

    static int countInDirection(char[][] board, int row, int col,
                                int dr, int dc, char player) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }

    // ─── Draw Detection ───────────────────────────────────────────────────────

    static boolean isBoardFull(char[][] board) {
        for (int c = 0; c < COLS; c++)
            if (board[0][c] == EMPTY) return false;
        return true;
    }

    // ─── Input ────────────────────────────────────────────────────────────────

    static int getColumn(Scanner sc, char[][] board, char player) {
        while (true) {
            System.out.print("Player " + player + ", choose a column (1-7): ");
            if (!sc.hasNextInt()) {
                System.out.println("  ✗ Please enter a number between 1 and 7.");
                sc.next(); // discard bad input
                continue;
            }
            int col = sc.nextInt() - 1; // convert to 0-based
            if (col < 0 || col >= COLS) {
                System.out.println("  ✗ Column must be between 1 and 7.");
                continue;
            }
            if (board[0][col] != EMPTY) {
                System.out.println("  ✗ That column is full. Choose another.");
                continue;
            }
            return col;
        }
    }

    static boolean askPlayAgain(Scanner sc) {
        System.out.print("Play again? (y/n): ");
        String input = sc.next().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");
    }

    // ─── Main Game Loop ───────────────────────────────────────────────────────

    static void playGame(Scanner sc) {
        char[][] board = createBoard();
        char current = P1;
        int[] scores = {0, 0}; // index 0 = P1, index 1 = P2

        System.out.println("╔══════════════════════╗");
        System.out.println("║    CONNECT  FOUR     ║");
        System.out.println("╚══════════════════════╝");
        System.out.println("  X = Player 1   O = Player 2");

        while (true) {
            printBoard(board);

            int col = getColumn(sc, board, current);
            int row = dropPiece(board, col, current);

            if (checkWin(board, row, col, current)) {
                printBoard(board);
                System.out.println("🎉 Player " + current + " wins!");
                if (current == P1) scores[0]++;
                else               scores[1]++;
                System.out.println("  Score → X: " + scores[0] + "  O: " + scores[1]);
                break;
            }

            if (isBoardFull(board)) {
                printBoard(board);
                System.out.println("It's a draw! Well played.");
                break;
            }

            // Switch players
            current = (current == P1) ? P2 : P1;
        }
    }

    // ─── Entry Point ──────────────────────────────────────────────────────────

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        do {
            playGame(sc);
        } while (askPlayAgain(sc));

        System.out.println("Thanks for playing. Goodbye!");
        sc.close();
    }
}
