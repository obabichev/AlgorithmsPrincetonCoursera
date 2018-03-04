import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeamCarver {

    private Picture picture;
    private double[][] weights;

    public SeamCarver(Picture picture) {
        this.picture = picture;

        fillEnergy();
    }

    private void fillEnergy() {
        weights = new double[picture.width()][picture.height()];
        for (int i = 0; i < width(); i++) {
            weights[i][0] = 1000;
            weights[i][height() - 1] = 1000;
        }
        for (int i = 0; i < height(); i++) {
            weights[0][i] = 1000;
            weights[width() - 1][i] = 1000;
        }
        for (int i = 1; i < width() - 1; i++) {
            for (int j = 1; j < height() - 1; j++) {
                weights[i][j] = countEnergy(i, j);
            }
        }
    }

    private double countEnergy(int x, int y) {

        double dx = gradient(picture.get(x - 1, y), picture.get(x + 1, y));
        double dy = gradient(picture.get(x, y - 1), picture.get(x, y + 1));

        return Math.sqrt(dx + dy);
    }

    private double gradient(Color f, Color s) {
        return Math.pow(f.getRed() - s.getRed(), 2) +
                Math.pow(f.getGreen() - s.getGreen(), 2) +
                Math.pow(f.getBlue() - s.getBlue(), 2);
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        return weights[x][y];
    }

    public int[] findHorizontalSeam() {
        int[] seam = new int[width()];

        double[] distTo = new double[size() + 2];
        int[] vertexTo = new int[size() + 2];

        for (int i = 0; i < size() + 2; i++) {
            distTo[i] = -1;
            vertexTo[i] = -1;
        }

        deikstra(0, 0, -1, distTo, vertexTo);

        for (int index = 0; index <= size(); index++) {
            double weight = distTo[index];
            for (Integer neighbor : horizontalNeighboars(index)) {
                int[] point = indexToPoint(neighbor);
                double nextWeight = neighbor == end() ? weight : weight + energy(point[0], point[1]);
                deikstra(neighbor, nextWeight, index, distTo, vertexTo);
            }
        }

        int last = vertexTo.length - 1;
        int[] point = null;
        do {
            last = vertexTo[last];

            point = indexToPoint(last);
            seam[point[0]] = point[1];
//            System.out.println(String.format("[%d](%d, %d)", last, point[0], point[1]));
        } while (point[0] != 0);

        return seam;
    }

    public int[] findVerticalSeam() {
        int[] seam = new int[height()];

        double[] distTo = new double[size() + 2];
        int[] vertexTo = new int[size() + 2];

        for (int i = 0; i < size() + 2; i++) {
            distTo[i] = -1;
            vertexTo[i] = -1;
        }

        deikstra(0, 0, -1, distTo, vertexTo);

        for (int index = 0; index <= size(); index++) {
            double weight = distTo[index];
            for (Integer neighbor : verticalNeighboars(index)) {
                int[] point = indexToPoint(neighbor);
                double nextWeight = neighbor == end() ? weight : weight + energy(point[0], point[1]);
                deikstra(neighbor, nextWeight, index, distTo, vertexTo);
            }
        }


//        printDistTo(distTo);
//        System.out.println(Arrays.toString(vertexTo));

        int last = vertexTo.length - 1;
        int[] point = null;
        do {
            last = vertexTo[last];

            point = indexToPoint(last);
            seam[point[1]] = point[0];
//            System.out.println(String.format("[%d](%d, %d)", last, point[0], point[1]));
        } while (point[1] != 0);

        return seam;
    }

    private void printDistTo(double[] distTo) {
        System.out.println("start: " + distTo[0]);
        for (int j = 0; j < height(); j++) {

            for (int i = 0; i < width(); i++) {
                System.out.printf(String.format("\t[%d]%4.1f (%4.1f) ", pointToIntex(i, j), distTo[pointToIntex(i, j)], energy(i, j)));
            }
            System.out.println();
        }
        System.out.println("end: " + distTo[size() + 1]);
    }

    private void deikstra(int index, double weight, int parent, double[] distTo, int[] vertexTo) {
        if (distTo[index] != -1 && distTo[index] <= weight) {
            return;
        }

        distTo[index] = weight;
        vertexTo[index] = parent;
    }

    private int[] indexToPoint(int index) {
        final int[] result = new int[2];
        result[0] = (index - 1) % width();
        result[1] = (index - 1) / width();
        return result;
    }

    private List<Integer> verticalNeighboars(int index) {
        final ArrayList<Integer> result = new ArrayList<>();

        if (index == 0) {
            for (int i = 1; i <= width(); i++) {
                result.add(i);
            }
            return result;
        }

        int x = (index - 1) % width();
        int y = (index - 1) / width();

        if (y == height() - 1) {
            result.add(end());
            return result;
        }

        if (x - 1 >= 0) {
            result.add(pointToIntex(x - 1, y + 1));
        }
        if (x + 1 < width()) {
            result.add(pointToIntex(x + 1, y + 1));
        }
        result.add(pointToIntex(x, y + 1));

        return result;
    }

    private List<Integer> horizontalNeighboars(int index) {
        final ArrayList<Integer> result = new ArrayList<>();
        if (index == 0) {
            for (int i = 0; i < height(); i++) {
                result.add(pointToIntex(0, i));
            }
            return result;
        }

        int x = (index - 1) % width();
        int y = (index - 1) / width();

        if (x == width() - 1) {
            result.add(end());
            return result;
        }

        if (y - 1 >= 0) {
            result.add(pointToIntex(x + 1, y - 1));
        }
        if (y + 1 < height()) {
            result.add(pointToIntex(x + 1, y + 1));
        }
        result.add(pointToIntex(x + 1, y));

        return result;
    }

    private int begin() {
        return 0;
    }

    private int end() {
        return width() * height() + 1;
    }

    private int pointToIntex(int x, int y) {
        return x + y * width() + 1;
    }

    private int size() {
        return width() * height();
    }

    public void removeHorizontalSeam(int[] seam) {
        final Picture newPicture = new Picture(width(), height() - 1);

        for (int j = 0; j < width(); j++) {
            int delRow = seam[j];
            for (int i = 0; i < height() - 1; i++) {
                newPicture.set(j, i, this.picture.get(j, i < delRow ? i : i + 1));
            }
        }

        this.picture = newPicture;
        fillEnergy();
    }

    public void removeVerticalSeam(int[] seam) {
        final Picture newPicture = new Picture(width() - 1, height());

        for (int i = 0; i < height(); i++) {
            int delCol = seam[i];
            for (int j = 0; j < width() - 1; j++) {
                newPicture.set(j, i, this.picture.get(j < delCol ? j : j + 1, i));
            }
        }

        this.picture = newPicture;
        fillEnergy();
    }

    public static void main(String[] args) {
        final Picture picture = new Picture(args[0]);
        picture.show();
        final SeamCarver seamCarver = new SeamCarver(picture);

        for (int i = 0; i < 100; i++) {
//            seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
            seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
        }

        seamCarver.picture().show();
    }
}