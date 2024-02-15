import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private Point[] point_array;
    private ArrayList<LineSegment> lineSegements;
    private final int n;

    public FastCollinearPoints(Point[] points) {
        // Check if points is null
        if (points == null) {
            throw new IllegalArgumentException();
        }

        this.point_array = points.clone();
        this.n = points.length;
        this.lineSegements = new ArrayList<>();

        for (Point p: points) {
            // Sort and catch null points
            try {
                Arrays.sort(this.point_array, p.slopeOrder());
            } catch (NullPointerException e) {
                throw new IllegalArgumentException();
            }

            // Duplicate point
            if (points.length <= 1){
                return;
            }
            if (p.slopeTo(this.point_array[1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException();
            }

            int count = 1;
            double prev_slope = p.slopeTo(this.point_array[0]);
            
            for(int i = 1; i < n; i++) {
                double curr_slope = p.slopeTo(this.point_array[i]);
                
                if (prev_slope == curr_slope) {
                    count++;
                } else {
                    
                    if (count >= 3) {
                        //sort subsection of points and return
                        //add line only if the smallest value is p, then we know that we are adding the line only once
                        Arrays.sort(point_array, i-count, i);
                        if (p.compareTo(point_array[i-count]) < 0) {
                            lineSegements.add(new LineSegment(p, point_array[i-1]));
                        }
                    }

                    count = 1;
                    prev_slope = curr_slope;
                }
            }
            if (count >= 3) {
                Arrays.sort(point_array, n-count, n);
                if (p.compareTo(point_array[n-count]) < 0) {
                    lineSegements.add(new LineSegment(p, point_array[n-1]));
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegements.size();
    }

    public LineSegment[] segments() {
        return lineSegements.toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println(collinear.numberOfSegments());
        StdDraw.show();

    }
}
