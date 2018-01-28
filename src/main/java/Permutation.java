import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        final RandomizedQueue<String> q = new RandomizedQueue<>();
        final String line = StdIn.readLine();

        final String[] split = line.split(" ");
        for (String word : split) {
            q.enqueue(word);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(q.dequeue());
        }
    }
}