import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] lineSegements;
    private int numberOfLineSegements;


    public BruteCollinearPoints(Point[] points){
        if (points == null) {
            throw new IllegalArgumentException();
        }
        if (points.length <= 3) {
            checkNull(points);
            checkEqual(points);
        }
        this.lineSegements = new LineSegment[points.length/2];
        this.numberOfLineSegements = 0;
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i+1; j < points.length - 2; j++) {
                for (int k = j+1; k < points.length - 1; k++) {
                    for (int l = k+1; l < points.length; l++) {
                        Point[] fourPoints = new Point[]{points[i],points[j], points[k],points[l]};
                        if (isCollinear(points[i],points[j], points[k],points[l])) {
                            if (this.numberOfLineSegements == this.lineSegements.length) {
                                resizeLineSegementArray();
                            }
                            this.lineSegements[this.numberOfLineSegements] = new LineSegment(minPoint(fourPoints), maxPoint(fourPoints));
                            this.numberOfLineSegements++;
                        }
                    }
                }
            }
        }

    }

    private void resizeLineSegementArray() {
        int newSize = 2*this.lineSegements.length;
        LineSegment[] newArray = new LineSegment[newSize];
        for (int i = 0; i < this.numberOfLineSegements; i++) {
            newArray[i] = this.lineSegements[i];
        }
        this.lineSegements = newArray;
    }

    private void checkNull(Point[] points) {
        for(int i = 0; i<points.length; i++){
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void checkEqual(Point[] points){
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i+1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private boolean isCollinear(Point p, Point q, Point r, Point s) {
        Point[] points = new Point[]{p,q,r,s};
        checkNull(points);
        checkEqual(points);

        return ((p.slopeTo(q) == q.slopeTo(r)) && (q.slopeTo(r) == r.slopeTo(s)));
    }

    private Point maxPoint(Point[] points) {
        Point max = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(max) > 0) {
                max = points[i];
            }else if (points[i].compareTo(max) == 0) {
                throw new IllegalArgumentException();
            }
        }

        return max;
    }

    private Point minPoint(Point[] points) {
        Point min = points[0];
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(min) < 0) {
                min = points[i];
            }else if (points[i].compareTo(min) == 0) {
                throw new IllegalArgumentException();
            }
        }

        return min;
    }

    public int numberOfSegments() {
        return this.numberOfLineSegements;
    }

    public LineSegment[] segments() {
        LineSegment[] returnArray = new LineSegment[this.numberOfLineSegements];
        for(int i = 0; i < numberOfLineSegements; i++) {
            returnArray[i] = this.lineSegements[i];
        }
        
        return returnArray;
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
        
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        points[0] = null;
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        System.out.println(collinear.numberOfSegments());
        StdDraw.show();
        // Arrays.sort(points);
        // for (Point p:points) {
        //     System.out.println(p);
        // }
    }
}
