import java.util.Arrays;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	
	private Point[] points;
	private LinkedQueue<LineSegment> segments;

	public BruteCollinearPoints(Point[] points) {	// finds all line segments containing 4 points
		
		// check duplicates point
		this.points = points.clone();
		Arrays.sort(this.points);
		for(int i = 0; i < this.points.length - 1; i++){
			Point p1 = this.points[i];
			Point p2 = this.points[i + 1];
			if(p1.compareTo(p2)==0) throw new IllegalArgumentException();
		}
		
		this.segments = new LinkedQueue<LineSegment>();
		for (int i = 0; i < this.points.length; i++) {
			for (int j = i + 1; j < this.points.length; j++) {
				for (int k = j + 1; k < this.points.length; k++) {
					for (int l = k + 1; l < this.points.length; l++) {
						Point[] tempPoints = new Point[4];
						tempPoints[0] = this.points[i];
						tempPoints[1] = this.points[j];
						tempPoints[2] = this.points[k];
						tempPoints[3] = this.points[l];
						Arrays.sort(tempPoints);
						Point p1 = tempPoints[0];
						Point p2 = tempPoints[1];
						Point p3 = tempPoints[2];
						Point p4 = tempPoints[3];
						if(p1.slopeTo(p2) == p1.slopeTo(p3) && (p1.slopeTo(p2) == p1.slopeTo(p4))) {
							this.segments.enqueue(new LineSegment(p1,p4));
						}
					}
				}
			}
		}
	}
	
	public int numberOfSegments() {					// the number of line segments
		return this.segments.size();
	}
	
	public LineSegment[] segments()	{			// the line segments
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
		
		BruteCollinearPoints bcp = new BruteCollinearPoints(points);
		LineSegment[] segments = bcp.segments();
		for(int j = 0; j < segments.length; j++){
			StdOut.println(segments[j]);
		}
	}

}
