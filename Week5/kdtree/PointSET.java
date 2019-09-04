import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private SET<Point2D> setPoint;

    // construct an empty set of points 
    public         PointSET() {
        setPoint = new SET<Point2D>();
    }
    // is the set empty? 
    public           boolean isEmpty() {
        return setPoint.isEmpty();
    }   
    // number of points in the set 
    public               int size() {
        return setPoint.size();
    }                         
    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        setPoint.add(p);
    }              
    // does the set contain point p? 
    public           boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return setPoint.contains(p);
    }            
    // draw all points to standard draw 
    public              void draw() {
        for (Point2D p : setPoint) {
            p.draw();
        }
    }                         
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        Stack<Point2D> pointInRect = new Stack<Point2D>();
        for (Point2D p : setPoint) {
            if (rect.contains(p)) {
                pointInRect.push(p);
            }
        }
        return pointInRect;
    }             
    // a nearest neighbor in the set to point p; null if the set is empty 
    public           Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D thisPoint : setPoint) {
            double distTemp = p.distanceSquaredTo(thisPoint);
            if (distTemp < minDist) {
                nearest = thisPoint;
                minDist = distTemp;
            }
        }
        return nearest;
    }             
    // unit testing of the methods (optional) 
    public static void main(String[] args) {

    }                  
}