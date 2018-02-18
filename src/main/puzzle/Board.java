import java.util.Iterator;

/**
 * Created by obabichev on 17/02/2018.
 */
public class Board {

    private final int[][] board;
    private final int N;
    private int hammingValue;
    private int manhettanValue;

    public Board(int[][] blocks) {
        N = blocks.length;
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = blocks[i][j];
            }
        }
        countConstants();
    }

    public int dimension() {
        return N;
    }

    private int countHamming() {
        int result = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                if (board[i][j] != i * N + j + 1) {
                    result++;
                }
            }
        }
        return result;
    }

    public int hamming() {
        return hammingValue;
    }

    private int countManhattan() {
        int result = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                result += Math.abs(i - (board[i][j] - 1) / N);
                result += Math.abs(j - (board[i][j] - 1) % N);
            }
        }
        return result;
    }

    public int manhattan() {
        return manhettanValue;
    }

    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == N - 1 && board[i][j] == 0) {
                    continue;
                }
                if (board[i][j] != i * N + j + 1)
                    return false;
            }
        }
        return true;
    }

    public Board twin() {
        final Board result = new Board(this.board);
        if (result.board[0][0] == 0 || result.board[0][1] == 0) {
            result.swap(1, 0, 1, 1);
        } else {
            result.swap(0, 0, 0, 1);
        }
        return result;
    }

    public boolean equals(Object y) {
        return y instanceof Board && isEqual((Board) y);
    }

    private boolean isEqual(Board that) {
        if (dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != that.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new NeighborsIterator();
            }
        };
    }

    private class NeighborsIterator implements Iterator<Board> {

        private Board[] iterBoards;
        private int current = -1;

        public NeighborsIterator() {
            iterBoards = new Board[4];
            int x = -1;
            int y = -1;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == 0) {
                        x = i;
                        y = j;
                    }
                }
            }

            if (x - 1 >= 0) add(copy().swap(x, y, x - 1, y));
            if (x + 1 < N) add(copy().swap(x, y, x + 1, y));
            if (y - 1 >= 0) add(copy().swap(x, y, x, y - 1));
            if (y + 1 < N) add(copy().swap(x, y, x, y + 1));
        }

        private void add(Board board) {
            iterBoards[++current] = board;
        }

        @Override
        public boolean hasNext() {
            return current >= 0;
        }

        @Override
        public Board next() {
            final Board result = iterBoards[current];
            iterBoards[current--] = null;
            return result;
        }
    }

    private Board copy() {
        return new Board(board);
    }

    private Board swap(int i0, int j0, int i1, int j1) {
        int val = board[i0][j0];
        board[i0][j0] = board[i1][j1];
        board[i1][j1] = val;
        countConstants();
        return this;
    }

    private void countConstants() {
        hammingValue = countHamming();
        manhettanValue = countManhattan();
    }

    public String toString() {
        StringBuilder result = new StringBuilder(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result.append(board[i][j] + (j == N - 1 ? "" : " "));
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        final Board board = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
        System.out.println(board);
        System.out.println("Hamming: " + board.hamming());
        System.out.println("Manhettan: " + board.manhattan());
        System.out.println("isGoal: " + board.isGoal());
        System.out.println("self equal: " + board.isEqual(board));
        System.out.println("twin:\n" + board.twin());
        System.out.println("equal:\n" + board.equals(board.twin()));

        System.out.println("========================================");
        final Board board2 = new Board(new int[][]{{0, 2, 3}, {4, 1, 6}, {7, 8, 5}});
        System.out.println("Orig:\n" + board2);

        for (Board iterBoard : board2.neighbors()) {
            System.out.println(iterBoard);
        }

        System.out.println();
    }
}
