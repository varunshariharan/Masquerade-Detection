import java.util.HashMap;
import java.util.Map;

public class LZW {
    private HashMap<String,Double> dictionary;
    private double weight;

    public LZW() {
        dictionary = new HashMap<String, Double>();
        weight = 1.0;
    }

    public LZW(double w){
        weight = w;
        dictionary = new HashMap<String, Double>();
    }

    public HashMap<String, Double> createDictionary(String dataStream, HashMap<String, Double> initializeDictionaryWith) {
        HashMap<String, Double> oldDictionary = new HashMap<String, Double>();
        for (Map.Entry<String, Double> stringDoubleEntry : initializeDictionaryWith.entrySet())
            oldDictionary.put(stringDoubleEntry.getKey(), stringDoubleEntry.getValue()*weight);
        dictionary.clear();
        dictionary.putAll(oldDictionary);
        int length = dataStream.length();
        String w, wk;

        w = "";
        for(int index = 0; index < length; index++){
            char k = dataStream.charAt(index);
            if(k == TraceFileReadAndWrite.sessionBreak){
                w = "";
                continue;
            }
            wk = w + k;
            if(dictionary.containsKey(wk)) {
                Double frequency = dictionary.get(wk);
                dictionary.remove(wk);
                dictionary.put(wk, frequency + 1);
                w = wk;
            }
            else{
                dictionary.put(wk, 1.0);
                w = String.valueOf(k);
            }
        }

        return dictionary;
    }
}
