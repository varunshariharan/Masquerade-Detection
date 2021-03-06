import java.util.*;

public class LossyCompression {

    private HashMap<String, Double> dictionary;
    private Set<String> usedDictionaryPhrases;

    public LossyCompression(HashMap<String, Double> dictionary) {
        this.dictionary = dictionary;
    }

    public HashMap<String, Double> createQuantisedDictionaryFromDictionary() {
        HashSet<String> P = new HashSet<String>();
        HashSet<String> w = new HashSet<String>();
        HashSet<String> wMax = new HashSet<String>();
        HashSet<String> wMinusWMax = new HashSet<String>();
        usedDictionaryPhrases = new HashSet<String>();
        String phrase = "";

        do {
            if (w.isEmpty()) {
                phrase = getNextPhraseFromDictionary();
                P.clear();
                P.add(phrase);
            }
            findPhrasesWithDistanceOneFrom(P, w);
            if ( !w.isEmpty() ) {
                findPhrasesFromWWithMaxFreqPlusWordLength(w, wMax);
                wMinusWMax.clear();
                wMinusWMax.addAll(w);

                for (String s : wMax)
                    wMinusWMax.remove(s);

                deleteFromDictionary(wMinusWMax);
                P.clear();
                P.addAll(wMax);

                //if w is same as wMax, then w should be cleared
                if( w.equals(wMax) )
                    w.clear();
            }
        } while ( !"".equals(phrase) );

        return dictionary;
    }

    /*To prevent concurrent modification exception*/
    private String getNextPhraseFromDictionary() {
        for (String entry : dictionary.keySet()) {
            if(usedDictionaryPhrases.contains(entry)) continue;
            usedDictionaryPhrases.add(entry);
            return entry;
        }
        return "";
    }

    private void deleteFromDictionary(HashSet<String> w) {
        for (String phrase : w)
            dictionary.remove(phrase);
    }

    private void findPhrasesFromWWithMaxFreqPlusWordLength(HashSet<String> w, HashSet<String> wMax) {
        double max = 0;
        wMax.clear();
        for (String phrase : w) {
            double freqPlusWordLength = dictionary.get(phrase) + phrase.length();
            if( max == freqPlusWordLength)
                wMax.add(phrase);
            else if ( max < freqPlusWordLength){
                wMax.clear();
                wMax.add(phrase);
                max = freqPlusWordLength;
            }
        }
    }

    private void findPhrasesWithDistanceOneFrom(HashSet<String> P, HashSet<String> w) {
        w.clear();
        EditDistance editDistance = new EditDistance();
        for (String p : P)
            for (String phrase : dictionary.keySet())
                if(editDistance.calculateDistance(p, phrase) == 1)
                    w.add(phrase);
    }
}
