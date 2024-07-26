package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    NGramMap ngram;
    WordNet wordnet;

    public HyponymsHandler(String synsetsFile, String hyponymFile, NGramMap nGram) {
        wordnet = new WordNet(synsetsFile, hyponymFile);
        ngram = nGram;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int start = q.startYear();
        int end = q.endYear();
        int i = q.k();

        if (words.size() == 1 && i == 0) {
            return handleForOneWord(wordnet, words, 0).toString();
        }
        if (words.size() > 1 && i == 0) {
            return handleForListOfWords(wordnet, words).toString();
        }
        if (words.size() == 1 && i > 0) {
            TreeSet<String> hyponyms = handleForOneWord(wordnet, words, 0);
            TreeSet<String> listOfKWords = handleForKNotZero(hyponyms, ngram, start, end, i);
            return listOfKWords.toString();
        }
        if (words.size() > 1 && i > 0) {
            TreeSet<String> hyponyms = handleForListOfWords(wordnet, words);
            TreeSet<String> listOfKWords = handleForKNotZero(hyponyms, ngram, start, end, i);
            return listOfKWords.toString();
        }
        return null;
    }

    public TreeSet<String> handleForOneWord(WordNet wordNetInput, List<String> words, int index) {
        String word = words.get(index);
        HashSet<Integer> allIDs = wordNetInput.synsetGraph.wordToID.get(word);
        TreeSet<String> allChildWords = new TreeSet<>();

        if (allIDs == null) {
            return allChildWords;
        } else {
            for (int x : allIDs) {
                allChildWords.addAll(wordNetInput.synsetGraph.getEdgeDFS(x));
            }
        }

        return allChildWords;
    }

    public TreeSet<String> handleForKNotZero(TreeSet<String> hyponyms, NGramMap nGram, int start, int end,
                                             int i) {
        TreeMap<Double, HashSet<String>> mapOfSums = new TreeMap<>();
        TreeSet<String> setOfWords = new TreeSet<>();

        for (String word : hyponyms) {
            if (nGram.countHistory(word, start, end) == null) {
                continue;
            }
            double sumOfWord = sumOfTimeSeries(nGram, word, start, end);
            if (sumOfWord > 0) {
                if (!mapOfSums.containsKey(sumOfWord)) {
                    HashSet<String> newSet = new HashSet<>();
                    newSet.add(word);
                    mapOfSums.put(sumOfWord, newSet);
                } else {
                    mapOfSums.get(sumOfWord).add(word);
                }
            }
        }

        NavigableSet<Double> descendingKeys = mapOfSums.descendingKeySet();
        for (double x : descendingKeys) {
            for (String word : mapOfSums.get(x)) {
                if (setOfWords.size() < i) {
                    setOfWords.add(word);
                }
            }
        }
        return setOfWords;
    }

    public TreeSet<String> handleForListOfWords(WordNet wordNetInput, List<String> words) {
        TreeSet<String> firstTreeSet = handleForOneWord(wordNetInput, words, 0);

        for (int i = 1; i < words.size(); i++) {
            TreeSet<String> nextTreeSet = handleForOneWord(wordNetInput, words, i);
            firstTreeSet.retainAll(nextTreeSet);
        }

        return firstTreeSet;
    }

    public Double sumOfTimeSeries(NGramMap nGram, String word, int start, int end) {
        TimeSeries timeSeries = nGram.countHistory(word, start, end);
        double i = 0;
        for (int year : timeSeries.keySet()) {
            i += timeSeries.get(year);
        }
        return i;
    }
}
