/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Description: Board
 **************************************************************************** */

package dima.java.puzzlesolver;

import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int[][] board;
    private int hammingDist, manhattanDist, n, zeroRow, zeroCol;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new java.lang.IllegalArgumentException();
        }
        n = blocks.length;
        board = new int[n][n];
        hammingDist = 0;
        manhattanDist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] < 0 || blocks[i][j] >= n * n) {
                    throw new IllegalArgumentException();
                }
                else {
                    board[i][j] = blocks[i][j];
                    int currentVal = blocks[i][j] - 1;
                    int targetVal = i * n + j;
                    if (targetVal != currentVal && targetVal < n * n && currentVal != -1) {
                        manhattanDist += Math.abs(targetVal / n - currentVal / n) +
                                Math.abs(targetVal % n - currentVal % n);
                        hammingDist++;
                    }
                    if (blocks[i][j] == 0) {
                        zeroRow = i;
                        zeroCol = j;
                    }
                }
            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hammingDist;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattanDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDist == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        // exchange blocks in first or second row, depending on where the zero block is
        if (board[0][0] != 0 && board[0][1] != 0) {
            exchangeBlocks(0, 0, 0, 1);
            Board twinBoard = new Board(board);
            exchangeBlocks(0, 1, 0, 0);
            return twinBoard;
        }
        else {
            exchangeBlocks(1, 0, 1, 1);
            Board twinBoard = new Board(board);
            exchangeBlocks(1, 1, 1, 0);
            return twinBoard;
        }
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || y.getClass() != this.getClass() || !this.toString().equals(y.toString())) {
            return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighboringBoards = new Stack<>();
        if (zeroRow > 0) {
            exchangeBlocks(zeroRow, zeroCol, zeroRow - 1, zeroCol);
            neighboringBoards.push(new Board(board));
            exchangeBlocks(zeroRow - 1, zeroCol, zeroRow, zeroCol);
        }
        if (zeroRow < n - 1) {
            exchangeBlocks(zeroRow, zeroCol, zeroRow + 1, zeroCol);
            neighboringBoards.push(new Board(board));
            exchangeBlocks(zeroRow + 1, zeroCol, zeroRow, zeroCol);
        }
        if (zeroCol > 0) {
            exchangeBlocks(zeroRow, zeroCol, zeroRow, zeroCol - 1);
            neighboringBoards.push(new Board(board));
            exchangeBlocks(zeroRow, zeroCol - 1, zeroRow, zeroCol);
        }
        if (zeroCol < n - 1) {
            exchangeBlocks(zeroRow, zeroCol, zeroRow, zeroCol + 1);
            neighboringBoards.push(new Board(board));
            exchangeBlocks(zeroRow, zeroCol + 1, zeroRow, zeroCol);
        }
        return neighboringBoards;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        boardString.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardString.append(String.format("%2d ", board[i][j]));
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

    // helper function to execute blocks exchange
    private void exchangeBlocks(int rowA, int colA, int rowB, int colB) {
        int swap = board[rowA][colA];
        board[rowA][colA] = board[rowB][colB];
        board[rowB][colB] = swap;
    }

    // unit tests
    public static void main(String[] args) {
    }
}





























































