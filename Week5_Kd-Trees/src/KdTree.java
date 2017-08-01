import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
	
	private Node root;
	private int size = 0;
	private Queue<Node> toStringBFSQueue;
	
	private static class Node implements Comparable {
		private Point2D p;
		private RectHV rect;
		private Node lb;
		private Node rt;
		private boolean isH;
		private int level;

		private Node(Point2D p) {
			this.p = p;
			this.rect = null;
			this.lb = null;
			this.rt = null;
			this.isH = false;
		}
		
		private Node(Point2D p, RectHV rect, boolean isH) {
			this.p = p;
			this.rect = rect;
			this.lb = null;
			this.rt = null;
			this.isH = isH;
		}
		
		@Override
		public int compareTo(Object o1) {
			Node o = (Node) o1;
			if (isH) {
				double thisY = this.p.y();
				double thatY = o.p.y();
				if (thisY < thatY) return -1;
				else if (thisY == thatY && !this.p.equals(o.p)) return -1;
				else if (thisY > thatY) return 1;
				else return 0;
			} else {
				double thisX = this.p.x();
				double thatX = o.p.x();
				if (thisX < thatX) return -1;
				else if (thisX == thatX && !this.p.equals(o.p)) return -1;
				else if (thisX > thatX) return 1;
				else return 0;
			}
		}
	}
	
	public KdTree() {							// construct an empty set of points
		this.root = null;
		this.size = 0;
	}
	public boolean isEmpty() {					// is the set empty
		return this.size <= 0;
	}
	public int size() {							// number of points in the set
		return this.size;
	}

	public void insert(Point2D p) {				// add the point to the set (if it is not already in the set)
		if (null == p) throw new NullPointerException();
		this.root = this.insert(root, p, new RectHV(0,0,1,1), 0);
	}
	
	private Node insert(Node x, Point2D p, RectHV rect, int level) {
		Node pNode = new Node(p);
		if (null == x) {		// is p is not found, insert
			this.size++;
			return new Node(p, rect, level % 2 != 0) ;
		}
		int cmp = x.compareTo(pNode);

		if (cmp == 1) {			//if p is smaller than x, go left
			RectHV lb = null;
			if(x.isH) {
				if (null == x.lb) lb = new RectHV(x.rect.xmin(), x.rect.ymin(),x.rect.xmax(),x.p.y());
				x.lb = insert(x.lb, p, lb, ++level);
			} else {
				if (null == x.lb) lb = new RectHV(x.rect.xmin(), x.rect.ymin(),x.p.x(),x.rect.ymax());
				x.lb = insert(x.lb, p, lb, ++level);
			}
		} else if(cmp == -1){	//if p is greater than x or partly equal, go right
			RectHV rb = null;
			if (x.isH) {
				if(null == x.rt) rb = new RectHV(x.rect.xmin(),x.p.y(),x.rect.xmax(),x.rect.ymax());
				x.rt = insert(x.rt, p, rb, ++level);
			} else {
				if(null == x.rt) rb = new RectHV(x.p.x(),x.rect.ymin(),x.rect.xmax(),x.rect.ymax());
				x.rt = insert(x.rt, p, rb, ++level);
			}
		} else {
			return x;
		}
		
		// if p is fully equal to p || after update, return
		return x;
	}
	
	public boolean contains(Point2D p) {		// does the set contain point p
		if (null == p) throw new NullPointerException();
		Node x = root;
		Node pNode = new Node(p);
		while((null != x)) {
			int cmp = x.compareTo(pNode);
			if (cmp == 1) x = x.lb;
			else if (cmp == -1) x = x.rt;
			else {
				if (x.p.equals(p)) return true;
				else return false;
			}
		}
		return false;
	}
	
	private Iterable<Node> Points() {
		Queue<Node> queue = new Queue<Node>();
		this.enqueue(queue, root);
		return queue;
	}
	
	private void enqueue(Queue<Node> queue, Node x) {
		if (null == x) return;
		else {
			enqueue(queue, x.lb);
			queue.enqueue(x);
			enqueue(queue,x.rt);
		}
	}

	public void draw() {						// draw all points to standard draw
		for (Node x: this.Points()) {
			StdDraw.setPenColor(StdDraw.BLACK);
	        StdDraw.setPenRadius(0.01);
			x.p.draw();
			if(x.isH){
				StdDraw.setPenColor(StdDraw.BLUE);
		        StdDraw.setPenRadius(0.005);
				(new Point2D(x.rect.xmin(),x.p.y())).drawTo( new Point2D(x.rect.xmax(),x.p.y()));

			}else{
				StdDraw.setPenColor(StdDraw.RED);
		        StdDraw.setPenRadius(0.005);
				(new Point2D(x.p.x(),x.rect.ymin())).drawTo(new Point2D(x.p.x(),x.rect.ymax()));
			}
		}
	}
	public Iterable<Point2D> range(RectHV rect)	{ // all points that are inside the rectangle
		if (null == rect) throw new NullPointerException();
		Queue<Point2D> queue = new Queue<Point2D>();
		this.enqueueRange(queue, root, rect);
		return queue;
	}
	
	private void enqueueRange(Queue<Point2D> queue, Node x, RectHV rect) {
		if (null == x) return;
		if (!this.splittingLineIntersects(rect, x)) {
			if (leftBottomSubtreeIntersects(rect, x)) enqueueRange(queue, x.lb, rect);
			else enqueueRange(queue,x.rt,rect);
			
		} else {
			if(rect.contains(x.p)) queue.enqueue(x.p);
			enqueueRange(queue,x.lb,rect);
			enqueueRange(queue,x.rt,rect);
		}
	}
	
	private boolean leftBottomSubtreeIntersects(RectHV r, Node x) {
		if(x.isH){
			return r.ymax() < x.p.y();
		}else{
			return r.xmax() < x.p.x();
		}
	}
	
	private boolean splittingLineIntersects(RectHV r, Node x) {
		if (x.isH) {
			return r.ymin() <= x.p.y() && r.ymax() >= x.p.y();
		} else {
			return r.xmin() <=x.p.x() && r.xmax() >= x.p.x();
		}
	}
	
	public Point2D nearest(Point2D p) {			// a nearest neighbor in the set to point p; null if the set is empty
		if (null == p) throw new NullPointerException();
		MinPoint minP = new MinPoint();
		minP.distance = 3.0;
		this.findNearestPoint(root, p, minP);
		return minP.p;
	}
	
	private class MinPoint {
		private Point2D p;
		private double distance;
	}
	
	private void findNearestPoint(Node x, Point2D targetP, MinPoint minP) {
		
		if(null == x) return;
		if(x.rect.distanceSquaredTo(targetP) >= minP.distance) return;
		
		double distance = x.p.distanceSquaredTo(targetP);
		if (distance < minP.distance) { 
			minP.distance = distance;
			minP.p = x.p;
		}
		
		if(null != x.lb ) {
			int cmp = x.compareTo(new Node(targetP));
//			if(x.lb.rect.contains(targetP)) {
			if (cmp == 1) {
				this.findNearestPoint(x.lb, targetP, minP);
				this.findNearestPoint(x.rt, targetP, minP);
			}else {
				this.findNearestPoint(x.rt, targetP, minP);
				this.findNearestPoint(x.lb, targetP, minP);
			}
		}else if(null != x.rt ) {
			int cmp = x.compareTo(new Node(targetP));
//			if (x.rt.rect.contains(targetP)) {
			if (cmp == -1) {
				this.findNearestPoint(x.rt, targetP, minP);
				this.findNearestPoint(x.lb, targetP, minP);
			} else {
				this.findNearestPoint(x.lb, targetP, minP);
				this.findNearestPoint(x.rt, targetP, minP);
			}
		} else {
			return;
		}		
	}
	
	private String toStringRepresenation() {
		this.toStringBFSQueue = new Queue<Node>();
		StringBuilder sb = new StringBuilder();
		this.toStringBFSQueue.enqueue(root);
		int level = -1;
		root.level = 0;
		boolean levelFlag = false;
		sb = this.BFSToString(this.toStringBFSQueue, sb, levelFlag, level);
		return sb.substring(1);	// remove first "\n"
	}
	
	// BFS representation, Poin2D(-1.0,-1.0) represents null child for parent with only one non-null child.
	private StringBuilder BFSToString(Queue<Node> queue, StringBuilder sb, Boolean levelFlag, int level) {
		if(queue.isEmpty()) return sb;
		
		Node n = queue.dequeue();
		if(levelFlag && n.level != level) {
			levelFlag = false;
		}
		if(!levelFlag) {
			level++;
			levelFlag = true;
			sb.append("\nlevel: " + n.level + "     ");
		}
		if(null != n.lb) {
			n.lb.level = n.level+1; 
			queue.enqueue(n.lb);
			if(null == n.rt){
				Node nullNode = new Node(new Point2D(-1.0,-1.0));
				nullNode.level = n.level + 1;
				queue.enqueue(nullNode);
			}
		}
		if(null != n.rt) {
			if(null == n.lb){
				Node nullNode = new Node(new Point2D(-1.0,-1.0));
				nullNode.level = n.level + 1;
				queue.enqueue(nullNode);
			}
			n.rt.level = n.level + 1;
			queue.enqueue(n.rt);
		}
		sb.append(n.p.toString() + " ");
		return BFSToString(queue, sb, levelFlag, level);
	}
	
	
	public static void main(String[] args) {	// unit testing of the methods (optional)
		
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();

        // initialize the two data structures with point from standard input
        
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
		kdtree.draw();
		StdDraw.show();
        StdOut.println(kdtree.toStringRepresenation());

        Point2D targetP = new Point2D(0.5,0.89);
        StdOut.printf("nearest pioints to %s: %s\n",targetP, kdtree.nearest(targetP));

        Point2D targetP2 = new Point2D(0.206107,0.904508);
        StdOut.printf("kdtree contains %s: %b\n", targetP2, kdtree.contains(targetP2));
	}
}
