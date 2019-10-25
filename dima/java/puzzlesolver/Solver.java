/* *****************************************************************************
 *  Name: Dumitru Hanciu
 *  Description: Solver
 **************************************************************************** */

package dima.java.puzzlesolver;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;
import java.util.Comparator;

public class Solver {
    private int moves;
    private boolean solvable;
    private Stack<Board> solutionQ;

    private class NodesComparator implements Comparator<Node> {

        public int compare(Node n2, Node n1) {
            if (n1.pri == n2.pri) {
                return n1.mv - n2.mv;
            }
            else {
                return n1.pri - n2.pri;
            }
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        moves = 0;
        solvable = false;
        solutionQ = null;
        MinPQ<Node> brdQ = new MinPQ<>(
                (Node n1, Node n2) -> n1.pri == n2.pri ? n2.mv - n1.mv : n1.pri - n2.pri);
        MinPQ<Node> brdQTwin = new MinPQ<>(
                (Node n1, Node n2) -> n1.pri == n2.pri ? n2.mv - n1.mv : n1.pri - n2.pri);

        Board twinBoard = initial.twin();
        Node searchNode = new Node(0, initial.manhattan(), initial, null);
        Node searchNodeTw = new Node(0, twinBoard.manhattan(), twinBoard, null);
        Node prevNode = null;
        Node prevNodeTw = null;
        brdQ.insert(searchNode);
        brdQTwin.insert(searchNodeTw);
        while (!searchNode.board.isGoal() && !searchNodeTw.board.isGoal()) {
            moves++;
            for (Board board : brdQ.delMin().board.neighbors()) {
                if (prevNode == null || !board.equals(prevNode.board)) {
                    brdQ.insert(
                            new Node(searchNode.mv + 1, board.manhattan() + searchNode.mv + 1,
                                     board, searchNode));
                }
            }
            for (Board boardTw : brdQTwin.delMin().board.neighbors()) {
                if (prevNodeTw == null || !boardTw.equals(prevNodeTw.board)) {
                    brdQTwin.insert(new Node(searchNodeTw.mv + 1, boardTw.manhattan() +
                            searchNodeTw.mv + 1, boardTw, searchNodeTw));
                }
            }
            searchNode = brdQ.min();
            prevNode = searchNode.previous;
            searchNodeTw = brdQTwin.min();
            prevNodeTw = searchNodeTw.previous;
            moves = searchNode.mv;
        }
        if (searchNode.board.isGoal()) {
            solutionQ = new Stack<>();
            solvable = true;
            while (searchNode != null) {
                solutionQ.push(searchNode.board);
                searchNode = searchNode.previous;
            }
        }
        else {
            moves = -1;
        }
    }

    private void print(Node node) {
        StdOut.println("Priority: " + Integer.toString(node.mv + node.board.manhattan()));
        StdOut.println("Moves for BRD: " + node.mv);
        StdOut.println("Manhattan: " + node.board.manhattan());
        StdOut.println("Hamming: " + node.board.hamming());
        StdOut.println("Moves: " + moves);
        StdOut.println(node.board.toString());
    }

    private class Node {
        private int mv;
        private int pri;
        private Board board;
        private Node previous;

        Node(int moves, int priority, Board board, Node previous) {
            this.mv = moves;
            this.pri = priority;
            this.board = board;
            this.previous = previous;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionQ;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        if (args[1].equals("9")) {
            File folder = new File("D:\\Stuff\\Dev\\Java\\Assignments\\Forth\\8puzzle");
            File[] listOfFiles = folder.listFiles();
            if (listOfFiles != null) {
                for (int k = 0; k < listOfFiles.length; k++) {
                    String fil = listOfFiles[k].getName();
                    if (listOfFiles[k].isFile() && fil.endsWith(".txt") && fil
                            .startsWith("puzzle")) {
                        // StdOut.println(fil);
                        // create initial board from file
                        In in = new In(fil);
                        int n = in.readInt();
                        int[][] blocks = new int[n][n];
                        for (int i = 0; i < n; i++)
                            for (int j = 0; j < n; j++)
                                blocks[i][j] = in.readInt();
                        Board initial = new Board(blocks);

                        // solve the puzzle
                        Solver solver = new Solver(initial);

                        // print solution to standard output
                        if (!solver.isSolvable())
                            StdOut.println("No solution possible");
                        else {
                            StdOut.println(fil + " : " + "" + solver.moves());
                        }
                    }
                }
            }
        }
        else {
            // create initial board from file
            In in = new In(args[0] + ".txt");
            int n = in.readInt();
            int[][] blocks = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    blocks[i][j] = in.readInt();
            Board initial = new Board(blocks);

            // solve the puzzle
            Solver solver = new Solver(initial);

            // print solution to standard output
            if (!solver.isSolvable()) {
                StdOut.println("No solution possible");
            }
            else {
                for (Board board : solver.solution())
                    StdOut.println(board);
                StdOut.println("Minimum number of moves = " + solver.moves());
            }
        }
    }
}
