import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation
 */
public class Percolation {

    private WeightedQuickUnionUF connection_grid;
    private boolean[][] open_grid;
    private int openSites;
    private int N;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N not a valid integer");
        }
        connection_grid = new WeightedQuickUnionUF(n * n + 2);
        // for (int i = 0; i < n; i++) {
        //     connection_grid.union(i + 1, 0);
        //     connection_grid.union((n * n) - i, (n * n) + 1);
        // }
        open_grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                open_grid[i][j] = false;
            }
        }
        openSites = 0;
        N = n;

    }

    public void open(int row, int col) {
        this.checkSiteValidity(row, col);
        if (this.isOpen(row, col)) {
            return;
        }
        row = zeroIndex(row);
        col = zeroIndex(col);

        open_grid[row][col] = true;
        this.openSites++;

        for (int i = -1; i < 2; i += 2) {
            try {
                if (this.isOpen(row + i + 1, col + 1)) {
                    this.union(row, col, row + i, col);
                }
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        for (int j = -1; j < 2; j += 2) {
            try {
                if (this.isOpen(row + 1, col + j + 1)) {
                    this.union(row, col, row, col + j);
                }
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        if (row == 0) {
            connection_grid.union(row*this.N+col+1, 0);
        }
        if (row == (this.N-1)) {
            connection_grid.union(row*this.N+col+1, (this.N*this.N) + 1 );
        }
    }

    private void union(int row_q, int col_q, int row_p, int col_p) {
        int q = row_q * this.N + col_q + 1;
        int p = row_p * this.N + col_p + 1;

        connection_grid.union(p, q);
    }

    // private boolean connected(int row_q, int col_q, int row_p, int col_p) {
    //     int q = row_q * this.N + col_q + 1;
    //     int p = row_p * this.N + col_p + 1;

    //     return (connection_grid.find(q) == connection_grid.find(p));
    // }

    public boolean isFull(int row, int col) {
        this.checkSiteValidity(row, col);
        row = zeroIndex(row);
        col = zeroIndex(col);

        if (!this.isOpen(row+1, col+1)) {
            return false;
        }
        int p = row * this.N + col + 1;

        return (connection_grid.find(p) == connection_grid.find(0));
    }

    public boolean isOpen(int row, int col) {
        this.checkSiteValidity(row, col);
        row = zeroIndex(row);
        col = zeroIndex(col);

        return this.open_grid[row][col];
    }

    public boolean percolates() {
        return (connection_grid.find(0) == connection_grid.find((this.N * this.N) + 1));
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    private int zeroIndex(int value) {
        value--;
        return value;
    }

    private void checkSiteValidity(int row, int col) {
        if (row < 1 || row > this.N) {
            throw new IllegalArgumentException(String.format("%s,%s: is not a valid site", row, col));
        }
        if (col < 1 || col > this.N) {
            throw new IllegalArgumentException(String.format("%s,%s: is not a valid site", row, col));
        }
        return;
    }

    // private void printGrid() {
    //     for (int i = 0; i < this.N; i++) {
    //         for (int j = 0; j < this.N; j++) {
    //             if (this.open_grid[i][j]) {
    //                 System.out.print(1);
    //             } else {
    //                 System.out.print(0);
    //             }
                
    //             System.out.print("\t");
    //         }
    //         System.out.println("");
    //     }
    // }

    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system

        Percolation perc = new Percolation(n);
        // System.out.println(perc.isFull(1, 1));
        // System.out.println(perc.isOpen(1, 1));
        // System.out.println(perc.connection_grid.find(n*n+1) == perc.connection_grid.find(n*n));
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            System.out.println(String.format("Opening i: %s, j: %s", i,j));
            System.out.println("System percolates:" + perc.percolates());
        }
    }
}
