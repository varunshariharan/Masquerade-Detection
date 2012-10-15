import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VarunDataTest {

    public double overallAvgFPR = 0.0;
    public double overallAvgTPR = 0.0;
    public int overallAvgSIZE = 0;
    public int count = 0;
    public int outerEnsembleSize;
    String commaSeparatedStatistics;
    public int totalNumberOfChunks;

    @Test
    public void testConvertVarunsSerToNormalFiles() throws IOException, ClassNotFoundException {
        File dir = new File("./data/TrainingAndTestingChunks/");
        File[] files = dir.listFiles();
        String[] chunks = new String[8];

        char letter = 'a';
        for(int i = 1; i < files.length; i+=8){
            chunks[0] = files[i+7].getPath();
            chunks[1] = files[i].getPath();
            chunks[2] = files[i+1].getPath();
            chunks[3] = files[i+2].getPath();
            chunks[4] = files[i+3].getPath();
            chunks[5] = files[i+4].getPath();
            chunks[6] = files[i+5].getPath();
            chunks[7] = files[i+6].getPath();

            FileInputStream[] fileInputStreams = {
                    new FileInputStream(chunks[0]),
                    new FileInputStream(chunks[1]),
                    new FileInputStream(chunks[2]),
                    new FileInputStream(chunks[3]),
                    new FileInputStream(chunks[4]),
                    new FileInputStream(chunks[5]),
                    new FileInputStream(chunks[6]),
                    new FileInputStream(chunks[7])
            };

            ObjectInputStream[] objectInputStreams = {
                    new ObjectInputStream(fileInputStreams[0]),
                    new ObjectInputStream(fileInputStreams[1]),
                    new ObjectInputStream(fileInputStreams[2]),
                    new ObjectInputStream(fileInputStreams[3]),
                    new ObjectInputStream(fileInputStreams[4]),
                    new ObjectInputStream(fileInputStreams[5]),
                    new ObjectInputStream(fileInputStreams[6]),
                    new ObjectInputStream(fileInputStreams[7])
            };

            ArrayList<ArrayList<TraceFileCommand>> commandLists = new ArrayList<ArrayList<TraceFileCommand>>();
            commandLists.add(0,((CommandList)objectInputStreams[0].readObject()).commandList);
            commandLists.add(1,((CommandList)objectInputStreams[1].readObject()).commandList);
            commandLists.add(2,((CommandList)objectInputStreams[2].readObject()).commandList);
            commandLists.add(3,((CommandList)objectInputStreams[3].readObject()).commandList);
            commandLists.add(4,((CommandList)objectInputStreams[4].readObject()).commandList);
            commandLists.add(5,((CommandList)objectInputStreams[5].readObject()).commandList);
            commandLists.add(6,((CommandList)objectInputStreams[6].readObject()).commandList);
            commandLists.add(7,((CommandList)objectInputStreams[7].readObject()).commandList);


            for (int i1 = 0, commandListsSize = commandLists.size(); i1 < commandListsSize; i1++) {
                ArrayList<TraceFileCommand> commandList = commandLists.get(i1);
                FileWriter fileWriter = new FileWriter("OtherTestData/FromVarun/ForConceptDrift/" + letter + i1);
                for (TraceFileCommand traceFileCommand : commandList)
                    fileWriter.write(traceFileCommand.getEntireEntry());
                fileWriter.close();
            }

            letter ++;
        }
    }

    @Test
    public void testResultsOnAllVarunsFiles() throws ClassNotFoundException, IOException {
        File dir = new File("./data/Training and Testing Chunks/");
        File[] files = dir.listFiles();
        String[] chunks = new String[8];
        outerEnsembleSize = 4;
        totalNumberOfChunks = 8;

        for(int i = 0; i < files.length; i+=8){
            chunks[0] = files[i+7].getPath();
            chunks[1] = files[i].getPath();
            chunks[2] = files[i+1].getPath();
            chunks[3] = files[i+2].getPath();
            chunks[4] = files[i+3].getPath();
            chunks[5] = files[i+4].getPath();
            chunks[6] = files[i+5].getPath();
            chunks[7] = files[i+6].getPath();
//            testResultsOnData(chunks);
            testResultsOnDataUnsupervised(chunks);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results/temporaryCommaSeparatedValues.csv"));
        bufferedWriter.write(commaSeparatedStatistics);
        bufferedWriter.close();

        overallAvgFPR /= count;
        overallAvgTPR /= count;
        overallAvgSIZE /= count;

        System.out.println("Overall Average FPR = " + overallAvgFPR);
        System.out.println("Overall Average TPR = " + overallAvgTPR);
        System.out.println("Overall Average SIZE = " + overallAvgSIZE);
    }

    public void testResultsOnData(String[] files) throws IOException, ClassNotFoundException {
        String[] encodedTWithoutSB = new String[8];
        String[] tWithJustAnomalies = new String[8];
        TraceFileReadAndWrite traceFileIO = new TraceFileReadAndWrite(1,0);
        HashMap<String, Double> dictionary;
        HashMap<String, Double> quantisedDictionary = null;
        LZW lzw = new LZW();
        DataStreamPatternRecognizer dataStreamPatternRecognizer;
        String anomalousData;
        ArrayList<Statistics> statistics = new ArrayList<Statistics>();
        int ensembleSize = outerEnsembleSize;
        ArrayList<HashMap<String, Double>> ensemble = new ArrayList<HashMap<String, Double>>(ensembleSize);

        FileInputStream[] fileInputStreams = {
                new FileInputStream(files[0]),
                new FileInputStream(files[1]),
                new FileInputStream(files[2]),
                new FileInputStream(files[3]),
                new FileInputStream(files[4]),
                new FileInputStream(files[5]),
                new FileInputStream(files[6]),
                new FileInputStream(files[7])
        };

        ObjectInputStream[] objectInputStreams = {
                new ObjectInputStream(fileInputStreams[0]),
                new ObjectInputStream(fileInputStreams[1]),
                new ObjectInputStream(fileInputStreams[2]),
                new ObjectInputStream(fileInputStreams[3]),
                new ObjectInputStream(fileInputStreams[4]),
                new ObjectInputStream(fileInputStreams[5]),
                new ObjectInputStream(fileInputStreams[6]),
                new ObjectInputStream(fileInputStreams[7])
        };

        ArrayList<ArrayList<TraceFileCommand>> commandLists = new ArrayList<ArrayList<TraceFileCommand>>();
        commandLists.add(0,((CommandList)objectInputStreams[0].readObject()).commandList);
        commandLists.add(1,((CommandList)objectInputStreams[1].readObject()).commandList);
        commandLists.add(2,((CommandList)objectInputStreams[2].readObject()).commandList);
        commandLists.add(3,((CommandList)objectInputStreams[3].readObject()).commandList);
        commandLists.add(4,((CommandList)objectInputStreams[4].readObject()).commandList);
        commandLists.add(5,((CommandList)objectInputStreams[5].readObject()).commandList);
        commandLists.add(6,((CommandList)objectInputStreams[6].readObject()).commandList);
        commandLists.add(7,((CommandList)objectInputStreams[7].readObject()).commandList);

        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(0));
        encodedTWithoutSB[0] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(1));
        encodedTWithoutSB[1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(2));
        encodedTWithoutSB[2] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(3));
        encodedTWithoutSB[3] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(4));
        encodedTWithoutSB[4] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(5));
        encodedTWithoutSB[5] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(6));
        encodedTWithoutSB[6] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(7));
        encodedTWithoutSB[7] = traceFileIO.retrieveTrainingCommandSequence(false, -1);

        tWithJustAnomalies[0] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[0], commandLists.get(0));
        tWithJustAnomalies[1] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], commandLists.get(1));
        tWithJustAnomalies[2] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], commandLists.get(2));
        tWithJustAnomalies[3] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], commandLists.get(3));
        tWithJustAnomalies[4] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], commandLists.get(4));
        tWithJustAnomalies[5] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], commandLists.get(5));
        tWithJustAnomalies[6] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], commandLists.get(6));
        tWithJustAnomalies[7] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], commandLists.get(7));

        int size = 0;
        for(int  index = 0; index < totalNumberOfChunks-1; index++){
            encodedTWithoutSB[index] = removeAnomalies(encodedTWithoutSB[index], tWithJustAnomalies[index]);
//            dictionary = index == 0 ?
//                    lzw.createDictionary(encodedTWithoutSB[index], new HashMap<String, Double>()) :
//                    lzw.createDictionary(encodedTWithoutSB[index], quantisedDictionary);

            dictionary = lzw.createDictionary(encodedTWithoutSB[index], new HashMap<String, Double>());

            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
            Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 0.2);
            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);

            ensemble.add(quantisedDictionary);

            Statistics bestStatistic = new Statistics();
            Statistics worstStatistic = new Statistics();

            for (HashMap<String, Double> qD : ensemble) {
                anomalousData = dataStreamPatternRecognizer.
                        removePatternsGotFromQuantizedDictionary(qD.keySet(), false, traceFileIO);
                Statistics statistic = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
            }

            if(ensemble.size() == ensembleSize){
                int bestStatisticIndex = 0, worstStatisticIndex = 0;
                for(int i = 0; i < ensembleSize; i++){
                    anomalousData = dataStreamPatternRecognizer.
                            removePatternsGotFromQuantizedDictionary(ensemble.get(i).keySet(), false, traceFileIO);
                    Statistics statistic = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);

                    if((bestStatistic.FPR == 0.0) || (statistic.FPR < bestStatistic.FPR))
                    {
                        bestStatistic = statistic;
                        bestStatisticIndex = i;
                        System.out.println("Set "+i+" as best statistic");
                    }
                    if((statistic.TPR < worstStatistic.TPR) || (worstStatistic.TPR == 0.0)){
                        worstStatistic = statistic;
                        worstStatisticIndex = i;
                        System.out.println("Set "+i+" as worst statistic");
                    }
                }
                ensemble.remove(worstStatisticIndex);
                System.out.println("Removing "+worstStatisticIndex);
            }

            if(index >= 4)
            statistics.add(bestStatistic);
            size += quantisedDictionary.size();
        }

        size /= 7;
        overallAvgSIZE += size;

        Double avgFPR = 0.0, avgTPR = 0.0, avgF1Measure = 0.0, avgPrecision = 0.0;
        Double avgRecall = 0.0, avgFpoint5Measure = 0.0, avgF2Measure = 0.0;
        for (Statistics statistic : statistics) {
            if(statistic.TP == 0.0) continue;
            avgFPR += statistic.FPR;
            avgTPR += statistic.TPR;
            avgPrecision += statistic.precision;
            avgFpoint5Measure += statistic.fpoint5measure;
            avgF1Measure += statistic.f1measure;
            avgF2Measure += statistic.f2measure;
            avgRecall += statistic.recall;
        }

        avgFPR /= statistics.size();
        avgTPR /= statistics.size();
        avgF1Measure /= statistics.size();
        avgF2Measure /= statistics.size();
        avgFpoint5Measure /= statistics.size();
        avgPrecision /= statistics.size();
        avgRecall /= statistics.size();
        System.out.println("Average FPR = " + avgFPR);
        System.out.println("Average TPR = " + avgTPR + "\n");

        commaSeparatedStatistics += avgPrecision + ",";
        commaSeparatedStatistics += avgRecall + ",";
        commaSeparatedStatistics += avgF1Measure + ",";
        commaSeparatedStatistics += avgFpoint5Measure + ",";
        commaSeparatedStatistics += avgF2Measure + "\n";

        overallAvgFPR += avgFPR;
        overallAvgTPR += avgTPR;
        count ++;
    }

    public void testResultsOnDataUnsupervised(String[] files) throws IOException, ClassNotFoundException {
        String[] encodedTWithoutSB = new String[8];
        String[] tWithJustAnomalies = new String[8];
        TraceFileReadAndWrite traceFileIO = new TraceFileReadAndWrite(1,0);
        HashMap<String, Double> dictionary;
        HashMap<String, Double> quantisedDictionary = null;
        LZW lzw = new LZW();
        DataStreamPatternRecognizer dataStreamPatternRecognizer;
        String anomalousData;

        int ensembleSize = outerEnsembleSize;
        int testChunkIndex = 1;
        ArrayList<HashMap<String, Double>> ensemble;

        FileInputStream[] fileInputStreams = {
                new FileInputStream(files[0]),
                new FileInputStream(files[1]),
                new FileInputStream(files[2]),
                new FileInputStream(files[3]),
                new FileInputStream(files[4]),
                new FileInputStream(files[5]),
                new FileInputStream(files[6]),
                new FileInputStream(files[7])
        };

        ObjectInputStream[] objectInputStreams = {
                new ObjectInputStream(fileInputStreams[0]),
                new ObjectInputStream(fileInputStreams[1]),
                new ObjectInputStream(fileInputStreams[2]),
                new ObjectInputStream(fileInputStreams[3]),
                new ObjectInputStream(fileInputStreams[4]),
                new ObjectInputStream(fileInputStreams[5]),
                new ObjectInputStream(fileInputStreams[6]),
                new ObjectInputStream(fileInputStreams[7])
        };

        ArrayList<ArrayList<TraceFileCommand>> commandLists = new ArrayList<ArrayList<TraceFileCommand>>();
        commandLists.add(0,((CommandList)objectInputStreams[0].readObject()).commandList);
        commandLists.add(1,((CommandList)objectInputStreams[1].readObject()).commandList);
        commandLists.add(2,((CommandList)objectInputStreams[2].readObject()).commandList);
        commandLists.add(3,((CommandList)objectInputStreams[3].readObject()).commandList);
        commandLists.add(4,((CommandList)objectInputStreams[4].readObject()).commandList);
        commandLists.add(5,((CommandList)objectInputStreams[5].readObject()).commandList);
        commandLists.add(6,((CommandList)objectInputStreams[6].readObject()).commandList);
        commandLists.add(7,((CommandList)objectInputStreams[7].readObject()).commandList);

//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(0));
//        encodedTWithoutSB[0] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(1));
//        encodedTWithoutSB[1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(2));
//        encodedTWithoutSB[2] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(3));
//        encodedTWithoutSB[3] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(4));
//        encodedTWithoutSB[4] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(5));
//        encodedTWithoutSB[5] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(6));
//        encodedTWithoutSB[6] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(7));
//        encodedTWithoutSB[7] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//
//        tWithJustAnomalies[0] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[0], commandLists.get(0));
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], commandLists.get(1));
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], commandLists.get(2));
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], commandLists.get(3));
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], commandLists.get(4));
//        tWithJustAnomalies[5] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], commandLists.get(5));
//        tWithJustAnomalies[6] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], commandLists.get(6));
//        tWithJustAnomalies[7] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], commandLists.get(7));

        int size = 0;
        ArrayList<Statistics> statistics = new ArrayList<Statistics>();
        Statistics statistic;
        for(testChunkIndex = 1; testChunkIndex < totalNumberOfChunks; testChunkIndex++){
            ensemble = new ArrayList<HashMap<String, Double>>(ensembleSize);

            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(0));
            encodedTWithoutSB[0] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(1));
            encodedTWithoutSB[1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(2));
            encodedTWithoutSB[2] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(3));
            encodedTWithoutSB[3] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(4));
            encodedTWithoutSB[4] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(5));
            encodedTWithoutSB[5] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(6));
            encodedTWithoutSB[6] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom(commandLists.get(7));
            encodedTWithoutSB[7] = traceFileIO.retrieveTrainingCommandSequence(false, -1);

            tWithJustAnomalies[0] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[0], commandLists.get(0));
            tWithJustAnomalies[1] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], commandLists.get(1));
            tWithJustAnomalies[2] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], commandLists.get(2));
            tWithJustAnomalies[3] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], commandLists.get(3));
            tWithJustAnomalies[4] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], commandLists.get(4));
            tWithJustAnomalies[5] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], commandLists.get(5));
            tWithJustAnomalies[6] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], commandLists.get(6));
            tWithJustAnomalies[7] = Utility.
                    returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], commandLists.get(7));

            Statistics insertStatistic = new Statistics();
            System.out.println("Test chunk index = "+testChunkIndex);
            int startIndex = testChunkIndex - ensembleSize;
            if(startIndex < 0) {
                startIndex = 0;
            }

            String[] anomalousDataList = new String[testChunkIndex-startIndex];
            for (int index = startIndex; index < testChunkIndex; index++){
                System.out.println("Index = "+index);
                encodedTWithoutSB[index] = removeAnomalies(encodedTWithoutSB[index], tWithJustAnomalies[index]);
//            dictionary = index == 0 ?
//                    lzw.createDictionary(encodedTWithoutSB[index], new HashMap<String, Double>()) :
//                    lzw.createDictionary(encodedTWithoutSB[index], quantisedDictionary);

                dictionary = lzw.createDictionary(encodedTWithoutSB[index], new HashMap<String, Double>());

                quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 0.2);
                dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[testChunkIndex]);


                ensemble.add(quantisedDictionary);

                Statistics bestStatistic = new Statistics();
                Statistics worstStatistic = new Statistics();

