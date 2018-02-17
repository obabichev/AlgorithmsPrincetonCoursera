import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private int size = 0;
    private List<Point> checkedPoints;


    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException();
                }
            }
        }

        segments = new LineSegment[10];
        checkedPoints = new ArrayList<>();
        fillSegments(points);
    }

    private void fillSegments(Point[] points) {
        if (points.length < 4) {
            return;
        }

        for (int i = 0; i < points.length; i++) {
            final Point point = points[i];
            Point[] pointsDupl = new Point[points.length - 1];
            int j = 0;
            int k = 0;
            while (j < pointsDupl.length) {
                if (k != i) {
                    pointsDupl[j] = points[k];
                    j++;
                }
                k++;
            }
            Arrays.sort(pointsDupl, point.slopeOrder());
            findLines(point, pointsDupl);
        }
    }

    private void findLines(Point point, Point[] slopeSorted) {
        int i = 0;
        int j = 1;

        while (i < slopeSorted.length - 1) {
            if (j < slopeSorted.length - 1
                    && point.slopeTo(slopeSorted[i]) == point.slopeTo(slopeSorted[j])) {
                j++;
            } else {
                Point[] segment = new Point[j - i + 1];
                for (int k = 0; k < j - i; k++) {
                    segment[k] = slopeSorted[k + i];
                    segment[segment.length - 1] = point;
                }
                checkSegment(segment);
                i = j;
                j++;
            }
        }
        checkedPoints.add(point);
    }

    private void checkSegment(Point[] segment) {
        if (segment.length < 4) {
            return;
        }
        for (Point point : segment) {
            if (isChecked(point)) {
                return;
            }
        }
        Arrays.sort(segment);

        if (isFull()) {
            resize();
        }
        segments[size++] = new LineSegment(segment[0], segment[segment.length - 1]);
    }

    private boolean isChecked(Point point) {
        for (Point checkedPoint : checkedPoints) {
            if (checkedPoint == point) {
                return true;
            }
        }
        return false;
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
        return size;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}