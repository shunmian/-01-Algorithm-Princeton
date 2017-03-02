import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;

public class PointSET {
	private SET<Point2D> pointSet;
	
	public PointSET() {							// construct an empty set of points
		this.pointSet = new SET<Point2D>();
	}
	public boolean isEmpty() {					// is the set empty
		return this.pointSet.size() <= 0;
	}
	public int size() {							// number of points in the set
		return this.pointSet.size();
	}
	

	public void insert(Point2D p) {				// add the point to the set (if it is not already in the set)
		if (null == p) throw new NullPointerException();
		this.pointSet.add(p);
	}
	
	public boolean contains(Point2D p) {		// does the set contain point p
		if (null == p) throw new NullPointerException();
		return this.pointSet.contains(p);
	}
	public void draw() {						// draw all points to standard draw
		for (Point2D p: this.pointSet) {
			p.draw();
		}
	}
	public Iterable<Point2D> range(RectHV rect)	{ // all points that are inside the rectangle
		if (null == rect) throw new NullPointerException();
		Queue<Point2D> points = new Queue<Point2D>();
		for (Point2D p: this.pointSet) {
			if (rect.contains(p)) points.enqueue(p);
		}
		return points;
	}
	public Point2D nearest(Point2D p) {			// a nearest neighbor in the set to point p; null if the set is empty
		if (null == p) throw new NullPointerException();
		double distance = 2;
		Point2D nearestPoint = null;
		for(Point2D thatP: this.pointSet) {
			if (p.distanceTo(thatP) < distance) {
				distance = p.distanceTo(thatP);
				nearestPoint = thatP;
			}
		}
		return nearestPoint;
	}
	public static void main(String[] args) {	// unit testing of the methods (optional)
		
	}
}
