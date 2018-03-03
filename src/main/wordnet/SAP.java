import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private final Digraph g;
    private int[] blue;
    private int[] red;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        this.g = G;
    }

    private void init() {
        blue = new int[g.V()];
        red = new int[g.V()];
        for (int i = 0; i < g.V(); i++) {
            blue[i] = -1;
            red[i] = -1;
        }
    }

    private void bfs(int length, int v, int[] marks) {
        marks[v] = length;
        for (Integer adj : g.adj(v)) {
            if (marks[adj] == -1 || marks[adj] > length + 1) {
                bfs(length + 1, adj, marks);
            }
        }
    }

    private int findShortestLength() {
        int result = -1;
        for (int i = 0; i < g.V(); i++) {
            if (red[i] != -1 && blue[i] != -1) {
                int sum = red[i] + blue[i];
                result = result == -1 ? sum : Math.min(result, sum);
            }
        }
        return result;
    }

    private int findAncestor() {
        int minLength = findShortestLength();
        if (minLength == -1) {
            return -1;
        }

        for (int i = 0; i < g.V(); i++) {
            if (red[i] != -1 && blue[i] != -1) {
                int sum = red[i] + blue[i];
                if (sum == minLength) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void checkVertex(int v) {
        if (v < 0 || v >= g.V()) {
            throw new IllegalArgumentException();
        }
    }

    public int length(int v, int w) {
        checkVertex(v);
        checkVertex(w);

        init();
        bfs(0, v, blue);
        bfs(0, w, red);
        return findShortestLength();
    }

    public int ancestor(int v, int w) {
        checkVertex(v);
        checkVertex(w);
        init();
        bfs(0, v, blue);
        bfs(0, w, red);
        return findAncestor();
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        init();
        for (Integer vi : v) {
            checkVertex(vi);
            bfs(0, vi, blue);
        }
        for (Integer wi : w) {
            checkVertex(wi);

            bfs(0, wi, red);
        }

        return findShortestLength();
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        init();
        for (Integer vi : v) {
            checkVertex(vi);
            bfs(0, vi, blue);
        }
        for (Integer wi : w) {
            checkVertex(wi);
            bfs(0, wi, red);
        }
        return findAncestor();
    }

    public static void main(String[] args) {
        System.out.println("path:" + args[0]);
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        int v = 3;
        int w = 11;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}