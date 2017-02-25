import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;


public class Permutation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> randQ = new RandomizedQueue<String>();
		while (!StdIn.isEmpty()) {
			randQ.enqueue(StdIn.readString());
		}	
		for (int i = 0; i < k; i++) {
			StdOut.println(randQ.dequeue());
		}
	}
}
