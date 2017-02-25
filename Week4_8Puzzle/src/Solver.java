import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	
	private Node A;
	
	private class Node {
		private Board board;
		private int priority;
		private int moves;
		private int manhattan;
		private Node pre;
		
		public Node(Board board, int moves, Node pre) {
			this.board = board;
			this.moves = moves;
			this.manhattan = board.manhattan();
			this.priority = this.moves + this.manhattan;
			this.pre = pre;
		}
		
		public Comparator<Node> priorityOrder() {
			return new PriorityOrderComparator();
		}
		
		private class PriorityOrderComparator implements Comparator<Node> {
			@Override
			public int compare(Node o1, Node o2) {
				if (o1.priority < o2.priority) return -1;
				else if (o1.priority > o2.priority) return 1;
				else return 0;
			}
		}
		
		public String toString() { 
			StringBuilder s = new StringBuilder();
	    	s.append("priority : " + this.priority + "\n");
	    	s.append("moves    : " + this.moves + "\n");
	    	s.append("manhattan: " + this.manhattan + "\n");
	    	s.append(this.board.toString());
	    	return s.toString();
		}
	}
	
    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)
    	this.twinSolution(initial);
    }
    
    private boolean twinSolution(Board initial) {
    	Node firstA = new Node(initial, 0, null);
    	Node currentA = firstA;
    	MinPQ<Node> nodePQA = new MinPQ<Node>(currentA.priorityOrder());

		Node firstB = new Node(initial.twin(), 0, null);
    	Node currentB = firstB;
    	MinPQ<Node> nodePQB = new MinPQ<Node>(currentB.priorityOrder());
		
    	while (currentA.manhattan != 0 && currentB.manhattan != 0) {
    		currentA = this.alternateAB(currentA, nodePQA);
    		currentB = this.alternateAB(currentB, nodePQB);
    	}
    	
    	if (currentA.manhattan == 0) { 
    		this.A = currentA;
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private Node alternateAB(Node node, MinPQ<Node> nodePQ) {
		for (Board b : node.board.neighbors()) {
			Node nextLevelNode = new Node(b, node.moves+1, node);
			if (null == node.pre || !nextLevelNode.board.equals(node.pre.board)) {
				nodePQ.insert(nextLevelNode);
			}
		}
		return nodePQ.delMin();
    }
    
    public boolean isSolvable() {           // is the initial board solvable?
    	return null == A ? false : true;
    }
    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
    	if (!this.isSolvable()) return -1;
    	else return A.moves;
    }
    
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable
    	if (!this.isSolvable()) return null;
    	else {
    		Stack<Board> solutionStack = new Stack<Board>();
    		for (Node n = A; n != null; n = n.pre) {
    			solutionStack.push(n.board);
    		}
    		return solutionStack;
    	}
    }
    
    public static void main(String[] args) {// solve a slider puzzle (given below)
    	In in = new In(args[0]);
    	int N = 0;
    	while (!in.isEmpty()) {
    		N = in.readInt();
    		break;
    	}
    	
    	int[][] blocks = new int[N][N];
    	
    	int i = 0;
    	int j = 0;
    	while (!in.isEmpty()) {
    		blocks[i][j] = in.readInt();
    		j++;
    		if (j == N) { 
    			i++; 
    			j = 0; 
    		}
    		if (i == N) { 
    			break; 
    		}
    	}
    	
    	Board b = new Board(blocks);
    	
    	Solver solver = new Solver(b);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
}