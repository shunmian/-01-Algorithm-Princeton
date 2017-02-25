import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;


public class PercolationStats {
	private int N;
	private int T;
	private double result[];
	
	public PercolationStats(int N, int T) {
		if (N <= 0 || T <= 0) throw new IllegalArgumentException();
		this.N = N;
		this.T = T;
		this.result = new double[T];
		for (int i = 0; i < T; i++) {
			Percolation p = new Percolation(N);
			this.result[i] = this.percolationTask(p);
		}
	}
	
	private double percolationTask(Percolation p) {
		while (!p.percolates()) {
			int randomI = StdRandom.uniform(1, this.N+1);
			int randomJ = StdRandom.uniform(1, this.N+1);
			if (p.isOpen(randomI, randomJ)) continue;
			p.open(randomI, randomJ);
		}
		return (double)(p.numberOfOpenSites())/(N*N);
	}
	
	public double mean() {
		return StdStats.mean(this.result);
	}
	public double stddev() {
		return StdStats.stddev(this.result);
	}
	public double confidenceLo() {
		return this.mean()-1.96*this.stddev()/Math.sqrt(this.T);
	}
	public double confidenceHi() {
		return this.mean()+1.96*this.stddev()/Math.sqrt(this.T);
	}
	
	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		PercolationStats ps = new PercolationStats(N, T);
		
		StdOut.printf("mean                     = %.16f\n", ps.mean());
		StdOut.printf("stddev                   = %.16f\n", ps.stddev());
		StdOut.printf("95%% confidence interval = %.16f, %.16f\n", ps.confidenceLo(), ps.confidenceHi());
	}
}