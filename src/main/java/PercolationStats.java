import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final static double MAGIC_CONSTANT = 1.96;

    private double[] results;
    final private int trials;

    public PercolationStats(int n, int trials) {
        this.trials = trials;
        results = new double[trials];
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < trials; i++) {
            run(n, i);
        }
    }

    private void run(int n, int trial) {
        Percolation percolation = new Percolation(n);
        while (!percolation.percolates()) {
            percolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
        }
        results[trial] = percolation.numberOfOpenSites() * 1. / (n * n);
    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return mean() - MAGIC_CONSTANT * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() {

        return mean() + MAGIC_CONSTANT * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}