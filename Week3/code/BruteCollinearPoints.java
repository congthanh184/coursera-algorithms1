import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {
    private int numOfSegments;
    private LineSegment[] lineSegment;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();

        this.numOfSegments = 0;
        this.lineSegment = new LineSegment[points.length];        

        Point headij, tailij, headik, tailik, headil, tailil;

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new NullPointerException();

            for (int j = i+1; j < points.length; j++) {                
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
                double slope1 = points[i].slopeTo(points[j]);
                headij = points[i];
                tailij = points[j];
                if (points[j].compareTo(points[i]) < 0) {
                    headij = points[j];
                    tailij = points[i];
                }

                for (int k = j+1; k < points.length; k++) {
                    double slope2 = points[i].slopeTo(points[k]);
                    headik = headij;
                    tailik = tailij;
                    if (slope1 == slope2) {                        
                        if (headij.compareTo(points[k]) > 0) headik = points[k];
                        else if (tailij.compareTo(points[k]) < 0) tailik = points[k];
                        headil = headik;
                        tailil = tailik;
                        boolean flag = false;
                        for (int l = k+1; l < points.length; l++) {
                            double slope3 = points[i].slopeTo(points[l]);

                            if (slope1 == slope3) {
                                if (headik.compareTo(points[l]) > 0) headil = points[l];
                                else if (tailik.compareTo(points[l]) < 0) tailil = points[l];          
                                flag = true;
                                // StdOut.printf("%f %f %f\n", slope1, slope2, slope3);
                                // StdOut.println(this.numOfSegments);
                            }
                        }
                        if (flag) {
                            this.lineSegment[this.numOfSegments++] = new LineSegment(headil, tailil);
                            // StdOut.printf("%d %s %s\n", this.numOfSegments, headil.toString(), tailil.toString());
                        } 
                    }
                }
            }
        }
    }
    
    public int numberOfSegments() {
        return this.numOfSegments;
    }
    public LineSegment[] segments() {
        LineSegment[] mySegments = new LineSegment[this.numOfSegments];
        for (int i = 0; i < this.numOfSegments; i++) mySegments[i] = this.lineSegment[i];
        return mySegments;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point temp = new Point(x, y);
            points[i] = temp;
            // StdOut.println(temp.toString());
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
            // StdOut.println(p.toString());
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}