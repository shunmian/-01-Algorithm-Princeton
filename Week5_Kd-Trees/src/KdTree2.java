import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * 2D-Tree solution of 2D range and nearest calculations.
 *
 * @author ISchwarz
 */
public class KdTree2 {

    private Node root;

    public boolean isEmpty() {   // is the set empty?
        return size() == 0;
    }

    public int size() {   // number of points in the set
        return size(root);
    }

    private int size(final Node nodeToCheckSize) {
        if (nodeToCheckSize == null) return 0;
        else return nodeToCheckSize.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(final Point2D pointToInsert) {
        checkNotNull(pointToInsert, "Not supported to insert null as point");
        root = put(root, pointToInsert, 0, new RectHV(0, 0, 1, 1));
    }

    private Node put(final Node node, final Point2D pointToInsert, final int level, final RectHV rect) {
        if (node == null) {               
            return new Node(level, pointToInsert, rect);
        }

        RectHV rectLeft = null;
        RectHV rectRight = null;
        double cmp = node.compare(pointToInsert);

        if (cmp < 0 && node.left == null) {
            if (level % 2 == 0) {
                rectLeft = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax());
            } else {
                rectLeft = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.point.y());
            }
        } else if (cmp >= 0 && node.right == null) {
            if (level % 2 == 0) {
                rectRight = new RectHV(node.point.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
            } else {
                rectRight = new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.rect.ymax());
            }
        }

        if (cmp < 0) node.left = put(node.left, pointToInsert, level + 1, rectLeft);
        else if (cmp > 0) node.right = put(node.right, pointToInsert, level + 1, rectRight);
        else if (!pointToInsert.equals(node.point)) node.right = put(node.right, pointToInsert, level + 1, rectRight);

        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    // does the set contain the given point?
    public boolean contains(final Point2D searchedPoint) {
        checkNotNull(searchedPoint, "Null is never contained in a PointSET");
        return get(root, searchedPoint, 0) != null;
    }

