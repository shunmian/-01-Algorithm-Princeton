import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
	private int N;
	private WeightedQuickUnionUF ufTopBottom;
	private WeightedQuickUnionUF ufTop;
	private boolean[][] opened;
	private int openSites;

	public Percolation(int N) {
		if (N <= 0) throw new IllegalArgumentException();
		this.N = N;
		// include virtual top site (N*Nth) and virtual bottom site(N*N+1th) and N*N sites
		this.ufTopBottom = new WeightedQuickUnionUF(N*N+2); 
		// include virtual top site (N*Nth) and virtual bottom site(N*N+1th) and N*N sites
		this.ufTop = new WeightedQuickUnionUF(N*N+1); 
		this.opened = new boolean[N][N];
		this.openSites = 0;	
	}
	
	public void open(int i, int j) {
		this.validateXY(i, j);
		if (this.isOpen(i, j)) return;
		this.opened[i-1][j-1] = true;
		this.openSites++;
		
		// for N==1, i==1 && j==1 special case
		if (this.N == 1 && i == 1 && j == 1) {
			this.union(this.xyTo1D(i, j), 0);
			this.ufTopBottom.union(this.xyTo1D(i, j), N*N+1);
			return;
		}
	
		// for other general case.
		if (i == 1) {
			this.union(this.xyTo1D(i, j), 0);
			if (isOpen(i+1, j)) this.union(this.xyTo1D(i, j), this.xyTo1D(i+1, j));
		} else if (i == N) {
			this.ufTopBottom.union(this.xyTo1D(i, j), N*N+1);
			if (isOpen(i-1, j)) this.union(this.xyTo1D(i, j), this.xyTo1D(i-1, j));
		} else {
			if (isOpen(i+1, j)) this.union(this.xyTo1D(i, j), this.xyTo1D(i+1, j));
			if (isOpen(i-1, j)) this.union(this.xyTo1D(i, j), this.xyTo1D(i-1, j));
		}
		
		if (j == 1) {
			if (isOpen(i, j+1)) this.union(this.xyTo1D(i, j), this.xyTo1D(i, j+1));
		} else if (j == N) {
			if (isOpen(i, j-1)) this.union(this.xyTo1D(i, j), this.xyTo1D(i, j-1));
		} else {
			if (isOpen(i, j+1)) this.union(this.xyTo1D(i, j), this.xyTo1D(i, j+1));
			if (isOpen(i, j-1)) this.union(this.xyTo1D(i, j), this.xyTo1D(i, j-1));	
		}
	}
	
	public boolean isOpen(int i, int j) {
		this.validateXY(i, j);
		return this.opened[i-1][j-1];
	}
	
	public boolean isFull(int i, int j) {
		this.validateXY(i, j);
		return this.ufTop.connected(this.xyTo1D(i, j), 0);
	}
	
	public boolean percolates() {
		return this.ufTopBottom.connected(0, N*N+1);
	}
	
	public int numberOfOpenSites() {
		return this.openSites;
	}
	
	// helper method
	private int xyTo1D(int i, int j) {
		this.validateXY(i, j);
		return (i-1)*N+j;
	}
	
	private void union(int i, int j) {
		this.ufTop.union(i, j);
		this.ufTopBottom.union(i, j);
	}
	
	private void validateXY(int i, int j) {
		if (i <= 0 || i > N || j <= 0 || j > N) throw new IndexOutOfBoundsException("row index i out of bounds");
		else return;
	}
}




