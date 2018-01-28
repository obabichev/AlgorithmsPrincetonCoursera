public class Percolation {

    final private int n;
    private int[] data;
    private int[] size;
    private int opened = 0;

    final private int top = 0;
    final private int bottom;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        data = new int[n * n + 2];
        size = new int[n * n + 2];

        data[0] = 0;
        size[0] = 1;

        bottom = n * n + 1;
        data[bottom] = bottom;
        size[bottom] = 1;

        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                setData(row, col, -1);
                setSize(row, col, 1);
            }
        }
    }

    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        if (isOpen(row, col)) {
            return;
        }
        opened++;
        setData(row, col, rowColIndex(row, col));
        union(rowColIndex(row, col), rowColIndex(row + 1, col));
        union(rowColIndex(row, col), rowColIndex(row, col + 1));
        union(rowColIndex(row, col), rowColIndex(row - 1, col));
        union(rowColIndex(row, col), rowColIndex(row, col - 1));
    }

    private void union(int p, int q) {
        if (p < 0 || p > n * n + 1 || q < 0 || q > n * n + 1) {
            return;
        }
        if (!isOpen(p) || !isOpen(q)) {
            return;
        }
//        System.out.println("Union of " + p + " with " + q);
        int i = root(p);
        int j = root(q);
        if (i == j) {
            return;
        }
        if (getSize(i) < getSize(j)) {
            data[i] = j;
            setSize(j, getSize(i) + getSize(j));
        } else {
            data[j] = i;
            setSize(i, getSize(i) + getSize(j));
        }
    }

    private int root(int i) {
        while (i != data[i]) {
            if (data[i] >= 0 && data[i] <= n * n + 1) {
                data[i] = data[data[i]];
            }
            i = data[i];
        }
        return i;
    }

    private boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        return getData(row, col) != -1;
    }

    private boolean isOpen(int index) {
        return data[index] != -1;
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            return false;
        }
        return connected(rowColIndex(row, col), top);
    }

    public int numberOfOpenSites() {
        return opened;
    }

    public boolean percolates() {
        return connected(top, bottom);
    }

    private void setData(int row, int col, int val) {
        data[rowColIndex(row, col)] = val;
    }

    private int getData(int row, int col) {
        return data[rowColIndex(row, col)];
    }

    private void setSize(int index, int val) {
        size[index] = val;
    }

    private void setSize(int row, int col, int val) {
        size[rowColIndex(row, col)] = val;
    }

    private int getSize(int index) {
        return size[index];
    }

    private int rowColIndex(int row, int col) {
        if (row == 0) {
            return top;
        }
        if (row == n + 1) {
            return bottom;
        }
        if (col == 0 || col == n + 1) {
            return -1;
        }
        row = row - 1;
        col = col - 1;
        return row * n + col + 1;
    }

    private void print() {
        System.out.println(data[0] + "(" + getSize(top) + ")");
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                System.out.printf(getData(row, col) + "(" + getSize(rowColIndex(row, col)) + ") \t");
            }
            System.out.println();
        }
        System.out.println(data[n * n + 1] + "(" + getSize(bottom) + ")");
        System.out.println("Opened:" + opened);
    }

    public static void main(String[] args) {
    }
}