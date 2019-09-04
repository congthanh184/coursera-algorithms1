import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    //find a solution to the initial board (using the A* algorithm)
    private SearchNode goalNode;
    private Stack<Board> answer;
    private boolean solvable = false;

    public Solver(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pq2 = new MinPQ<SearchNode>();
        boolean hasAnswer = false;

        SearchNode initNode = new SearchNode(null, 0, initial);
        SearchNode twinNode = new SearchNode(null, 0, initial.twin());
        pq.insert(initNode);
        pq2.insert(twinNode);
        // pq.insert(new SearchNode(null, 0, initial));
        // Board board = new Board(new int[0][0]);
        // board = initial.twin();
        // StdOut.println(board.getClass());
        // StdOut.println(board.toString());
        // StdOut.printf("%d %d %d %d %d\n", board.hamming(), board.manhattan(), board.zero_index, board.zero_row, board.zero_col);
        // pq.insert(new SearchNode(null, 0, board));
        // StdOut.println(board.equals(initial));

        // for (Board b : board.neighbors()) {
        //     StdOut.println(b.toString());
        // }

        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            SearchNode node2 = pq2.delMin();
            // StdOut.println(node.board);

            if (node.board.isGoal()) {
                this.goalNode = node;
                hasAnswer = true;
                break;              
            }

            if (node2.board.isGoal()) {
                this.goalNode = node2;
                hasAnswer = true;
                break;              
            }

            for (Board neighbor : node.board.neighbors()) {
                if (node.prev == null || (node.prev != null && !neighbor.equals(node.prev.board))) {
                    pq.insert(new SearchNode(node, node.costMove+1, neighbor));
                }
            }
            for (Board neighbor : node2.board.neighbors()) {
                if (node2.prev == null || (node2.prev != null && !neighbor.equals(node2.prev.board))) {
                    pq2.insert(new SearchNode(node2, node2.costMove+1, neighbor));
                }
            }
        }

        this.solvable = false;
        if (hasAnswer) {            
            SearchNode curNode = this.goalNode;
            answer = new Stack<Board>();
            StdOut.println("Has solution");
            while (true) {
                answer.push(curNode.board);
                if (curNode.prev == null) break;
                else curNode = curNode.prev;
            }           
            
            if (curNode.board.equals(initial)) {
                this.solvable = true;
            }
        }
    }           
    
    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode prev;
        private Board board;
        private int costMove;
        private int thisManCost;
        private int boardHamCost, boardManCost;

        public SearchNode(SearchNode prev, int costMove, Board board) {
            this.prev = prev;
            this.costMove = costMove;
            this.board = board;
            this.thisManCost = this.costMove + this.board.manhattan();
            this.boardHamCost = this.board.hamming();
            this.boardManCost = this.board.manhattan();
        }

        public int compareTo(SearchNode that) {
            // int thisManCost = this.costMove + this.board.manhattan();
            int thatManCost = that.costMove + that.board.manhattan();
            // StdOut.println(thisManCost);
            // StdOut.println(thatManCost);
            if (thisManCost > thatManCost) return +1;
            else if (thisManCost < thatManCost) return -1;
            // else if (this.costMove > that.costMove) return +1;
            // else if (this.costMove < that.costMove) return -1;
            else if (boardManCost > that.board.manhattan()) return +1;
            else if (boardManCost < that.board.manhattan()) return -1;
            else if (this.boardHamCost > that.board.hamming()) return +1;
            return -1;
        }

    }
    // is the initial board solvable?
    public boolean isSolvable() {
        return this.solvable;
    }            
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (this.solvable) {
            return this.goalNode.costMove;
        }
        return -1;
    } 

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.solvable) {
            return this.answer;
        }
        return null;
    }   

    // solve a slider puzzle (given below)       
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();

        // int temp = blocks[0][0];
        // blocks[0][0] = blocks[2][1];
        // blocks[2][1] = temp;
        Board initial = new Board(blocks);
        // initial = initial.twin();
        // StdOut.println(initial.toString());

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}