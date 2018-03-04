import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        if (nouns == null || nouns.length == 0) {
            throw new IllegalArgumentException();
        }
        int maxD = 0;
        String goodNoun = null;
        for (String noun : nouns) {
            int d = 0;
            for (String nounOther : nouns) {
                d += wordNet.distance(noun, nounOther);
            }
            System.out.println("---------------");
            System.out.println(d);
            System.out.println(noun);
            if (d >= maxD) {

                maxD = d;
                goodNoun = noun;
            }
        }
        return goodNoun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}