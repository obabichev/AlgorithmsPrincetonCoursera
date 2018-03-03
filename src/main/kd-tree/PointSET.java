import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        final ArrayList<Point2D> pointInRect = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointInRect.add(point);
            }
        }
        return pointInRect;
    }

    public Point2D nearest(Point2D p) {
        if (points.isEmpty()) {
            return null;
        }
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D point : points) {
            if (point.distanceTo(p) < minDistance) {
                minDistance = point.distanceTo(p);
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
//        final PointSET pointSET = new PointSET();
//        System.out.println(pointSET.isEmpty());
//        pointSET.insert(new Point2D(1,2));
//        pointSET.insert(new Point2D(1,2));
//        pointSET.insert(new Point2D(2,3));
//        System.out.println(pointSET.isEmpty());
//        pointSET.insert(new Point2D(5,2));
//        System.out.println(pointSET.contains(new Point2D(1, 2)));
//        System.out.println(!pointSET.contains(new Point2D(7, 3)));
//        final RectHV rectHV = new RectHV(2, 2, 9, 9);
//        for (Point2D pointInRect : pointSET.range(rectHV)) {
//            System.out.println(pointInRect);
//        }
//        System.out.println(pointSET.nearest(new Point2D(10000,3)));
    }
}
