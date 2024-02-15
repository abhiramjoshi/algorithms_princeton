import java.util.HashMap;
/**
 * Percolation
 */
public class PercolationL {

    private int[][] connection_grid;
    private int[][] open_grid;
    private int openSites;
    private int N;
    private HashMap<Integer, Integer> treeSizes;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N not a valid integer");
        }
        treeSizes = new HashMap<>();
        connection_grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                connection_grid[i][j] = i*n + j;
                treeSizes.put(i*n + j, 1);
            }
        }
        open_grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                open_grid[i][j] = 0;
            }
        }
        openSites = 0;
        N = n;
        
    }

    public void open(int row, int col) {
        this.checkSiteValidity(row, col);
        row = zeroIndex(row);
        col = zeroIndex(col);

        open_grid[row][col] = 1;
        this.openSites++;

        for (int i = -1; i<2; i += 2) {
            try{
                if (this.isOpen(row+i+1, col+1)) {
                    this.union(row, col, row+i, col);
                }
            } catch (IllegalArgumentException e) {
                continue;
            }
        }
        for (int j = -1; j<2; j += 2) {
            try {
                if (this.isOpen(row+1, col+j+1)) {
                    this.union(row, col, row, col+j);
                }   
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void union(int row_q, int col_q, int row_p, int col_p) {
        int q = this.root(row_q, col_q);
        int p = this.root(row_p, col_p);
        
        if (treeSizes.get(q) >= treeSizes.get(p)) {
            connection_grid[row_p][col_p] = q;
        } else {
            connection_grid[row_q][col_q] = p;
        }
        
    }

    private int root(int row, int col) {
        while ((row*this.N + col) != connection_grid[row][col]) {
            int p = connection_grid[row][col];
            row = p / this.N;
            col = p % this.N;
        }
        treeSizes.put(null, null)
        return connection_grid[row][col];
    }

    private boolean connected(int row_q, int col_q, int row_p, int col_p) {
        if (this.root(row_q,col_q) == this.root(row_p, col_p)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull(int row, int col) {
        this.checkSiteValidity(row, col);
        row = zeroIndex(row);
        col = zeroIndex(col);

        if (connected(row, col, -1, -1)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isOpen(int row, int col) {
        this.checkSiteValidity(row, col);
        row = zeroIndex(row);
        col = zeroIndex(col);

        if (this.open_grid[row][col] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    // public boolean percolates() {
    //     if (connected(-1, -1, n, n))
    // }

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

    public static void main(String[] args) {
        Percolation grid = new Percolation(5);
        //grid.union(1, 1, 3, 3);
        //grid.union(3, 3, 4, 1);
        grid.open(1,1);
        grid.open(5,1);
        grid.open(5,2);
        grid.open(4,2);
        grid.open(3,2);
        grid.union(2, 2, 2, 1);
        System.out.println(grid.root(1,1));
        for (int i = 0; i < grid.N; i++) {
            for (int j = 0; j < grid.N; j++) {
                System.out.print(grid.connection_grid[i][j]);
                System.out.print("\t");
            }
            System.out.println("");
        }
        for (int i = 0; i < grid.N; i++) {
            for (int j = 0; j < grid.N; j++) {
                System.out.print(grid.open_grid[i][j]);
                System.out.print("\t");
            }
            System.out.println("");
        }
        System.out.println(grid.numberOfOpenSites());
    }
}