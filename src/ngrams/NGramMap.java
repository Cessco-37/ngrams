package ngrams;
import edu.berkeley.eecs.inst.cs61b.ngrams.StaffNGramMap;

/**
 * A placeholder implementation of NGramMap.
 */
public class NGramMap {
    private final StaffNGramMap staffSolution;

    public NGramMap(String wordsFilename, String countsFilename) {
        this.staffSolution = new StaffNGramMap(wordsFilename, countsFilename);
    }

    public TimeSeries countHistory(String word, int start, int end) {
        TimeSeries h = new TimeSeries();
        h.putAll(staffSolution.countHistory(word, start, end));
        return h;
    }

    public TimeSeries weightHistory(String word, int start, int end) {
        throw new UnsupportedOperationException();
    }
}
