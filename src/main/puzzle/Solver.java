import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private MinPQ<Board> queue;
    private MinPQ<Board> twinQueue;

    private Board[] solution = new Board[2];
    private Board[] twinSolution = new Board[2];
    private int size = 0;

    public Solver(Board initial) {
        queue = new MinPQ<>(Comparator.comparingInt(Board::manhattan));
        twinQueue = new MinPQ<>(Comparator.comparingInt(Board::manhattan));

        queue.insert(initial);
        twinQueue.insert(initial.twin());

        solving();
    }

    private void solving() {
        Board min = queue.min();
        if (min.isGoal()) {
            add(min, null);
        }

        while (!min.isGoal()) {
            min = queue.delMin();
            Board twinMin = twinQueue.delMin();
            if (twinMin.isGoal()) {
                solution = null;
                twinSolution = null;
                queue = null;
                twinQueue = null;
                return;
            }
            add(min, twinMin);

            Iterable<Board> neighbors = min.neighbors();

            for (Board neighbor : neighbors) {
                if (isContain(solution, neighbor)) {
                    continue;
                }
                queue.insert(neighbor);
            }

            neighbors = twinMin.neighbors();
            for (Board neighbor : neighbors) {
                if (isContain(twinSolution, neighbor)) {
                    continue;
                }
                twinQueue.insert(neighbor);
            }
        }
    }

    private boolean isContain(Board[] boards, Board board) {
        for (int i = 0; i < size; i++) {
            if (boards[i].equals(board)) {
                return true;
            }
        }
        return false;
    }

    private void add(Board board, Board twinBoard) {
        if (solution.length == size) {
            resize();
        }
        solution[size] = board;
        twinSolution[size] = twinBoard;
        size++;
    }

    private void resize() {
        Board[] newSolve = new Board[size * 2];
        Board[] newTwinQueue = new Board[size * 2];

        for (int i = 0; i < size; i++) {
            newSolve[i] = solution[i];
            newTwinQueue[i] = twinSolution[i];
        }

        solution = newSolve;
        twinSolution = newTwinQueue;
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return size - 1;
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

        int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public Board next() {
            return solution[current++];
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