import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * Created by obabichev on 25/02/2018.
 */
public class KdTree {

    private class Node {
        boolean isHorizontal;
        Point2D point;
        Node first;
        Node second;
        Node parent;

        public Node(boolean isHorizontal, Point2D point, Node parent) {
            this.isHorizontal = isHorizontal;
            this.point = point;
            this.parent = parent;
        }
    }

    private Node root = null;
    private int size = 0;

    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void goNextNodeForAdd(Node currentNode, Point2D p) {
        double nodeValues = currentNode.isHorizontal ? currentNode.point.x() : currentNode.point.y();
        double pValues = currentNode.isHorizontal ? p.x() : p.y();
        if (pValues < nodeValues) {
            if (currentNode.first == null) {
                currentNode.first = new Node(!currentNode.isHorizontal, p, currentNode);
                return;
            }
            goNextNodeForAdd(currentNode.first, p);
        } else {
            if (currentNode.second == null) {
                currentNode.second = new Node(!currentNode.isHorizontal, p, currentNode);
                return;
            }
            goNextNodeForAdd(currentNode.second, p);
        }
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (contains(p)) {
            return;
        }
        if (size == 0) {
            root = new Node(true, p, null);
        } else {
            Node currentNode = root;
            goNextNodeForAdd(currentNode, p);
        }
        size++;
    }

    private boolean goNextNodeForFind(Node currentNode, Point2D p) {
        if (currentNode == null) {
            return false;
        }
        if (currentNode.point.equals(p)) {
            return true;
        }
        double nodeValues = currentNode.isHorizontal ? currentNode.point.x() : currentNode.point.y();
        double pValues = currentNode.isHorizontal ? p.x() : p.y();
        if (pValues < nodeValues) {
            return goNextNodeForFind(currentNode.first, p);
        } else {
            return goNextNodeForFind(currentNode.second, p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (size == 0) {
            return false;
        }
        return goNextNodeForFind(root, p);
    }

    private void goNextNodeForDraw(Node currentNode) {
        if (currentNode == null) {
            return;
        }
        currentNode.point.draw();
        if (currentNode.isHorizontal) {
            StdDraw.line(currentNode.point.x(), 0, currentNode.point.x(), 1);
        } else {
            StdDraw.line(0, currentNode.point.y(), 1, currentNode.point.y());
        }
        goNextNodeForDraw(currentNode.first);
        goNextNodeForDraw(currentNode.second);
    }

    public void draw() {
        if (size == 0) {
            return;
        }
        goNextNodeForDraw(root);
    }

    private void goNextNodeForRange(Node currentNode, RectHV rect, ArrayList<Point2D> res) {
        if (currentNode == null) {
            return;
        }
        double nodeValues = currentNode.isHorizontal ? currentNode.point.x() : currentNode.point.y();
        double maxValues = currentNode.isHorizontal ? rect.xmax() : rect.ymax();
        double minValues = currentNode.isHorizontal ? rect.xmin() : rect.ymin();

        if (rect.contains(currentNode.point)) {
            res.add(currentNode.point);
        }
        if (maxValues >= nodeValues) {
            goNextNodeForRange(currentNode.second, rect, res);
        }
        if (minValues <= nodeValues) {
            goNextNodeForRange(currentNode.first, rect, res);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        final ArrayList<Point2D> pointInRect = new ArrayList<>();
        goNextNodeForRange(root, rect, pointInRect);
        return pointInRect;
    }

    private Node goNextNodeForNearest(Node currentNode, RectHV rect, Point2D point, Node currentMinNode) {
        if (currentNode == null) {
            return currentMinNode;
        }
        RectHV rectFirst;
        RectHV rectSecond;
        if (currentNode.isHorizontal) {
            rectFirst = new RectHV(rect.xmin(), rect.ymin(), currentNode.point.x(), rect.ymax());

            rectSecond = new RectHV(currentNode.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            rectSecond = new RectHV(rect.xmin(), currentNode.point.y(), rect.xmax(), rect.ymax());
            rectFirst = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), currentNode.point.y());
        }
        if (currentNode.point.distanceTo(point) < currentMinNode.point.distanceTo(point)) {
            currentMinNode = currentNode;
        }
        if (currentMinNode.point.distanceTo(point) > rectFirst.distanceTo(point)) {
            currentMinNode = goNextNodeForNearest(currentNode.first, rectFirst, point, currentMinNode);
        }
        if (currentMinNode.point.distanceTo(point) > rectSecond.distanceTo(point)) {
            currentMinNode = goNextNodeForNearest(currentNode.second, rectSecond, point, currentMinNode);
        }
        return currentMinNode;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (size == 0) {
            return null;
        }
        return goNextNodeForNearest(root, new RectHV(0, 0, 1, 1), p, root).point;
    }

    public static void main(String[] args) {
        final KdTree pointSET = new KdTree();
        System.out.println(pointSET.isEmpty());
        pointSET.insert(new Point2D(1, 2));
        pointSET.insert(new Point2D(1, 2));
        pointSET.insert(new Point2D(2, 3));
        System.out.println(pointSET.isEmpty());
        pointSET.insert(new Point2D(5, 2));
        System.out.println(pointSET.contains(new Point2D(1, 2)));
        System.out.println(!pointSET.contains(new Point2D(7, 3)));
        final RectHV rectHV = new RectHV(2, 2, 9, 9);
//        for (Point2D pointInRect : pointSET.range(rectHV)) {
//            System.out.println(pointInRect);
//        }
//        System.out.println(pointSET.nearest(new Point2D(10000,3)));
    }
}
