import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Map;

public class WordNet {

    private Map<Integer, String> int2word;
    private Map<String, Integer> word2int;
    private Digraph g;
    private SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        int2word = new HashMap<>();
        word2int = new HashMap<>();
        final In in = new In(synsets);
        String s;
        String[] split = null;
        int numberVertex = 0;
        while ((s = in.readLine()) != null) {
            split = s.split(",");
            int2word.put(Integer.parseInt(split[0]), split[1]);
            for (String s1 : split[1].split(" ")) {
                word2int.put(s1, Integer.parseInt(split[0]));
            }
        }
        if (split == null) {
            throw new IllegalArgumentException();
        }


        numberVertex = Integer.parseInt(split[0]);
        g = new Digraph(numberVertex + 1);
        final In in1 = new In(hypernyms);
        while ((s = in1.readLine()) != null) {
            split = s.split(",");
            for (int i = 1; i < split.length; i++) {
                g.addEdge(Integer.parseInt(split[0]), Integer.parseInt(split[i]));
            }
        }

        sap = new SAP(g);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2int.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return word2int.keySet().contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return sap.length(word2int.get(nounA), word2int.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        final int ancestor_id = sap.ancestor(word2int.get(nounA), word2int.get(nounB));
        return int2word.get(ancestor_id);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
    }
}