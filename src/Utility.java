import java.io.IOException;
import java.util.*;

public class Utility {
    public static HashSet<String> findUniqueCommandsInFileBButNotInFileA(String fileA, String fileB) throws IOException {
        TraceFileReadAndWrite traceFileIOForA = new TraceFileReadAndWrite(3, 1);
        TraceFileReadAndWrite traceFileIOForB = new TraceFileReadAndWrite(3, 1);

        List<String> commandListA = traceFileIOForA.retrieveCommandSequenceAndCreateMapFrom("in/" + fileA);
        List<String> commandListB = traceFileIOForB.retrieveCommandSequenceAndCreateMapFrom("in/" + fileB);

        HashSet<String> commandSetA = new HashSet<String>(commandListA);
        HashSet<String> commandSetB = new HashSet<String>(commandListB);

        commandSetB.removeAll(commandSetA);
        return commandSetB;
    }

    public static String returnActualCommandSequenceWithJustAnomalies(String commandSequence, int[] anomalousDataIndices) {
        StringBuilder actualCommandSequence = new StringBuilder(commandSequence);
        int crawler = 0;
        for (int index = 0, length = anomalousDataIndices.length; index < length; index+=2) {
            while(crawler < anomalousDataIndices[index]){
                actualCommandSequence.setCharAt(crawler, DataStreamPatternRecognizer.matchedChar);
                crawler++;
            }
            crawler = anomalousDataIndices[index + 1] + 1;
        }
        while (crawler < actualCommandSequence.length()){
            actualCommandSequence.setCharAt(crawler, DataStreamPatternRecognizer.matchedChar);
            crawler++;
        }
        return actualCommandSequence.toString();
    }

    public static Statistics printAndReturnStatisticsFrom(String anomalousData, String actualCommandSequence) {
        Statistics statistics = new Statistics();
        for(int index = 0; index < Math.min(anomalousData.length(), actualCommandSequence.length()); index++){
            if((anomalousData.charAt(index) != DataStreamPatternRecognizer.matchedChar) &&
                    (anomalousData.charAt(index) == actualCommandSequence.charAt(index)))
                statistics.TP++;

            if((actualCommandSequence.charAt(index) == DataStreamPatternRecognizer.matchedChar) &&
                    (anomalousData.charAt(index) != DataStreamPatternRecognizer.matchedChar))
                statistics.FP++;

            if((actualCommandSequence.charAt(index) == DataStreamPatternRecognizer.matchedChar) &&
                    (anomalousData.charAt(index) == DataStreamPatternRecognizer.matchedChar))
                statistics.TN++;

            if((actualCommandSequence.charAt(index) != DataStreamPatternRecognizer.matchedChar) &&
                    (anomalousData.charAt(index) == DataStreamPatternRecognizer.matchedChar))
                statistics.FN++;
        }

        statistics.calculateRates();

//        System.out.println("TP: " + statistics.TP);
//        System.out.println("FP: " + statistics.FP);
//        System.out.println("TN: " + statistics.TN);
//        System.out.println("FN: " + statistics.FN);
//        System.out.println("TPR: " + statistics.TPR);
//        System.out.println("FPR: " + statistics.FPR);
//        System.out.println("TNR: " + statistics.TNR);
//        System.out.println("FNR: " + statistics.FNR + "\n");

        return statistics;
    }

    public static void discardEntriesWithFreqPlusLengthLessThan(HashMap<String, Double> quantisedDictionary, int threshold) {
//        HashSet<String> phrases = new HashSet<String>();
//        for (Map.Entry<String, Double> entry : quantisedDictionary.entrySet()) {
////            if(entry.getKey().length() <= threshold)
////                phrases.add(entry.getKey());
//            if(entry.getKey().length() + entry.getValue() <= threshold)
//                phrases.add(entry.getKey());
//        }
//        for (String phrase : phrases) {
//            quantisedDictionary.remove(phrase);
        Object[] objects = quantisedDictionary.entrySet().toArray();
        Arrays.sort(objects, new ValueComparator());
        int count = 0 , limit = (int) ((7.0/100.0)*(double)quantisedDictionary.size());
        for (Object object : objects) {
            Map.Entry<String, Double> entry = (Map.Entry<String, Double>) object;
            quantisedDictionary.remove(entry.getKey());
            if((count++) == limit)
                break;
        }
    }

    public static void discardEntriesWithFreqPlusLengthLessThan(HashMap<String, Double> quantisedDictionary, double threshold) {
//        HashSet<String> phrases = new HashSet<String>();
//        for (Map.Entry<String, Double> entry : quantisedDictionary.entrySet()) {
////            if(entry.getKey().length() <= threshold)
////                phrases.add(entry.getKey());
//            if(entry.getKey().length() + entry.getValue() <= threshold)
//                phrases.add(entry.getKey());
//        }
//        for (String phrase : phrases) {
//            quantisedDictionary.remove(phrase);
        Object[] objects = quantisedDictionary.entrySet().toArray();
        Arrays.sort(objects, new ValueComparator());
        int count = 0 , limit = (int) (threshold*(double)quantisedDictionary.size());
        for (Object object : objects) {
            Map.Entry<String, Double> entry = (Map.Entry<String, Double>) object;
            quantisedDictionary.remove(entry.getKey());
            if((count++) == limit)
                break;
        }
    }

    public static String returnActualCommandSequenceWithJustAnomalies(String commandSequence, ArrayList<TraceFileCommand> traceFileCommands) {
        StringBuilder resultingCommandSequence = new StringBuilder(commandSequence);
        for (int i = 0, traceFileCommandsSize = traceFileCommands.size(); i < traceFileCommandsSize - 1; i++) {
            TraceFileCommand traceFileCommand = traceFileCommands.get(i);
            if(traceFileCommand.getLabel())
                resultingCommandSequence.setCharAt(i, DataStreamPatternRecognizer.matchedChar);
        }
        return resultingCommandSequence.toString();
    }
}
