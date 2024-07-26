package main;

import edu.princeton.cs.algs4.In;

public class WordNet {

    public Graph synsetGraph;

    public WordNet(String synsetsFileName, String hyponymFileName) {
        In fileOfSynsets = new In(synsetsFileName);
        In fileOfHyponyms = new In(hyponymFileName);
        synsetGraph = new Graph();

        while (fileOfSynsets.hasNextLine()) {
            String currentSynsetLine = fileOfSynsets.readLine();
            if (!currentSynsetLine.isEmpty()) {
                String[] synsetEntries = currentSynsetLine.split(",");
                int idOfSynset = Integer.parseInt(synsetEntries[0]);
                String synsetValue = synsetEntries[1];
                synsetGraph.createNode(idOfSynset, synsetValue);
            }
        }

        while (fileOfHyponyms.hasNextLine()) {
            String currentHyponymLine = fileOfHyponyms.readLine();
            if (!currentHyponymLine.isEmpty()) {
                String[] hyponymEntries = currentHyponymLine.split(",");
                int mainSynsetId = Integer.parseInt(hyponymEntries[0]);
                for (int idx = 1; idx < hyponymEntries.length; idx++) {
                    int hyponymId = Integer.parseInt(hyponymEntries[idx]);
                    synsetGraph.addEdge(mainSynsetId, hyponymId);
                }
            }
        }
    }
}
