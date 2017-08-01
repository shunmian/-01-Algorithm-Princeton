import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;

public class FastCollinearPoints {
	private Point[] points;
	private Point[] sortedSlopes;
	private LinkedQueue<LineSegment> segments;

	public FastCollinearPoints(Point[] points) {     // finds all line segments containing 4 or more points
		if(null == points) throw new IllegalArgumentException();
		for (int i = 0; i < points.length; i++){
			if (null == points[i]) throw new IllegalArgumentException();
		}
		
		this.points = points.clone();
		Arrays.sort(this.points);
		Point pre = this.points[0];
		for (int i = 1; i < this.points.length; i++){
			if (null == this.points[i]) throw new IllegalArgumentException();
			if (pre.compareTo(this.points[i]) == 0) throw new IllegalArgumentException();
			pre = this.points[i];
		}
		
		this.segments = new LinkedQueue<LineSegment>();
		
		for (int i = 0; i < this.points.length; i++) {
			this.sortedSlopes = new Point[this.points.length-i];
			System.arraycopy(this.points, i, this.sortedSlopes, 0, this.points.length-i);
			Point p0 = this.points[i];
			Arrays.sort(sortedSlopes, p0.slopeOrder());
			
			for(int start = 1; start < this.sortedSlopes.length-2; ){
				Point p1 = sortedSlopes[start];
				Point p2 = sortedSlopes[start+1];
				Point p3 = sortedSlopes[start+2];
				if (p0.slopeTo(p1) == p0.slopeTo(p2) &&
				   p0.slopeTo(p1) == p0.slopeTo(p3)){
					int end = 0;
					for(end = start+3; end < this.sortedSlopes.length; end++) {
						Point p_end = this.sortedSlopes[end];
						if(p0.slopeTo(p1) != p0.slopeTo(p_end)) break;
					}
					int length = end-start+1;
					Point[] collinearPoints = new Point[length];
					collinearPoints[0] = this.sortedSlopes[0];
					for (int delta = 1; delta < length; delta++) {
						collinearPoints[delta] = this.sortedSlopes[start+delta-1];
					}
					Arrays.sort(collinearPoints);
					Point max = collinearPoints[length-1];
					Point min =	collinearPoints[0];
					boolean addedFlag = false;
					for (int j = 0; j < i; j++){
						if(this.points[j].slopeTo(max) == max.slopeTo(min)) {
							addedFlag = true;
							break;
						}
					}
					if (!addedFlag) {
						this.segments.enqueue(new LineSegment(min, max));
					}
					start = end;
				} else {
					start++;
					continue;
				}
			}
		}
	}
	 
	public int numberOfSegments() {       // the number of line segments
		return this.segments.size();
	}
	 
	public LineSegment[] segments() {               // the line segments
		LineSegment[] segments = new LineSegment[this.numberOfSegments()];
		int i = 0;
		for(LineSegment s: this.segments){
			segments[i++] = s;
		}
		return segments;
	}
	 
	public static void main(String[] args) {
		String filename = args[0];
		StdOut.println(filename);
		In in = new In(filename);
		int size = 0;
		while(!in.isEmpty()){
			size = in.readInt();
			break;
		}
		Point[] points = new Point[size];
		int i = 0;
		while(!in.isEmpty()){
			 int x = in.readInt();
			 int y = in.readInt();
			 Point point = new Point(x,y);
			 points[i++] = point;
			 StdOut.println(point);
		}
		
		FastCollinearPoints bcp = new FastCollinearPoints(points);
		LineSegment[] segments = bcp.segments();
		for(int j = 0; j < segments.length; j++){
			StdOut.println(segments[j]);
		}
	}
}
