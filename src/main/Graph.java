package main;

import java.util.*;

public class Graph {
    HashMap<Integer, Node> IDtoWord = new HashMap<>();
    HashMap<String, HashSet<Integer>> wordToID = new HashMap<>();
    HashSet<Integer> IDset;

    private class Node {
        List<String> synset;
        TreeSet<Integer> neighbours;
        Node(String text) {
            neighbours = new TreeSet<>();
            synset = Arrays.asList(text.split(" "));
        }
    }

    public void addEdge(int parentID, int childID) {
        IDtoWord.get(parentID).neighbours.add(childID);
    }

    public Set<String> getEdgeDFS(int parentID) {
        Set setIDs = new TreeSet();
        return retrieveWords(parentID, setIDs);
    }

    public Node createNode(int ID, String synset) {
        Node newNode = new Node(synset);
        IDtoWord.put(ID, newNode);

        String[] synsetWords = synset.split(" ");
        for (String word : synsetWords) {
            if (!wordToID.containsKey(word)) {
                IDset = new HashSet<Integer>();
                IDset.add(ID);
                wordToID.put(word, IDset);
            } else {
                wordToID.get(word).add(ID);
            }
        }
        return newNode;
    }

    public Set<String> retrieveWords(int rootNodeId, Set collectedWords) {
        Node relevantNode = IDtoWord.get(rootNodeId);
        collectedWords.addAll(relevantNode.synset);

        if (!relevantNode.neighbours.isEmpty()) {
            for (int childNodeId : relevantNode.neighbours) {
                retrieveWords(childNodeId, collectedWords);
            }
        }

        return collectedWords;
    }
}
