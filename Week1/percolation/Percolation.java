import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
// import java.lang;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private int[] grid;
    private int[][] dir = {{-1, 0, 1, 0}, {0, 1, 0, -1}};
    private int gridDim; 
    private int numOfOpenSites;
    private boolean isPercolate = false;
    private int stateTop = 3;
    private int stateBot = 2;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        uf = new WeightedQuickUnionUF(n*n);
        grid = new int[n*n];
        gridDim = n;
        numOfOpenSites = 0;
    }

    private int convertRowColToIndex(int row, int col) {
        return (gridDim * (row - 1) + col) - 1;
    }

    private void validateRowCol(int row, int col) {
        if (row < 1 || row > gridDim || col < 1 || col > gridDim) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void printLastRowGrid() {
        for (int idLastRow = 1; idLastRow <= gridDim; idLastRow++) {
            int temp = grid[convertRowColToIndex(gridDim, idLastRow)];
            StdOut.printf("%d ", temp);
        }
        StdOut.println();
    }

    // open site(row, col) if it is not open already
    public void open(int row, int col) {
        validateRowCol(row, col);
        int cenCellIdx = convertRowColToIndex(row, col);
        if (grid[cenCellIdx] > 0) return;

        numOfOpenSites++;

        boolean isTop = false;
        boolean isBot = false;

        if (row == 1) {
            isTop = true;
        }
        if (row == gridDim) {
            isBot = true;
        } 
        grid[cenCellIdx] = 1;

        int tempIdx = cenCellIdx;
        for (int i = 0; i < 4; i++) {
            int newrow = row + dir[0][i];
            int newcol = col + dir[1][i];
            int newCellIdx = convertRowColToIndex(newrow, newcol);        
            
            // StdOut.printf("Check %d %d %d", newrow, newcol, newCellIdx);
            if ((newrow > 0) && (newrow <= gridDim) 
                && (newcol > 0) && (newcol <= gridDim) 
                && (grid[newCellIdx] > 0)) 
            {               
                int parentOfNewCellIdx = uf.find(newCellIdx);
                // check the status of the parent cell
                if (grid[parentOfNewCellIdx] == stateTop) {
                    isTop = true;
                } else if (grid[parentOfNewCellIdx] == stateBot) {
                    isBot = true;
                }            
                // tempIdx hold the index of the last parent, which can save time when union
                uf.union(tempIdx, parentOfNewCellIdx);
                tempIdx = parentOfNewCellIdx;
            }
        }

        if (isTop && isBot) isPercolate = true;
        // theState is Open = 1
        int theState = 1; 
        if (isBot) {
            theState = stateBot;
        }
        if (isTop) {
            theState = stateTop;            
        }
        grid[uf.find(tempIdx)] = theState;
        // StdOut.println();
        // printLastRowGrid();
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        return (grid[convertRowColToIndex(row, col)] > 0);
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        // if (row == 7 && col == 1) {
        //     int idx = convertRowColToIndex(row, col);
        //     int parentidx = uf.find(idx);
        //     int gridVal = grid[parentidx];
        //     StdOut.printf("idx = %d, parent = %d, grid = %d\n", idx, parentidx, gridVal);
        // }
        validateRowCol(row, col);
        return (grid[uf.find(convertRowColToIndex(row, col))] == stateTop);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolate;
    }

    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(1, 2);
        StdOut.println(percolation.isOpen(1, 2));
        StdOut.println(percolation.isFull(1, 1));
    }
}