//                for (HashMap<String, Double> qD : ensemble) {
                anomalousDataList[index-startIndex] = dataStreamPatternRecognizer.
                            removePatternsGotFromQuantizedDictionary(ensemble.get(index-startIndex).keySet(), false, traceFileIO);
//                    Statistics statistic = Utility.printAndReturnStatisticsFrom(anomalousDataList[index], tWithJustAnomalies[testChunkIndex]);
//                }

                if(ensemble.size() == ensembleSize){
                    int bestStatisticIndex = 0, worstStatisticIndex = 0;
                    String votingResults = compressAnomalousData(anomalousDataList);
                    for(int i = 0; i < ensembleSize; i++){
//                        anomalousData = dataStreamPatternRecognizer.removePatternsGotFromQuantizedDictionary(ensemble.get(i).keySet(), false, traceFileIO);

                        //TODO replace tWithJustAnomalies with majority voting string
//                        Statistics statistic = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[testChunkIndex]);
                        statistic = Utility.printAndReturnStatisticsFrom(anomalousDataList[i], votingResults);
                        if((bestStatistic.FPR == 0.0) || (statistic.FPR < bestStatistic.FPR))
                        {
                            bestStatistic = statistic;
                            bestStatisticIndex = i;
                            insertStatistic = Utility.printAndReturnStatisticsFrom(anomalousDataList[i], tWithJustAnomalies[testChunkIndex]);
                            System.out.println("Set "+i+" as best statistic");
                        }
                        if((statistic.TPR < worstStatistic.TPR) || (worstStatistic.TPR == 0.0)){
                            worstStatistic = statistic;
                            worstStatisticIndex = i;
                            System.out.println("Set "+i+" as worst statistic");
                        }
                    }
                    statistics.add(insertStatistic);
//                    statistics.add(bestStatistic);
                    size += quantisedDictionary.size();

                    ensemble.remove(worstStatisticIndex);
                    System.out.println("Removing " + worstStatisticIndex);
                }

            }
        }

        size /= (totalNumberOfChunks-1);
        overallAvgSIZE += size;

        Double avgFPR = 0.0, avgTPR = 0.0, avgF1Measure = 0.0, avgPrecision = 0.0;
        Double avgRecall = 0.0, avgFpoint5Measure = 0.0, avgF2Measure = 0.0;
        for (Statistics eachStatistic : statistics) {
            if(eachStatistic.TP == 0.0) continue;
            avgFPR += eachStatistic.FPR;
            avgTPR += eachStatistic.TPR;
            avgPrecision += eachStatistic.precision;
            avgFpoint5Measure += eachStatistic.fpoint5measure;
            avgF1Measure += eachStatistic.f1measure;
            avgF2Measure += eachStatistic.f2measure;
            avgRecall += eachStatistic.recall;
        }

        avgFPR /= statistics.size();
        avgTPR /= statistics.size();
        avgF1Measure /= statistics.size();
        avgF2Measure /= statistics.size();
        avgFpoint5Measure /= statistics.size();
        avgPrecision /= statistics.size();
        avgRecall /= statistics.size();
        System.out.println("Average FPR = " + avgFPR);
        System.out.println("Average TPR = " + avgTPR + "\n");

        commaSeparatedStatistics += avgPrecision + ",";
        commaSeparatedStatistics += avgRecall + ",";
        commaSeparatedStatistics += avgF1Measure + ",";
        commaSeparatedStatistics += avgFpoint5Measure + ",";
        commaSeparatedStatistics += avgF2Measure + "\n";

        overallAvgFPR += avgFPR;
        overallAvgTPR += avgTPR;
        count ++;
    }

    private String compressAnomalousData(String[] anomalousDataList) {
        String returnString;
        char[] returnCharArray = anomalousDataList[0].toCharArray();
        int minLength = 0;
        for(int listIndex = 0; listIndex < anomalousDataList.length; listIndex++){
            if(minLength > anomalousDataList[listIndex].length())
                minLength = anomalousDataList[listIndex].length();
        }
        for(int charIndex = 0; charIndex < minLength;charIndex++){
            int benign = 0; int anomaly = 0;
            for (int index = 0; index < anomalousDataList.length; index++){
                if(anomalousDataList[index].charAt(charIndex) == DataStreamPatternRecognizer.matchedChar)
                    benign++;
                else
                    anomaly++;
            }
            if (benign > anomaly)
                returnCharArray[charIndex] = DataStreamPatternRecognizer.matchedChar;
        }
        returnString = new String(returnCharArray);
        return returnString;
    }

    private String removeAnomalies(String removeAnomalies, String hasJustAnomalies) {
        StringBuilder sb = new StringBuilder(removeAnomalies);
        for(int i = hasJustAnomalies.length() - 1; i >= 0; i--)
            if(hasJustAnomalies.charAt(i) != '.')
                sb.deleteCharAt(i);
        return sb.toString();
    }
}
