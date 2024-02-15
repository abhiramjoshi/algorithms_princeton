import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
//import java.io.FileWriter;
//import java.io.IOException;  

public class PercolationStats {

    // Percolation percolation;
    private int trials;
    private int N;
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid value of N");
        }
        if (trials <= 0) {
            throw new IllegalArgumentException("Invalid value of trials");
        }
        this.trials = trials;
        this.N = n;
        this.results = this.performAllTrials();
    }

    private double performTrial(int i) {
        // File percolation_data = new
        // File(String.format("data\\percolation_data_%s.txt", i));
        Percolation percolation = new Percolation(this.N);
        // try {
        // FileWriter data_writer = new
        // FileWriter(String.format("data\\percolation_data_%s.txt", i));
        // data_writer.write(this.N + "\n");

        while (!percolation.percolates()) {
            int site = StdRandom.uniformInt(this.N * this.N);
            int row = site / this.N + 1;
            int col = site % this.N + 1;
            // if (!percolation.isOpen(row, col)) {
            // data_writer.write(String.format("%s %s\n", row, col));
            // }
            percolation.open(row, col);
        }

        // percolation.printGrid();
        // data_writer.close();
        // } catch (IOException e) {
        //
        // }
        return ((double) percolation.numberOfOpenSites()) / (this.N * this.N);
    }

    private double[] performAllTrials() {
        double[] allResults = new double[this.trials];
        for (int i = 0; i < this.trials; i++) {
            allResults[i] = this.performTrial(i);
        }

        return allResults;
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = this.mean();
        double std = this.stddev();
        double lo = mean - ((1.96 * std)) / Math.sqrt(this.trials);
        return lo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = this.mean();
        double std = this.stddev();
        double hi = mean + ((1.96 * std)) / Math.sqrt(this.trials);
        return hi;
    }

    // test client (see below)
    public static void main(String[] args) {
        // int n = 200;
        // int T = 1000;
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats percolation_test = new PercolationStats(n, T);
        System.out.println("mean\t\t\t= " + percolation_test.mean());
        System.out.println("stddev\t\t\t= " + percolation_test.stddev());
        System.out.println("95% confidence interval\t= [" + percolation_test.confidenceLo() + ", "
                + percolation_test.confidenceHi() + "]");
    }

}