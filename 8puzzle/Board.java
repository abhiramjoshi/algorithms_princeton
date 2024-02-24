import java.util.ArrayList;

import edu.princeton.cs.algs4.In;

public class Board {
    private final int[][] board;
    private final int n;
    private final int posBlank;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length == 0 || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("Input array must be a square 2D array.");
        }
        n = tiles.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = tiles[i][j];
            }
        }
        posBlank = findBlank();
    }

    // string representation of this board
    private int findBlank() {
        int n2 = this.n * this.n;
        for (int i = 0; i < n2; i++) {
            int row = i / this.n;
            int col = i % this.n;

            if (board[row][col] == 0) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        String boardRepr = String.format("%s\n", this.n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boardRepr += String.format("%2d ", board[i][j]);
            }
            boardRepr += "\n";
        }
        return boardRepr;
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        int n2 = this.n * this.n;
        for (int i = 0; i < n2; i++) {
            int row = i / this.n;
            int col = i % this.n;

            if (board[row][col] == 0) {
                continue;
            }
            if ((i + 1) % n2 != board[row][col]) {
                distance++;
            }
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int n2 = this.n * this.n;
        for (int i = 0; i < n2; i++) {
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
        int n2 = this.n * this.n;
        for (int i = 0; i < n2; i++) {
            int row = i / this.n;
            int col = i % this.n;

            if ((i + 1) % n2 != board[row][col]) {
                return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        int n2 = this.n * this.n;
        Board yBoard = (Board) y;
        if (yBoard.dimension() != this.dimension()) {
            return false;
        }
        try {
            for (int i = 0; i < n2; i++) {
                int row = i / this.n;
                int col = i % this.n;
                if (this.board[row][col] != yBoard.board[row][col]) {
                    return false;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborBoards = new ArrayList<>();
        int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Up, Down, Left, Right
        for (int[] move : moves) {
            int newRow = this.posBlank / n + move[0];
            int newCol = this.posBlank % n + move[1];

            if (isValidPosition(newRow, newCol)) {
                int[][] newBoard = copyBoard(this.board);
                swap(newBoard, posBlank / n, posBlank % n, newRow, newCol);
                neighborBoards.add(new Board(newBoard));
            }
        }
        return neighborBoards;
    }

    private void swap(int[][] swapBoard, int oldRow, int oldCol, int newRow, int newCol) {
        int oldValue = swapBoard[oldRow][oldCol];
        swapBoard[oldRow][oldCol] = swapBoard[newRow][newCol];
        swapBoard[newRow][newCol] = oldValue;
    }

    private int[][] copyBoard(int[][] originalBoard) {
        int[][] newBoard = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            System.arraycopy(originalBoard[i], 0, newBoard[i], 0, this.n);
        }
        return newBoard;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinBoard = copyBoard(board);
        int i = 0;
        while (i == this.posBlank || i + 1 == this.posBlank) {

            i += 2;
        }
        int randRowOne = i / n;
        int randColOne = i % n;
        int randRowTwo = (i + 1) / n;
        int randColTwo = (i + 1) % n;
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
        for (Board nN : neighbours) {
            System.out.print(nN.toString());
            System.out.println(nN.hamming());
            System.out.println(nN.manhattan());
            System.out.println(nN.isGoal());
        }
        System.out.print(initial.twin().toString());
        System.out.println(initial.dimension());
    }

}