import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private class Node {
        int priority;
        int distance;
        int movesMade;
        int[] move;
        Board board;
        Node prevBoard;

        public Node (int prevMoves, Board nodeBoard, Node prevBoard, int[] moveToBoard, String distanceFunc){
            this.board = nodeBoard;
            this.prevBoard = prevBoard;
            this.movesMade = prevMoves+1;
            this.move = moveToBoard;
            this.distance = calcDistance(distanceFunc);
            this.priority = movesMade + distance;
        }

        private int calcDistance(String distanceFunc) {
            if (this.move == null) {
                if (distanceFunc == "hamming") {
                    return board.hamming();
                } else {
                    return board.manhattan();
                }
            }
            if (distanceFunc == "hamming") {
                return intermediateHamming();
            }
            return intermediateManhattan();
        }

        private int intermediateManhattan() {
            int n = board.n;
            int blankRow = board.posBlank/n;
            int blankCol = board.posBlank%n;
            int valueRow = blankRow - this.move[0];
            int valueCol = blankCol - this.move[1];
            int value = board.board[valueRow][valueCol];

            int curr_distance = Math.abs(valueRow - ((value - 1) / n)) + Math.abs(valueCol - ((value - 1) % n));
            int prev_distance = Math.abs(blankRow - ((value - 1) / n)) + Math.abs(blankCol - ((value - 1) % n));
            
            return prevBoard.distance + (curr_distance-prev_distance);
        }

        private int intermediateHamming() {
            int n = board.n;
            int blankRow = board.posBlank/n;
            int blankCol = board.posBlank%n;
            int valueRow = blankRow + this.move[0];
            int valueCol = blankRow + this.move[1];
            int value = board.board[valueRow][valueCol];

            int curr_distance = (valueRow*n + valueCol == value) ? 1 : 0;
            int prev_distance = (blankRow*n + blankCol == value) ? 1 : 0;
            
            return prevBoard.distance + (curr_distance-prev_distance);
        }

        public int getPriority() {
            return this.priority;
        }

        public int getDistance() {
            return this.distance;
        }

        public int getMoves() {
            return this.movesMade;
        }

        public Board getBoard() {
            return this.board;
        }
    }

    private class NodeComparator implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            if (n1.priority > n2.priority) {
                return 1;
            }
            if (n1.priority > n2.priority) {
                return -1;
            }
            return 0;
        }
    }
    
    private MinPQ<Node> gameTree;
    private MinPQ<Node> gameTreeTwin;
    private boolean solvable;
    private int movesToSolve;
    private String distanceFunc;
    private Node solvedNode;

    public Solver(Board initial) {
        distanceFunc = null;
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        movesToSolve = -1;
        Node solvedNode = null;
        Board initialTwin = initial.twin();
        Node initialNode = new Node(-1, initial, null, null, this.distanceFunc);
        Node initialNodeTwin = new Node(-1, initialTwin, null, null, this.distanceFunc);
        gameTree = new MinPQ<>(new NodeComparator());
        gameTree.insert(initialNode); 
        gameTreeTwin = new MinPQ<>(new NodeComparator());
        gameTreeTwin.insert(initialNodeTwin); 
        
        while (!gameTree.isEmpty()) {
            Node currNode = gameTree.delMin();
            
            if (currNode.board.isGoal()) {
                solvable = true;
                solvedNode = currNode;
                this.movesToSolve = solvedNode.movesMade;
                break;
            } else {
                addNeighbours(currNode, gameTree);
            }

            Node currNodeTwin = gameTreeTwin.delMin();
            if (currNodeTwin.board.isGoal()) {
                solvable = false;
                solvedNode = null; 
                break;
            } else {
                addNeighbours(currNodeTwin, gameTreeTwin);
            }      
        }
    }

    private void addNeighbours(Node currNode, MinPQ<Node> tree) {
        for (int[] m: new int[][]{{0,1}, {0,-1},{1,0}, {-1,0}}) {
            if (Arrays.equals(negativeArray(currNode.move), m)) {
                continue;
            }
            Node addNode = getNeighbour(currNode, m);
            if (addNode != null) {
                tree.insert(addNode);
            }
        }

    }

    private Node getNeighbour(Node currNode, int[] m) {
        int newRow = currNode.board.posBlank / currNode.board.n + m[0];
        int newCol = currNode.board.posBlank % currNode.board.n + m[1];

        if (!currNode.board.isValidPosition(newRow, newCol)) {
            return null;
        }
        int[][] newBoard = currNode.board.copyBoard(currNode.board.board);
        currNode.board.swap(newBoard, currNode.board.posBlank / currNode.board.n, currNode.board.posBlank % currNode.board.n, newRow, newCol);
        Node neighbour = new Node(currNode.movesMade, new Board(newBoard), currNode, m, this.distanceFunc);

        return neighbour;
    }

    private int[] negativeArray(int[] array) {
        if (array == null) {
            return null;
        }

        int[] negArray = new int[array.length];
        for (int i = 0; i<array.length; i++) {
            negArray[i] = -1*array[i];
        }
        return negArray;
    }

    public int moves() {
        return this.movesToSolve;
    }

    public boolean isSolvable() {
        return this.solvable;
    }

    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        ArrayList<Board> solutionBranch = new ArrayList<>();
        while (this.solvedNode != null) {
            solutionBranch.add(0, this.solvedNode.board);
            this.solvedNode = this.solvedNode.prevBoard;
            movesToSolve++;
        }
        return solutionBranch;
    }
    
}
