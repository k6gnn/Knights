import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    private static int N; 
    private static int[][] board;
    private static final int[] moveX = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] moveY = {1, 2, 2, 1, -1, -2, -2, -1};
    private static int trials = 0;
    private static StringBuilder traceLog = new StringBuilder();
    private static boolean printOnScreen;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter board size (N x N): ");
        N = scanner.nextInt();
        board = new int[N][N];
        
        System.out.print("Enter starting X coordinate (1-based index): ");
        int startX = scanner.nextInt() - 1;
        System.out.print("Enter starting Y coordinate (1-based index): ");
        int startY = scanner.nextInt() - 1;
        
        
        System.out.print("Do you want to print the output on the screen? (yes/no): ");
        printOnScreen = scanner.next().equalsIgnoreCase("yes");

        for (int[] row : board) Arrays.fill(row, -1); 
        board[startX][startY] = 0; 

        traceLog.append("PART 1. Data\n")
                .append("1) Board: " + N + "x" + N + "\n")
                .append("2) Initial position: X=" + (startX + 1) + ", Y=" + (startY + 1) + ", L=1\n\nPART 2. Trace\n");

        if (solveKnightTour(startX, startY, 1)) {
            traceLog.append("\nPART 3. Results\n1) Path is found. Trials=" + trials + "\n");
            printBoard();
        } else {
            traceLog.append("\nPART 3. Results\nNo solution found. Trials=" + trials + "\n");
        }

        writeToFile("out-short.txt", traceLog.toString());
        writeToFile("out-long.txt", traceLog.toString());

        if (printOnScreen) {
            System.out.println(traceLog);
        }
        scanner.close();
    }

    private static boolean solveKnightTour(int x, int y, int moveNum) {
        if (moveNum == N * N) return true;
        
        for (int i = 0; i < 8; i++) {
            int nextX = x + moveX[i];
            int nextY = y + moveY[i];
            
            if (isValidMove(nextX, nextY)) {
                board[nextX][nextY] = moveNum;
                trials++;
                traceLog.append(String.format("%5d) R%d. U=%d, V=%d. L=%d. Free. BOARD[%d,%d]:=%d.\n",
                        trials, i + 1, nextX + 1, nextY + 1, moveNum + 1, nextX + 1, nextY + 1, moveNum + 1));
                
                if (solveKnightTour(nextX, nextY, moveNum + 1)) return true;
                
                board[nextX][nextY] = -1; 
                traceLog.append(String.format("Backtracking from (%d, %d).\n", nextX + 1, nextY + 1));
            }
        }
        return false;
    }

    private static boolean isValidMove(int x, int y) {
        return (x >= 0 && x < N && y >= 0 && y < N && board[x][y] == -1);
    }

    private static void printBoard() {
        traceLog.append("2) Path graphically:\n\n");
        for (int i = N - 1; i >= 0; i--) {
            for (int j = 0; j < N; j++) {
                traceLog.append(String.format("%3d ", board[j][i] + 1));
            }
            traceLog.append("\n");
        }
    }

    private static void writeToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename);
        }
    }
}
