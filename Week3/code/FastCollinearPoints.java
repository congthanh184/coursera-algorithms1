import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class FastCollinearPoints {
    private int numOfSegments = 0;
    private Point[] storeMinPoint, storeMaxPoint, sortPoints;
    private LineSegment[] storeLineSeg;

    public FastCollinearPoints(Point[] points) { // finds all line segments containing 4 or more points
        if (points == null) throw new NullPointerException();

        storeMinPoint = new Point[points.length*16];
        storeMaxPoint = new Point[points.length*16];        
        storeLineSeg = new LineSegment[points.length*16];
        sortPoints = new Point[points.length];

        this.numOfSegments = 0;

        for (int i = 0; i < points.length; i++) {
            sortPoints[i] = points[i];
        }
        // Arrays.sort(points);

        // Point a = sortPoints[6];
        // StdOut.println(a);
        // Arrays.sort(sortPoints);
        // StdOut.println(a);

        int count = 0;
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(sortPoints, points[i].slopeOrder());

            // for (Point p : sortPoints) {
            //     StdOut.printf("%s %f\n", p.toString(), sortPoints[0].slopeTo(p));
            // }
            // StdOut.println();
            count = 0;
            Point minPoint = sortPoints[0];
            Point maxPoint = sortPoints[0];

            for (int j = 1; j < points.length; j++) {
                if (sortPoints[j].compareTo(sortPoints[j-1]) == 0) throw new IllegalArgumentException();
                double slope1 = sortPoints[0].slopeTo(sortPoints[j]); 
                double slope2;
                if (j < points.length - 1) slope2 = sortPoints[0].slopeTo(sortPoints[j+1]);
                else slope2 = sortPoints[0].slopeTo(sortPoints[0]);

                if (minPoint.compareTo(sortPoints[j]) > 0) minPoint = sortPoints[j];
                else if (maxPoint.compareTo(sortPoints[j]) < 0) maxPoint = sortPoints[j];

                // StdOut.printf("%f %f ", slope1, slope2);
                // StdOut.println(slope1 == slope2);
                if (slope1 == slope2) {
                    count++;
                }
                else {
                    if (count >= 2) {
                        boolean flag = true;
                        for (int k = 0; k < this.numOfSegments; k++) {
                            if (minPoint.compareTo(storeMinPoint[k]) == 0 && maxPoint.compareTo(storeMaxPoint[k]) == 0) {
                                flag = false;
                                break;
                            }
                        }
                        // for (int k = 0; k < this.numOfSegments; k++) {
                        //     StdOut.printf("%s %s * ", storeMinPoint[k].toString(), storeMaxPoint[k].toString());                            
                        // }
                        // StdOut.println("=========");
                        if (flag) {
                            storeMaxPoint[this.numOfSegments] = maxPoint;
                            storeMinPoint[this.numOfSegments] = minPoint;
                            storeLineSeg[this.numOfSegments] = new LineSegment(minPoint, maxPoint);
                            this.numOfSegments++;                                                        
                        }
                    }
                    minPoint = sortPoints[0];
                    maxPoint = sortPoints[0];
                    count = 0;
                }
            }
        }
    }   

    public int numberOfSegments() {
        return this.numOfSegments;
    }   // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] tempSegs = new LineSegment[this.numOfSegments];
        // StdOut.println(this.numOfSegments);
        for (int i = 0; i < this.numOfSegments; i++) {
            tempSegs[i] = this.storeLineSeg[i];            
        }
        return tempSegs;        
    }   // the line segments

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        points[4] = new Point(1000, 1000);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        collinear.numberOfSegments();
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            // segment.draw();
        }
        // StdDraw.show();
        // StdOut.println("done");
    }
}