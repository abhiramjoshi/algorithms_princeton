import java.util.ArrayList;
import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private class Node {
        int priority;
        int distance;
        int movesMade;
        Board board;
        Node prevBoard;

        public Node (int prevMoves, Board nodeBoard, Node prevBoard, String distanceFunc){
            this.board = nodeBoard;
            this.prevBoard = prevBoard;
            this.movesMade = prevMoves+1;
            this.distance = calcDistance(distanceFunc);
            this.priority = movesMade + distance;
        }

        private int calcDistance(String distanceFunc) {
            if (this.prevBoard == null) {
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
            return board.manhattan();
        }

        private int intermediateHamming() {
            return board.hamming();
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
    
    private boolean solvable;
    private int movesToSolve;
    private String distanceFunc;
    private Node solvedNode;

    public Solver(Board initial) {
        this.distanceFunc = null;
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.movesToSolve = -1;
        this.solvedNode = null;
        Board initialTwin = initial.twin();
        Node initialNode = new Node(-1, initial, null, this.distanceFunc);
        Node initialNodeTwin = new Node(-1, initialTwin, null, this.distanceFunc);
        
        MinPQ<Node> gameTree = new MinPQ<>(new NodeComparator());
        gameTree.insert(initialNode); 
        MinPQ<Node> gameTreeTwin = new MinPQ<>(new NodeComparator());
        gameTreeTwin.insert(initialNodeTwin); 
        
        while (!gameTree.isEmpty()) {
            Node currNode = gameTree.delMin();
            
            if (currNode.board.isGoal()) {
                this.solvable = true;
                this.solvedNode = currNode;
                this.movesToSolve = this.solvedNode.movesMade;
                break;
            } else {
                addNeighbours(currNode, gameTree);
            }

            Node currNodeTwin = gameTreeTwin.delMin();
            if (currNodeTwin.board.isGoal()) {
                this.solvable = false;
                this.solvedNode = null; 
                break;
            } else {
                addNeighbours(currNodeTwin, gameTreeTwin);
            }      
        }
    }

    private void addNeighbours(Node currNode, MinPQ<Node> tree) {
        Iterable<Board> neighbourBoards = currNode.board.neighbors();
        for (Board b: neighbourBoards) {
            if (currNode.prevBoard == null) {
                ;
            }
            else if (currNode.prevBoard.board.equals(b)) {
                continue;
            }
            Node neighbourNode = new Node(
                currNode.movesMade, 
                b, 
                currNode, 
                this.distanceFunc
            );
            tree.insert(neighbourNode);
        }
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
        Node lastNode = this.solvedNode;
        while (lastNode != null) {
            solutionBranch.add(0, lastNode.board);
            lastNode = lastNode.prevBoard;
        }
        return solutionBranch;
    }
    
}
