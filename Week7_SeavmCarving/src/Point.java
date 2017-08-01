/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        else if (this.x != that.x && this.y == that.y) return 0.0;
        else if (this.x == that.x && this.y != that.y) return Double.POSITIVE_INFINITY;
        else return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    
    public int compareTo(Point that) {
        if ((this.y < that.y) || ((this.y == that.y) && (this.x < that.x))) return -1;
        else if (this.y == that.y && this.x == that.x) return 0;
        else return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     * 
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
    	return new SlopeOrderComparator();
    }

	private class SlopeOrderComparator implements Comparator<Point> {
		public int compare(Point p1, Point p2){
			if (Point.this.slopeTo(p1) > Point.this.slopeTo(p2)) return 1;
			else if (Point.this.slopeTo(p1) == Point.this.slopeTo(p2)) return 0;
			else return -1;
	};
}
    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p1 = new Point(1,2);
        StdOut.println(p1);
        Point p2 = new Point(3,4);
        StdOut.println(p2);
        Point p3 = new Point(5,6);
        StdOut.println(p3);
        Point p4 = new Point(1,2);
        StdOut.printf("itself -infinity: %.2f\n",p1.slopeTo(p4));
        Point p5 = new Point(2,2);
        StdOut.printf("horizontal 0.0: %.2f\n",p1.slopeTo(p5));
        Point p6 = new Point(1,1);
        StdOut.printf("vertical +infinity: %.2f\n",p1.slopeTo(p6));
        
        StdOut.printf("%s compare to %s: %d\n",p1, p2, p1.compareTo(p2));
        StdOut.printf("%s compare to %s: %d\n",p1, p4, p1.compareTo(p4));
        StdOut.printf("%s compare to %s: %d\n",p1, p5, p1.compareTo(p5));
        StdOut.printf("%s compare to %s: %d\n",p1, p6, p1.compareTo(p6));

        Point[] points = new Point[4];
        points[0] = p2;
        points[1] = p3;
        points[2] = p5;
        points[3] = p6;
        
        StdOut.println("before sort");
        for(int i = 0; i < 4; i++)StdOut.println((Point)points[i]);
        
        StdOut.println("after sort by default");
        Arrays.sort(points);
        for(int i = 0; i < 4; i++)StdOut.println((Point)points[i]);
        
        Arrays.sort(points, p1.slopeOrder());
        StdOut.println("after sort by comparator");
        for(int i = 0; i < 4; i++)StdOut.println((Point)points[i]);
        
    }
}
