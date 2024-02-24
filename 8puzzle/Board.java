import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] board;
    private int n;
    private int posBlank;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length == 0 || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("Input array must be a square 2D array.");
        }
        board = tiles;
        n = tiles.length;
        posBlank = findBlank();
    }
                                           
    // string representation of this board
    private int findBlank() {
        int n_2 = this.n*this.n;
        for (int i = 0; i < n_2; i++) {
            int row = i / this.n;
            int col = i % this.n;
            
            if (board[row][col] == 0) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        String board_repr = String.format("%s\n", this.n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board_repr += String.format("%2d ", board[i][j]);
            }
            board_repr += "\n";
        }
        return board_repr;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        int n_2 = this.n*this.n;
        for (int i = 0; i < n_2; i++) {
            int row = i / this.n;
            int col = i % this.n;
            
            if (board[row][col] == 0) {
                continue;
            }
            if ((i+1) % n_2 != board[row][col]) {
                distance++;
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int n_2 = this.n*this.n;
        for (int i = 0; i < n_2; i++) {
            int row = i / this.n;
            int col = i % this.n;
            
            int x = this.board[row][col];
            
            if (x == 0) {
                continue;
            }
            distance += Math.abs(row - ((x - 1) / this.n));
            distance += Math.abs(col - ((x - 1) % this.n));
            
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int n_2 = this.n*this.n;
        for (int i = 0; i < n_2; i++) {
            int row = i / this.n;
            int col = i % this.n;
            
            if ((i+1) % n_2 != board[row][col]) {
                return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        int n_2 = this.n*this.n;
        Board y_board = (Board) y;
        for (int i = 0; i < n_2; i++) {
            int row = i / this.n;
            int col = i % this.n;
            if (this.board[row][col] != y_board.board[row][col]){
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborBoards = new ArrayList<>();
        int[][] moves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right
        for (int[] move : moves) {
            int newRow = this.posBlank / n + move[0];
            int newCol = this.posBlank % n + move[1];

            if (isValidPosition(newRow, newCol)) {
                int[][] newBoard = copyBoard(board);
                swap(newBoard, posBlank / n, posBlank % n, newRow, newCol);
                neighborBoards.add(new Board(newBoard));
            }
        }
        return neighborBoards;
    }

    private void swap(int[][] board, int oldRow, int oldCol, int newRow, int newCol) {
        int oldValue = board[oldRow][oldCol];
        board[oldRow][oldCol] = board[newRow][newCol];
        board[newRow][newCol] = oldValue;
    }

    private int[][] copyBoard(int[][] board) {
        int[][] new_board = new int[this.n][this.n];
        
        for (int i = 0; i < this.n; i++) {
            System.arraycopy(board[i], 0, new_board[i], 0, this.n);
        }
        return new_board;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBoard = copyBoard(board);
        int randRowOne = StdRandom.uniformInt(this.n*this.n) / n;
        int randColOne = StdRandom.uniformInt(this.n*this.n) % n;
        while ( (posBlank/n == randRowOne) && (posBlank%n == randColOne)) {
            randRowOne = StdRandom.uniformInt(this.n*this.n) / n;
            randColOne = StdRandom.uniformInt(this.n*this.n) % n;
        }
        int randRowTwo = StdRandom.uniformInt(this.n*this.n) / n;
        int randColTwo = StdRandom.uniformInt(this.n*this.n) % n;
        while ( ((randRowOne == randRowTwo) && (randColOne == randColTwo)) || ((posBlank/n == randRowTwo) && (posBlank%n == randColTwo))) {
            randRowTwo = StdRandom.uniformInt(this.n*this.n) / n;
            randColTwo = StdRandom.uniformInt(this.n*this.n) % n;
        }
        swap(twinBoard, randRowOne, randColOne, randRowTwo, randColTwo);
        return new Board(twinBoard);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        System.out.print(initial.toString());
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());
        System.out.println(initial.isGoal());
        Iterable<Board> neighbours = initial.neighbors();
        for (Board N : neighbours) {
            System.out.print(N.toString());
            System.out.println(N.hamming());
            System.out.println(N.manhattan());
            System.out.println(N.isGoal());
        }
        System.out.print(initial.twin().toString());
        System.out.println(initial.dimension());
    }

}