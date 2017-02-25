import edu.princeton.cs.algs4.LinkedQueue;
import java.util.Iterator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
	
	private int[][] blocks;
	private int N;
	
	public Board(int[][] blocks) {          // construct a board from an n-by-n array of blocks
		this.blocks = this.copy2DArray(blocks);
		this.N = blocks.length;
	}
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension() {                 // board dimension n
    	return this.N;
    }
    public int hamming() {                   // number of blocks out of place
    	int sum = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (this.blocks[i][j] != 0 && (this.blocks[i][j] != N*i+j+1)) sum++;  
			}
		}
		return sum;
    }
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
    	int sum = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (this.blocks[i][j] != 0) {
					int n = this.blocks[i][j];
					int goalI = (n-1) / N;
					int goalJ = (n-1) % N;
					int delta = Math.abs(goalI-i) + Math.abs(goalJ-j);
					if (delta != 0) sum += delta;
				};  
			}
		}
		return sum;
    }
    public boolean isGoal() {               // is this board the goal board?
    	return this.manhattan() == 0;
    }
    public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
		int[][] twinArray = this.copy2DArray(this.blocks);
		int count = 0;
		int[][] index = new int[2][2];
		boolean outFlag = false;
    	for (int i = 0; i < N; i++) {
    		if (outFlag == true) break;
			for (int j = 0; j < N; j++) {
				if (count >= 2) {
					outFlag = true;
					break;
				}
				if (twinArray[i][j] != 0) {
					index[count][0] = i;
					index[count][1] = j;
					count++;
				};  
			}
		}
    	this.swapItem(twinArray, index[0][0], index[0][1], index[1][0], index[1][1]);
    	return new Board(twinArray);
    }
    
    public boolean equals(Object y) {       // does this board equal y?
    	if (null == y || y.getClass() != this.getClass()) return false;
    	else {
    		Board bY = (Board)y;
    		if (this.dimension() != bY.dimension()) return false;
    		return this.toString().equals(bY.toString());
    	}
    }
    
    public Iterable<Board> neighbors() {     // all neighboring boards
    	int i0 = 0, j0 = 0;
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			if (this.blocks[i][j] == 0) {
    				i0 = i;
    				j0 = j;
    				break;
    			}
    		}
    	}
    	LinkedQueue<Board> bQueue = new LinkedQueue<Board>();
    	if (i0 - 1 >= 0) {
    		int[][] upBlock = this.copy2DArray(this.blocks);
    		this.swapItem(upBlock, i0, j0, i0-1, j0);
    		bQueue.enqueue(new Board(upBlock));
    	}
    	
    	if (i0 + 1 < N) {
    		int[][] belowBlock = this.copy2DArray(this.blocks);
    		this.swapItem(belowBlock, i0, j0, i0+1, j0);
    		bQueue.enqueue(new Board(belowBlock));
    	}
    	
    	if (j0 - 1 >= 0) {
    		int[][] leftBlock = this.copy2DArray(this.blocks);
    		this.swapItem(leftBlock, i0, j0, i0, j0-1);
    		bQueue.enqueue(new Board(leftBlock));
    	}
    	
    	
    	if (j0 + 1 < N) {
    		int[][] rightBlock = this.copy2DArray(this.blocks);
    		this.swapItem(rightBlock, i0, j0, i0, j0+1);
    		bQueue.enqueue(new Board(rightBlock));
    	}
    	return bQueue;
    }
    
    private void swapItem(int[][] src, int srcI, int srcJ, int destI, int destJ) {
    	int temp = src[srcI][srcJ];
    	src[srcI][srcJ] = src[destI][destJ];
    	src[destI][destJ] = temp;
    }
    
    private int[][] copy2DArray(int[][] src) {
    	int n = src.length;
    	int[][] dest = new int[n][n];
    	for (int i = 0; i < n; i++) {
    		for (int j = 0; j < n; j++) {
    			dest[i][j] = src[i][j];
    		}
    	}
    	return dest;
    }
    
    public String toString() {              // string representation of this board (in the output format specified below)
    	StringBuilder s = new StringBuilder();
    	s.append(N + "\n");
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			s.append(String.format("%2d ", blocks[i][j]));
    		}
    		s.append("\n");
    	}
    	return s.toString();
    }

    public static void main(String[] args) {// unit tests (not graded)

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
    	StdOut.printf("Board toString:\n%s", b);
    	StdOut.println("twins:----------");
    	StdOut.print(b.twin());
    	
    	StdOut.printf("dimension: %d\n", b.dimension());
    	StdOut.printf("hamming  : %d\n", b.hamming());
    	StdOut.printf("manhattan: %d\n", b.manhattan());
    	StdOut.println("neighbours:----------");
    	for (Board neighbourB : b.neighbors()) {
    		StdOut.print(neighbourB);
    	}
    	
    	StdOut.println("twins:----------");
    	StdOut.print(b.twin());
    	
    }
}
