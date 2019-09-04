import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
// import java.util.Iterator;
// import java.util.NoSuchElementException;

public class Board {
    private char[] board;
    private int n;
    private int zero_row, zero_col, zero_index;
    private int hammingCost = 0;
    private int manhattanCost = 0;    

    // construct a board from an n-by-n array of blocks 
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) { 
        this.n = blocks.length;

        board = new char[this.n * this.n];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int arrayIdx = i*this.n + j;
                board[arrayIdx] = (char)blocks[i][j];
                if (blocks[i][j] == 0) {
                    this.zero_row = i;
                    this.zero_col = j;
                    this.zero_index = arrayIdx;
                }                
                else {
                    if (blocks[i][j] != arrayIdx + 1) this.hammingCost++;    // out of place, arrayIdx from 0 -> (n-1)
                    int expectRow = this.toRow(blocks[i][j] - 1);
                    int expectCol = this.toCol(blocks[i][j] - 1);
                    this.manhattanCost += Math.abs(i - expectRow) + Math.abs(j - expectCol);
                } 
                    
            }
        }

    }           

    private Board(char[] board, int n, int zero_index, int newMan, int newHam) {
        this.board = board;
        this.n = n;
        this.hammingCost = newHam;
        this.manhattanCost = newMan;
        this.zero_index = zero_index;
        this.zero_row = toRow(zero_index);
        this.zero_col = toCol(zero_index);
    }

    private int toRow(int index) {
        return (index / this.n);
    }

    private int toCol(int index) {
        return (index % this.n);
    }

    private int toIndex(int row, int col) {
        return row * this.n + col;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of blocks out of place
    public int hamming() {
        return this.hammingCost;
    }   

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return this.manhattanCost;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hammingCost == 0;
    }

    // swap and return a new blocks
    // as swap is used every time we need new board
    // so better return a whole board
    private int manhattanCostAt(char value, int i, int j) {
        if (value == 0) return 0;
        int expectRow = this.toRow(value - 1);
        int expectCol = this.toCol(value - 1);
        return Math.abs(i - expectRow) + Math.abs(j - expectCol);
    }

    private int hammingAt(int value, int idx) {
        if (value == 0) return 0;
        if (value != idx+1) return +1;
        return 0;
    }

    private Board swap(int idx1, int idx2) {
        // int[][] newBlocks = new int[this.n][this.n];
        // for (int i = 0; i < this.n; i++) {
        //     for (int j = 0; j < this.n; j++) {
        //         newBlocks[i][j] = this.board[this.toIndex(i, j)];
        //     }
        // }

        int row1 = this.toRow(idx1); int col1 = this.toCol(idx1);
        int row2 = this.toRow(idx2); int col2 = this.toCol(idx2);

        int oldManCost1 = manhattanCostAt(this.board[idx1], row1, col1);
        int oldManCost2 = manhattanCostAt(this.board[idx2], row2, col2);
        int newManCost1 = manhattanCostAt(this.board[idx2], row1, col1);
        int newManCost2 = manhattanCostAt(this.board[idx1], row2, col2);

        int oHamCost1 = hammingAt(this.board[idx1], idx1);
        int oHamCost2 = hammingAt(this.board[idx2], idx2);
        int nHamCost1 = hammingAt(this.board[idx2], idx1);
        int nHamCost2 = hammingAt(this.board[idx1], idx2);

        char[] newBoard = this.board.clone();
        char temp = newBoard[idx1];
        newBoard[idx1] = newBoard[idx2];
        newBoard[idx2] = temp;
        int newMan = this.manhattanCost - oldManCost1 - oldManCost2 + newManCost1 + newManCost2;
        int newHam = this.hammingCost - oHamCost1 - oHamCost2 + nHamCost1 + nHamCost2;
        int newZeroIdx = this.zero_index;
        if (newBoard[idx1] == 0) newZeroIdx = idx1;
        else if (newBoard[idx2] == 0) newZeroIdx = idx2;

        Board swapBoard = new Board(newBoard, this.n, newZeroIdx, newMan, newHam);
        return swapBoard;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        while (true) {
            int idx1 = StdRandom.uniform(this.board.length);
            int idx2 = StdRandom.uniform(this.board.length);
            if (idx1 != idx2 && idx1 != this.zero_index && idx2 != this.zero_index) {
                // int[][] newBlocks = this.swap(idx1, idx2);
                // char[] newBoard = this.board.clone();
                // char temp = newBoard[idx1];
                // newBoard[idx1] = newBoard[idx2];
                // newBoard[idx2] = temp;
                
                // Board newBlocks = new Board(new int[0][0]);
                // newBlocks.board = newBoard;
                // return newBlocks;
                // int[][] newBlocks = new int[this.n][this.n];
                // for (int i = 0; i < this.n; i++) {
                //     for (int j = 0; j < this.n; j++) {
                //         int id = toIndex(i, j);
                //         newBlocks[i][j] = this.board[id];
                //     }
                // }
                // int row1 = this.toRow(idx1); int col1 = this.toCol(idx1);
                // int row2 = this.toRow(idx2); int col2 = this.toCol(idx2);

                // int temp = newBlocks[row1][col1];
                // newBlocks[row1][col1] = newBlocks[row2][col2];
                // newBlocks[row2][col2] = temp;
                return swap(idx1, idx2);
                // return new Board(newBlocks);
            }
        }
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        Board that = (Board) other;
        if (that.board.length != this.board.length) return false;
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] != that.board[i]) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> validNeighbors = new Stack<Board>();
        int[][] move = new int[][] {{-1, 1, 0, 0}, {0, 0, -1, 1}};
        
        for (int i = 0; i < 4; i++ ) {
            int newRow = zero_row + move[0][i];
            int newCol = zero_col + move[1][i];
            if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n) {
                int idxSwap = toIndex(newRow, newCol);
                validNeighbors.push( swap(zero_index, idxSwap));
            }                
        }
        return validNeighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                // StdOut.println((int)this.board[this.toIndex(i, j)]);
                s.append(String.format("%2d ", (int)this.board[this.toIndex(i, j)]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // StdOut.println(initial.toString());
        // StdOut.println(initial.manhattan());
        // StdOut.println(initial.hamming());
        Board currentBoard = initial;
        for (int i = 0; i < 20; i++) {
            StdOut.println(currentBoard.hamming());
            StdOut.println(currentBoard.toString());

            Stack<Board> stack = (Stack<Board>)currentBoard.neighbors();
            currentBoard = stack.pop();
            currentBoard = stack.pop();
            currentBoard = stack.pop();
        }
        // for (Board board : initial.neighbors()) {
        //     StdOut.println(board.toString());
        // }

        // Board twin = initial.twin();
        // StdOut.println(twin.toString());
    }
}