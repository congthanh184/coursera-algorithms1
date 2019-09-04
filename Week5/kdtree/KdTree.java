import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class KdTree {

    private static class KdNode {
        private Point2D point;
        private RectHV rect;
        private KdNode lb, rb;
        private boolean orientation;
        public KdNode( Point2D p, RectHV rect, boolean orientation) {
            this.point = p;
            this.rect = rect;
            this.orientation = orientation;
        }
    }

    private int sizeTree = 0;
    private KdNode root;
    private final boolean vertical = true;
    private final boolean horizontal = false;

    // construct an empty set of points 
    public         KdTree() {        
    }
    // is the set empty? 
    public           boolean isEmpty() {
        return sizeTree == 0;
    }   
    // number of points in the set 
    public               int size() {
        return sizeTree;
    }                         
    // add the point to the set (if it is not already in the set)
    public              void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        if (sizeTree == 0) {
            root = new KdNode(p, new RectHV(0, 0, 1, 1), vertical);
            this.sizeTree = 1;
        }
        else {
            put(root, p);
        }
        // sizeTree = size(root.lb) + size(root.rb) + 1;
    }              

    private             void put(KdNode node, Point2D p) {
        this.root = put(node, p, vertical);
    }

    private             KdNode put(KdNode node, Point2D p, boolean orientation) {
        if (node == null) {
            this.sizeTree++;
            return new KdNode(p, null, orientation);
        }
        // if (p.equals(node.point)) return node;
        int compareResult = compareKdTree(node.point, p, orientation);

        if (compareResult == 1) {
            node.lb = put( node.lb, p, !orientation);
            if (node.lb.rect == null) {
                if (orientation == vertical) node.lb.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.point.x(), node.rect.ymax());
                else node.lb.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.point.y());
            }
        }
        else if (compareResult == -1) {
            node.rb = put( node.rb, p, !orientation);        
            if (node.rb.rect == null) {
                if (orientation == vertical) node.rb.rect = new RectHV(node.point.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
                else node.rb.rect = new RectHV(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.rect.ymax());
            }
        }
        return node;
    }

    private             int compareKdTree(Point2D p, Point2D that, boolean orientation) {
        // if (p.x() == that.x() && p.y() == that.y()) return 0;
        if (p.equals(that)) return 0;
        if (orientation == vertical) {
            if (p.x() <= that.x()) return -1;
            // return 1;
            // else return 0;
        } 
        else {
            if (p.y() <= that.y()) return -1;
            // return 1;
        }
        return 1;
    }

    private             KdNode get(Point2D key) {
        return get(root, key, vertical);
    }

    private             KdNode get(KdNode node, Point2D p, boolean orientation) {
        if (node == null) return null;
        // if (p.equals(node.point)) return node;
        if (compareKdTree(node.point, p, orientation) == 1) return get( node.lb, p, !orientation);
        else if (compareKdTree(node.point, p, orientation) == -1) return get( node.rb, p, !orientation);        
        return node;        
    }

    // does the set contain point p? 
    public           boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return (get(p) != null);
    }            
    // draw all points to standard draw 
    public              void draw() {
        Stack<KdNode> stack = new Stack<KdNode>();

        stack.push(root);
        while (!stack.isEmpty()) {
            KdNode node = stack.pop();
            if (node != null) 
            {            
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                node.point.draw();
                if (node.orientation == vertical) StdDraw.setPenColor(StdDraw.RED);
                else StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                node.rect.draw();
                StdOut.println(node.point);
                StdOut.println(node.orientation);
                StdOut.println(node.rect);
                stack.push(node.lb);
                stack.push(node.rb);
            }        
        }
    }                         
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        Stack<Point2D> pointInRect = new Stack<Point2D>();
        Stack<KdNode> stack = new Stack<KdNode>();
        stack.push(this.root);

        while (!stack.isEmpty()) {
            KdNode node = stack.pop();
            if (node != null && rect.intersects(node.rect)) {
                if (rect.contains(node.point)) pointInRect.push(node.point);
                stack.push(node.lb);
                stack.push(node.rb);
            }
        }

        return pointInRect;
    }             
    // a nearest neighbor in the set to point p; null if the set is empty 
    public           Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        double minDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;

        Stack<KdNode> stack = new Stack<KdNode>();
        stack.push(this.root);

        while (!stack.isEmpty()) {
            KdNode node = stack.pop();
            if (node != null) {
                double distToRectTemp = node.rect.distanceSquaredTo(p);
                if (distToRectTemp < minDist) {
                    double distTemp = p.distanceSquaredTo(node.point);
                    if (distTemp < minDist) {
                        nearest = node.point;
                        minDist = distTemp;
                    }

                    if (compareKdTree(node.point, p, node.orientation) == 1) {
                        stack.push(node.rb);
                        stack.push(node.lb);                        
                    }
                    else {
                        stack.push(node.lb);                                                
                        stack.push(node.rb);
                    }
                }

            }
        }

        return nearest;
    }             
    // unit testing of the methods (optional) 
    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        StdOut.println(tree.contains(new Point2D(0.2, 0.3)));
        StdOut.println(tree.contains(new Point2D(0.5, 0.3)));
        tree.draw();
    }                  
}