    private Point2D get(final Node node, final Point2D searchedPoint, final int level) {
        if (node == null) return null;

        double cmp = node.compare(searchedPoint);
        if (cmp < 0) return get(node.left, searchedPoint, level + 1);
        else if (cmp > 0) return get(node.right, searchedPoint, level + 1);
        else if (!searchedPoint.equals(node.point)) return get(node.right, searchedPoint, level + 1);
        else return node.point;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(final Node nodeToDraw) {
        if (nodeToDraw == null) return;
        StdDraw.point(nodeToDraw.point.x(), nodeToDraw.point.y());
        draw(nodeToDraw.left);
        draw(nodeToDraw.right);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(final RectHV queryRect) {
        checkNotNull(queryRect, "Can't calculate range for a rect will point null");
        return range(queryRect, root);
    }

    private List<Point2D> range(final RectHV queryRect, final Node node) {
        if (node == null) return Collections.emptyList();

        if (node.doesSpittingLineIntersect(queryRect)) {
            List<Point2D> points = new ArrayList<>();
            if (queryRect.contains(node.point)) {
                points.add(node.point);
            }
            points.addAll(range(queryRect, node.left));
            points.addAll(range(queryRect, node.right));
            return points;
        } else {
            if (node.isRightOf(queryRect)) return range(queryRect, node.left);
            else return range(queryRect, node.right);
        }
    }

    // a nearest neighbor in the set to point queryPoint; null if the set is empty
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
		if(x.rect.distanceTo(targetP) >= minP.distance) return;
		
		double distance = x.point.distanceTo(targetP);
		if (distance < minP.distance) { 
			minP.distance = distance;
			minP.p = x.point;
		}
		
		if(null != x.left ) {
			if(x.left.rect.contains(targetP)) {
				this.findNearestPoint(x.left, targetP, minP);
				this.findNearestPoint(x.right, targetP, minP);
			}else {
				this.findNearestPoint(x.right, targetP, minP);
				this.findNearestPoint(x.left, targetP, minP);
			}
		}else if(null != x.right ) {
			if(x.right.rect.contains(targetP)) {
				this.findNearestPoint(x.right, targetP, minP);
				this.findNearestPoint(x.left, targetP, minP);
			}else {
				this.findNearestPoint(x.left, targetP, minP);
				this.findNearestPoint(x.right, targetP, minP);
			}
		}else {
			return;
		}		
	}
//    public Point2D nearest(final Point2D queryPoint) {
//        checkNotNull(queryPoint, "Can't calculate nearest point to a point with point null");
//        if (root == null) {
//            return null;
//        }
//        return nearest(queryPoint, root, root.point, queryPoint.distanceTo(root.point));
//    }
//
//    private Point2D nearest(final Point2D queryPoint, final Node node, final Point2D currentlyClosestPoint,
//                            final double currentlyClosestDistance) {
//        if (node == null) return null;
//        Point2D closestPoint = currentlyClosestPoint;
//        double closestDistance = currentlyClosestDistance;
//
//        Point2D currentPoint = node.point;
//        double currentDistance = queryPoint.distanceTo(currentPoint);
//        if (currentDistance < closestDistance) {
//            closestDistance = currentDistance;
//            closestPoint = currentPoint;
//        }
//
//        double cmp = node.compare(queryPoint);
//        if (cmp < 0) {
//            currentPoint = nearest(queryPoint, node.left, closestPoint, closestDistance);
//        } else {
//            currentPoint = nearest(queryPoint, node.right, closestPoint, closestDistance);
//        }
//
//        if (currentPoint != null) {
//            if (currentPoint != closestPoint) {
//                closestDistance = currentPoint.distanceTo(queryPoint);
//                closestPoint = currentPoint;
//            }
//        }
//
//        double nodeRectDistance = -1;
//        if (cmp < 0 && node.right != null) {
//            nodeRectDistance = node.right.rect.distanceTo(queryPoint);
//        } else if (cmp >= 0 && node.left != null) {
//            nodeRectDistance = node.left.rect.distanceTo(queryPoint);
//        }
//
//        if (nodeRectDistance != -1 && nodeRectDistance < closestDistance) {
//            if (cmp < 0) {
//                currentPoint = nearest(queryPoint, node.right, closestPoint, closestDistance);
//            } else {
//                currentPoint = nearest(queryPoint, node.left, closestPoint, closestDistance);
//            }
//        }
//
//        if (currentPoint != null) {
//            closestPoint = currentPoint;
//        }
//
//        return closestPoint;
//    }

    private static void checkNotNull(final Object o, final String messageIfObjectIsNull) {
        if (o == null) throw new NullPointerException(messageIfObjectIsNull);
    }


    private static class Node {

        private final Point2D point;
        private final RectHV rect;
        private final int level;

        private Node left, right;   // left and right subtrees
        private int size;           // number of nodes in subtree


        public Node(final int level, final Point2D point, final RectHV rect) {
            this.level = level;
            this.point = point;
            this.rect = rect;
            this.size = 1;
        }

        public double compare(final Point2D pointToCompare) {
            if (level % 2 == 0) {
                return pointToCompare.x() - point.x();
            } else {
                return pointToCompare.y() - point.y();
            }
        }

        public boolean doesSpittingLineIntersect(final RectHV rectToCheck) {
            if (level % 2 == 0) {
                return rectToCheck.xmin() <= point.x() && point.x() <= rectToCheck.xmax();
            } else {
                return rectToCheck.ymin() <= point.y() && point.y() <= rectToCheck.ymax();
            }
        }

        public boolean isRightOf(final RectHV rectToCheck) {
            if (level % 2 == 0) {
                return rectToCheck.xmin() < point.x() && rectToCheck.xmax() < point.x();
            } else {
                return rectToCheck.ymin() < point.y() && rectToCheck.ymax() < point.y();
            }
        }

    }
    
//	public static void main(String[] args) {	// unit testing of the methods (optional)
//		
//        String filename = args[0];
//        In in = new In(filename);
//
//        StdDraw.enableDoubleBuffering();

        // initialize the two data structures with point from standard input
        
//        KdTree2 kdtree = new KdTree2();
//        while (!in.isEmpty()) {
//            double x = in.readDouble();
//            double y = in.readDouble();
//            Point2D p = new Point2D(x, y);
//            kdtree.insert(p);
//        }
//		kdtree.draw();
//		StdDraw.show();
//        StdOut.println(kdtree);
//      
//        StdOut.printf("# x()				: %d\n",Point2D.xcalled);
//        StdOut.printf("# y()				: %d\n",Point2D.ycalled);
        
//        StdOut.printf("# xmin				: %d\n",RectHV.xminCalled);
//        StdOut.printf("# xmax				: %d\n",RectHV.xmaxCalled);
//        StdOut.printf("# ymin				: %d\n",RectHV.yminCalled);
//        StdOut.printf("# ymax				: %d\n",RectHV.ymaxCalled);
//        StdOut.printf("# intersects			: %d\n",RectHV.intersectsCalled);
//        StdOut.printf("# contains			: %d\n",RectHV.containsCalled);
//        StdOut.printf("# distanceTo			: %d\n",RectHV.distanceToCalled);
//        StdOut.printf("# distanceSquaredTo	: %d\n",RectHV.distanceSquaredToCalled);
//        StdOut.printf("# equals				: %d\n",RectHV.equalsCalled);
//        StdOut.printf("# toString			: %d\n",RectHV.toStringCalled);
//        StdOut.printf("# drawCalled			: %d\n",RectHV.drawCalled);
//        StdOut.printf("# initialization		: %d\n",RectHV.initialization);
//        StdOut.printf("# total				: %d\n",RectHV.totalCalled());
//        
//        int xminCalled = RectHV.xminCalled;
//        int xmaxCalled = RectHV.xmaxCalled;
//        int yminCalled = RectHV.yminCalled;
//        int ymaxCalled = RectHV.ymaxCalled;
//        int intersectsCalled = RectHV.intersectsCalled;
//        int containsCalled = RectHV.containsCalled;
//        int distanceToCalled = RectHV.distanceToCalled;
//        int distanceSquaredToCalled = RectHV.distanceSquaredToCalled;
//        int equalsCalled = RectHV.equalsCalled;
//        int toStringCalled = RectHV.toStringCalled;
//        int drawCalled = RectHV.drawCalled;
//        int initialization = RectHV.initialization;
//        int totalCalled = RectHV.totalCalled();
//
//   
//        Point2D targetP = new Point2D(0.5,0.89);
//        StdOut.printf("nearest pioints to %s: %s\n",targetP, kdtree.nearest(targetP));
//        
//        
//        StdOut.printf("# x()				: %d\n",Point2D.xcalled);
//        StdOut.printf("# y()				: %d\n",Point2D.ycalled);
//        
//        Point2D targetP2 = new Point2D(0.206107,0.904508);
//        StdOut.printf("kdtree contains %s: %b\n", targetP2, kdtree.contains(targetP2));
//        
//        StdOut.printf("# xmin				: %d\n",RectHV.xminCalled-xminCalled);
//        StdOut.printf("# xmax				: %d\n",RectHV.xmaxCalled-xmaxCalled);
//        StdOut.printf("# ymin				: %d\n",RectHV.yminCalled-yminCalled);
//        StdOut.printf("# ymax				: %d\n",RectHV.ymaxCalled-ymaxCalled);
//        StdOut.printf("# intersects			: %d\n",RectHV.intersectsCalled-intersectsCalled);
//        StdOut.printf("# contains			: %d\n",RectHV.containsCalled-containsCalled);
//        StdOut.printf("# distanceTo			: %d\n",RectHV.distanceToCalled-distanceToCalled);
//        StdOut.printf("# distanceSquaredTo	: %d\n",RectHV.distanceSquaredToCalled-distanceSquaredToCalled);
//        StdOut.printf("# equals				: %d\n",RectHV.equalsCalled-equalsCalled);
//        StdOut.printf("# toString			: %d\n",RectHV.toStringCalled-toStringCalled);
//        StdOut.printf("# drawCalled			: %d\n",RectHV.drawCalled-drawCalled);
//        StdOut.printf("# initialization		: %d\n",RectHV.initialization-initialization);
//        StdOut.printf("# total				: %d\n",RectHV.totalCalled()-totalCalled);
//
//	}

}