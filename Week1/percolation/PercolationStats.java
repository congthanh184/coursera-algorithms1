import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    // private int[] fractionThreshold;
    private double[] percolationThreshold;
    private int trials;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) throw new IllegalArgumentException();
        // fractionThreshold = new int[trials];
        percolationThreshold = new double[trials];
        this.trials = trials;

        // StdRandom.setSeed(184);
        Percolation percolation;

        for (int idTrials = 0; idTrials < trials; idTrials++) {
            // StdOut.printf("Iteration %d\n", idTrials);
            percolation = new Percolation(n);
            do {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                percolation.open(row, col);
            } while (!percolation.percolates());

            // fractionThreshold[idTrials] = percolation.numberOfOpenSites();
            percolationThreshold[idTrials] = (double) percolation.numberOfOpenSites() / (n*n);
            percolation = null;
        }
    }

    public double mean() {
        return StdStats.mean(percolationThreshold);
    }

    public double stddev() {
        return StdStats.stddev(percolationThreshold);
    }

    public double confidenceLo() {
        double xMean = mean();
        double stds = stddev();
        return xMean - (1.96*stds) / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        double xMean = mean();
        double stds = stddev();
        return xMean + (1.96*stds) / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        StdOut.printf("%d %d\n", n, trials);
        PercolationStats stats = new PercolationStats(n, trials);
        StdOut.printf("mean = %f\nstddev = %f\n95 confidence interval = %f, %f\n", 
            stats.mean(), stats.stddev(), stats.confidenceLo(), stats.confidenceHi());
    }
}