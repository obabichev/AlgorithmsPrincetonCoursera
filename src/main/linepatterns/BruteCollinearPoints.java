import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int size = 0;


    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        segments = new LineSegment[10];
        fillSegments(points);
    }

    private void fillSegments(Point[] points) {
        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                for (int r = q + 1; r < points.length; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        checkPointsSegment(points[p], points[q], points[r], points[s]);
                    }
                }
            }
        }
    }

    private void checkPointsSegment(Point p, Point q, Point r, Point s) {
        if (p.slopeTo(q) == p.slopeTo(r)
                && p.slopeTo(q) == p.slopeTo(s)) {
            final Point[] resultSet = {p, q, r, s};
            Arrays.sort(resultSet);

            if (isFull()) {
                resize();
            }
            segments[size++] = new LineSegment(resultSet[0], resultSet[3]);
        }
    }

    private boolean isFull() {
        return size == segments.length;
    }

    private void resize() {
        LineSegment[] newSegments = new LineSegment[size * 2];
        System.arraycopy(segments, 0, newSegments, 0, size);
        segments = newSegments;
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[size];
        System.arraycopy(segments, 0, result, 0, size);
        return result;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}