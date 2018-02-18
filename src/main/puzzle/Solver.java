import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private class TBoard {
        private Board board;
        private TBoard parent;
        private int moves;

        public TBoard(Board board, TBoard parent, int moves) {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
        }
    }

    private TBoard[] tree = new TBoard[100];
    private int size = 0;
    private TBoard finish = null;

    private MinPQ<TBoard> queue;


    public Solver(Board initial) {
        queue = new MinPQ<>(Comparator.comparingInt(o -> o.board.manhattan() + o.moves));
        final TBoard root = new TBoard(initial, null, 0);
        queue.insert(root);
        addTreeItem(root);

        solve();
    }

    private void solve() {
        TBoard min;
        while (!queue.isEmpty()) {
            min = queue.delMin();
            if (min.board.isGoal()) {
                finish = min;
                return;
            }

            final Iterable<Board> neighbors = min.board.neighbors();
            for (Board neighbor : neighbors) {
                if (min.parent != null && min.parent.board.equals(neighbor)) {
                    continue;
                }
                final TBoard tBoard = new TBoard(neighbor, min, min.moves + 1);
                queue.insert(tBoard);
                addTreeItem(tBoard);
            }
//            System.out.println("Moves: " + min.moves);
//            System.out.println("Manhetten: " + min.board.manhattan());
//            System.out.println("Priority: " + (min.moves + min.board.manhattan()));
//            System.out.println(min.board);
//            System.out.println();
        }
    }

    private void addTreeItem(TBoard tBoard) {
        tree[size++] = tBoard;
    }

    public boolean isSolvable() {
        return finish != null;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return finish.moves;
    }

    public Iterable<Board> solution() {
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new SolutionIterator();
            }
        };
    }

    private class SolutionIterator implements Iterator<Board> {

        private Board[] boards;
        private int current = 0;


        public SolutionIterator() {
            TBoard end = finish;
            boards = new Board[end.moves + 1];
            for (int i = end.moves; i >= 0; i--) {
                boards[i] = end.board;
                end = end.parent;
            }
        }

        @Override
        public boolean hasNext() {
            return current < boards.length;
        }

        @Override
        public Board next() {
            return boards[current++];
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solution the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}