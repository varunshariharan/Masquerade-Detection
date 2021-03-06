import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import etm.core.renderer.SimpleTextRenderer;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class IntegrationTest {
    private static EtmMonitor monitor;

    private TraceFileReadAndWrite traceFileIO;
    private String traceFile;
    private String anomalousData;
    private String encodedTrainingCommandSequence;
    private String encodedTestingCommandSequence;
    private HashMap<String,Double> dictionary;
    private HashMap<String,Double> quantisedDictionary;
    private DataStreamPatternRecognizer dataStreamPatternRecognizer;

//    @Ignore
//    @Test
//    public void testProgramFlow() throws IOException {
//        traceFileIO = new TraceFileReadAndWrite(3, 1);// 3:1 training and test data
//        traceFile = "novice-1-injectedWithScientist1Commands";
//
//        //retrieve command sequence from file and encode it
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTrainingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//        encodedTestingCommandSequence = traceFileIO.retrieveTestingCommandSequence();
//
//        //create dictionary from encoded sequence
//        LZW lzw = new LZW();
//        dictionary = lzw.createDictionary(encodedTrainingCommandSequence, new HashMap<String, Integer>());
//
//        //create quantized dictionary from dictionary
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//
//        //decode and write quantized dictionary to file
//        traceFileIO.writeDictionaryToFile(quantisedDictionary, traceFile);
//
//        //get anomalous data from testing sequence
//        dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTestingCommandSequence);
//        anomalousData = dataStreamPatternRecognizer.
//                removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//
//        //print anomalous data in testing sequence
//        dataStreamPatternRecognizer.writeAnomalousDataToFile(traceFile, traceFileIO );
//        //print TP, TN, FP, FN, TPR, FPR values
//        String commandSequenceWithJustAnomalies = Utility.returnActualCommandSequenceWithJustAnomalies(
//                encodedTestingCommandSequence, new int[]{395, 405, 436, 456});
//        System.out.println(traceFileIO.decodeCommandsFromString(anomalousData));
//        System.out.println(traceFileIO.decodeCommandsFromString(commandSequenceWithJustAnomalies));
//        Utility.printAndReturnStatisticsFrom(anomalousData, commandSequenceWithJustAnomalies);
//    }
//
//    @Test
//    public void testMultipleFileQuantizationWorksAsExpected() throws IOException {
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1, 0);
//
//        traceFile = "test-1";
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTrainingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//        dictionary = lzw.createDictionary(encodedTrainingCommandSequence, new HashMap<String, Integer>());
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//
//        traceFile = "test-2";
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTrainingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//        dictionary = lzw.createDictionary(encodedTrainingCommandSequence, quantisedDictionary);
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//
//        traceFileIO.writeDictionaryToFile(quantisedDictionary, "unifiedQuantizedDictionary");
//    }
//
//    @Test
//    public void testResultsWhenMoreAndMoreTrainingDataAreIncludedGradually() throws IOException {
//        String commandSequenceWithJustAnomalies;
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1, 0);
//        String directory = "2";
//
//        traceFile = directory + "/testing";
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTestingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(false, -1);//don't mind the discrepancy for now
//        commandSequenceWithJustAnomalies = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTestingCommandSequence, new int[]{912, 959});
//
//        traceFile = directory + "/T1";
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTrainingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//        dictionary = lzw.createDictionary(encodedTrainingCommandSequence, new HashMap<String, Integer>());
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//
//        dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTestingCommandSequence);
//        anomalousData = dataStreamPatternRecognizer.
//                removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//        dataStreamPatternRecognizer.writeAnomalousDataToFile("T1", traceFileIO);
//
//        //include T2 to above dictionary and try
//        traceFile = directory + "/T2";
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTrainingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//        dictionary = lzw.createDictionary(encodedTrainingCommandSequence, quantisedDictionary);
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//
//        dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTestingCommandSequence);
//        anomalousData = dataStreamPatternRecognizer.
//                removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//        dataStreamPatternRecognizer.writeAnomalousDataToFile("T1T2", traceFileIO);
//        Utility.printAndReturnStatisticsFrom(anomalousData, commandSequenceWithJustAnomalies);
//
//        //include T3 to above and try
//        traceFile = directory + "/T3";
//        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//        encodedTrainingCommandSequence = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//        dictionary = lzw.createDictionary(encodedTrainingCommandSequence, quantisedDictionary);
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//
//        dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTestingCommandSequence);
//        anomalousData = dataStreamPatternRecognizer.
//                removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//        dataStreamPatternRecognizer.writeAnomalousDataToFile("T1T2T3", traceFileIO);
//        Utility.printAndReturnStatisticsFrom(anomalousData, commandSequenceWithJustAnomalies);
//        System.out.println(anomalousData.length() + " "+commandSequenceWithJustAnomalies.length());
//    }
//
//    @Test
//    public void testResultsWithStaticTraining() throws IOException {
//        //anomalous data locations- T3: 954 - 997 T4: 762 - 801 T5: 949 - 986 T6: 939 - 989
//
//        boolean discardSmallFreqPlusLength = true;
//        String[] tWithJustAnomalies = new String[6];//only T3..T6 have anomalies
//        String[] encodedTWithSB = new String[6];
//        String[] encodedTWithoutSB = new String[6];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 1; filenum <= 5; filenum++){
//            traceFile = "3/T"+filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{954, 972});//954, 997
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{762, 776});//762, 801
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{939, 963});//939 ,988
//
//        //T1 on T2, T1T2 on T3, T1T2T3 on T4, T1T2T3T4 on T5, T1T2T3T4T5 on T6
//        dictionary = new LZW().createDictionary(encodedTWithSB[0], new HashMap<String, Integer>());
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//        for(int index = 0; index < 4; index++){
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
//        }
//    }
//
//    @Test
//    public void testResultsWithDynamicTraining() throws IOException {
//        //anomalous data locations- T3: 954 - 997 T4: 762 - 801 T5: 949 - 986 T6: 939 - 989
//
//        boolean discardSmallFreqPlusLength = true;
//        String[] tWithJustAnomalies = new String[6];//only T3..T6 have anomalies
//        String[] encodedTWithSB = new String[6];
//        String[] encodedTWithoutSB = new String[6];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 1; filenum <= 5; filenum++){
//            traceFile = "3/T"+filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{954, 972});//954, 997
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{762, 776});//762, 801
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{939, 963});//939 ,988
//
//        //T1 on T2, T1T2 on T3, T1T2T3 on T4, T1T2T3T4 on T5
//        for(int index = 0; index < 4; index++){
//
//            if(index == 0)
//                dictionary = lzw.createDictionary(encodedTWithSB[index], new HashMap<String, Integer>());
//            else
//                dictionary = lzw.createDictionary(encodedTWithSB[index], quantisedDictionary);
//            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
//
//        }
//    }
//
//    @Test
//    public void testResultsWithStaticTrainingAndAnomalousDataOnlyOnT5() throws IOException {
//        boolean discardSmallFreqPlusLength = true;
//        String[] tWithJustAnomalies = new String[6];//only T3..T6 have anomalies
//        String[] encodedTWithSB = new String[6];
//        String[] encodedTWithoutSB = new String[6];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 1; filenum <= 5; filenum++){
//            traceFile = "4/T"+filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{});
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{});
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{762, 874});   //977
//
////        T1 on T2, T2 on T3, T3 on T4, T4 on T5
//        for(int index = 0; index < 4; index++){
//            dictionary = new LZW().createDictionary(encodedTWithSB[index], new HashMap<String, Integer>());
//
//            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
//
//        }
//        utilityToFindIndicesOfAnomalousCodeInjected(encodedTWithoutSB[4]);
//    }
//
//    @Test
//    public void testResultsWithDynamicTrainingAndAnomalousDataOnlyOnT5() throws IOException {
//        boolean discardSmallFreqPlusLength = true;
//        String[] tWithJustAnomalies = new String[6];//only T3..T6 have anomalies
//        String[] encodedTWithSB = new String[6];
//        String[] encodedTWithoutSB = new String[6];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 1; filenum <= 5; filenum++){
//            traceFile = "4/T"+filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum-1] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{});
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{});
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{762, 874});   //977
//
////        T1 on T2, T1T2 on T3, T1T2T3 on T4, T1T2T3T4 on T5, T1T2T3T4T5 on T6
//        for(int index = 0; index < 4; index++){
//            dictionary = (index == 0) ?
//                    lzw.createDictionary(encodedTWithSB[index], new HashMap<String, Integer>()) :
//                    lzw.createDictionary(encodedTWithSB[index], quantisedDictionary);
//
//            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
//
//        }
//        utilityToFindIndicesOfAnomalousCodeInjected(encodedTWithoutSB[4]);
//    }
//
//    @Test
//    public void testResultsWithStaticTrainingOnFolder5() throws IOException {
//        boolean discardSmallFreqPlusLength = false;
//        String[] tWithJustAnomalies = new String[6];//only T2..T5 have anomalies
//        String[] encodedTWithSB = new String[6];
//        String[] encodedTWithoutSB = new String[6];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//        int jump = 25;
//
////        traceFile = "5(IncrementalAnomalies)/25/T5";
////        traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
////        String temp = traceFileIO.retrieveTrainingCommandSequence(false, -1);
////        utilityToFindIndicesOfAnomalousCodeInjected(temp);
//
//        //get all training data with and without Session Breaks
//            for(int filenum = 0; filenum <= 5; filenum++){
//            traceFile = "5(IncrementalAnomalies)/" + jump + "/T" + filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        //T2_669 T3_658 T4_685 T5_680
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{669, 669 + jump - 1});
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{658, 658 + jump - 1});
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{685, 685 + jump - 1});
//        tWithJustAnomalies[5] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{680, 680 + jump - 1});
//
//        dictionary = new LZW().createDictionary(encodedTWithSB[0], new HashMap<String, Integer>());
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//        for(int index = 1; index <= 5; index++){
////            dictionary = new LZW().createDictionary(encodedTWithSB[index - 1], new HashMap<String, Integer>());
////            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index]);
//        }
//    }
//
//    @Test
//    public void testResultsWithDynamicTrainingOnFolder5() throws IOException {
//        boolean discardSmallFreqPlusLength = false;
//        String[] tWithJustAnomalies = new String[6];//only T2..T5 have anomalies
//        String[] encodedTWithSB = new String[6];
//        String[] encodedTWithoutSB = new String[6];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//        int jump = 100;
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 0; filenum <= 5; filenum++){
//            traceFile = "5(IncrementalAnomalies)/" + jump + "/T" + filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        //T2_669 T3_658 T4_685 T5_680
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{669, 669 + jump - 1});
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{658, 658 + jump - 1});
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{685, 685 + jump - 1});
//        tWithJustAnomalies[5] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{680, 680 + jump - 1});
//
//        //T1 on T2, T1T2 on T3, T1T2T3 on T4, T1T2T3T4 on T5
//        for(int index = 0; index <= 4; index++){//train on index, test on index + 1
//
//            if(index == 0)
//                dictionary = lzw.createDictionary(encodedTWithSB[index], new HashMap<String, Integer>());
//            else
//                dictionary = lzw.createDictionary(encodedTWithSB[index], quantisedDictionary);
//            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
//        }
//    }
//
//    @Test
//    public void testResultsWIthStaticTrainingOnFolder6() throws IOException {
//        boolean discardSmallFreqPlusLength = false;
//        String[] tWithJustAnomalies = new String[8];
//        String[] encodedTWithSB = new String[8];
//        String[] encodedTWithoutSB = new String[8];
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 0; filenum <= 7; filenum++){
//            traceFile = "6(WithConceptDriftHighAnomaly)/T"+filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        //NCDHA T4_463-562 T7_355-448
//        //NCDLA T4_456-469 T7_355-448
//        //WCDHA T4_556-655 T7_565-658
//        //WCDLA T4_549-562 T7_565-658
//
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{});
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{});
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{556, 655});
//        tWithJustAnomalies[5] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{});
//        tWithJustAnomalies[6] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], new int[]{});
//        tWithJustAnomalies[7] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], new int[]{565, 658});
//
//        dictionary = new LZW().createDictionary(encodedTWithSB[0], new HashMap<String, Integer>());
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//        for(int index = 1; index <= 7; index++){
////            dictionary = new LZW().createDictionary(encodedTWithSB[index - 1], new HashMap<String, Integer>());
////            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index]);
//        }
//    }
//
//    @Test
//    public void testResultsWithDynamicTrainingOnFolder6() throws IOException {
//        boolean discardSmallFreqPlusLength = false;
//        String[] tWithJustAnomalies = new String[8];
//        String[] encodedTWithSB = new String[8];
//        String[] encodedTWithoutSB = new String[8];
//        boolean enhancement = false, skip = false;
//        LZW lzw = new LZW();
//        traceFileIO = new TraceFileReadAndWrite(1,0);
//        Statistics prevStatistics = new Statistics();
//        Statistics currStatistics;
//        prevStatistics.TP = 10000.0;
//
//        //get all training data with and without Session Breaks
//        for(int filenum = 0; filenum <= 7; filenum++){
//            traceFile = "6(WithConceptDriftHighAnomaly)/T"+filenum;
//            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
//            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
//            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
//        }
//
//        //NCDHA T4_463-562 T7_355-448
//        //NCDLA T4_456-469 T7_355-448
//        //WCDHA T4_556-655 T7_565-658
//        //WCDLA T4_549-562 T7_565-658
//
//        tWithJustAnomalies[1] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
//        tWithJustAnomalies[2] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{});
//        tWithJustAnomalies[3] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{});
//        tWithJustAnomalies[4] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{556, 655});
//        tWithJustAnomalies[5] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{});
//        tWithJustAnomalies[6] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], new int[]{});
//        tWithJustAnomalies[7] = Utility.
//                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], new int[]{565, 658});
//
//        for(int index = 0; index < 7; index++){//train on index, test on index + 1
//            if(index == 0)
//                dictionary = lzw.createDictionary(encodedTWithSB[index], new HashMap<String, Integer>());
//            if((index > 0) && (!skip))
//                dictionary = lzw.createDictionary(encodedTWithSB[index], quantisedDictionary);
//            if(!skip) quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
//            skip = false;
//            if(discardSmallFreqPlusLength)
//                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
//            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + 1]);
//            anomalousData = dataStreamPatternRecognizer.
//                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
//            currStatistics = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
//            prevStatistics = currStatistics;
//        }
//    }

    @Test
    public void testResultsWIthStaticTrainingOnFolder7() throws IOException {
        boolean discardSmallFreqPlusLength = false;
        String[] tWithJustAnomalies = new String[8];
        String[] encodedTWithSB = new String[8];
        String[] encodedTWithoutSB = new String[8];
        LZW lzw = new LZW();
        traceFileIO = new TraceFileReadAndWrite(1,0);

        //get all training data with and without Session Breaks
        for(int filenum = 0; filenum <= 7; filenum++){
            traceFile = "7(WithConceptDrift40AnomalyEach)/T"+filenum;
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        }

        //NCDHA T4_463-562 T7_355-448
        //NCDLA T4_456-469 T7_355-448
        //WCDHA T4_556-655 T7_565-658
        //WCDLA T4_549-562 T7_565-658
        //WCD25A T4_556-580 T7_565-589
        //NCD25A T4_463-486 T7_355-379
        //WCD40A T4_556-595 T7_565-605

        tWithJustAnomalies[1] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{encodedTWithoutSB[1].length()-40, encodedTWithoutSB[1].length()-1});
        tWithJustAnomalies[2] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{encodedTWithoutSB[2].length()-40, encodedTWithoutSB[2].length()-1});
        tWithJustAnomalies[3] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{encodedTWithoutSB[3].length()-40, encodedTWithoutSB[3].length()-1});
        tWithJustAnomalies[4] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{encodedTWithoutSB[4].length()-40, encodedTWithoutSB[4].length()-1});
        tWithJustAnomalies[5] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{encodedTWithoutSB[5].length()-40, encodedTWithoutSB[5].length()-1});
        tWithJustAnomalies[6] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], new int[]{encodedTWithoutSB[6].length()-40, encodedTWithoutSB[6].length()-1});
        tWithJustAnomalies[7] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], new int[]{encodedTWithoutSB[7].length()-40, encodedTWithoutSB[7].length()-1});

//        dictionary = new LZW().createDictionary(encodedTWithSB[0], new HashMap<String, Integer>());
//        quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
        ArrayList<Statistics> statisticses = new ArrayList<Statistics>();
        for(int index = 1; index <= 7; index++){
            dictionary = new LZW().createDictionary(encodedTWithSB[index - 1], new HashMap<String, Double>());
            quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
            if(discardSmallFreqPlusLength)
                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, 4);
            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index]);
            anomalousData = dataStreamPatternRecognizer.
                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
            Statistics statistics = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index]);
            statisticses.add(statistics);
        }
        double precision = 0.0, recall = 0.0, f1measure = 0.0, fpoint5measure = 0.0, f2measure = 0.0;
        for (Statistics statisticse : statisticses) {
            precision += statisticse.precision;
            recall += statisticse.recall;
            f1measure += statisticse.f1measure;
            fpoint5measure += statisticse.fpoint5measure;
            f2measure += statisticse.f2measure;
        }

        precision /= statisticses.size();
        recall /= statisticses.size();
        f1measure /= statisticses.size();
        fpoint5measure /= statisticses.size();
        f2measure /= statisticses.size();
        System.out.println("\n" + precision + " " + recall + " " + f1measure + " " + fpoint5measure + " " + f2measure);
    }

    public void testResultsWithDynamicTrainingOnFolder7(double v) throws IOException {
        boolean discardSmallFreqPlusLength = true;
        String[] tWithJustAnomalies = new String[8];
        String[] encodedTWithSB = new String[8];
        String[] encodedTWithoutSB = new String[8];
        boolean enhancement = true, skip = false;
        LZW lzw = new LZW(0.7);
        traceFileIO = new TraceFileReadAndWrite(1,0);
        ArrayList<Statistics> currStatistics = new ArrayList<Statistics>();
        double avgSize = 0.0;

        //get all training data with and without Session Breaks
        for(int filenum = 0; filenum <= 7; filenum++){
            traceFile = "7(WithConceptDrift40Anomaly)/T"+filenum;
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        }

        //NCDHA T4_463-562 T7_355-448
        //NCDLA T4_456-469 T7_355-448
        //WCDHA T4_556-655 T7_565-658
        //WCDLA T4_549-562 T7_565-658
        //WCD25A T4_556-580 T7_565-589
        //NCD25A T4_463-486 T7_355-379
        //WCD40A T4_556-595 T7_565-605

        tWithJustAnomalies[0] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[0], new int[]{});
        tWithJustAnomalies[1] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
        tWithJustAnomalies[2] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{});
        tWithJustAnomalies[3] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{});
        tWithJustAnomalies[4] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{encodedTWithoutSB[4].length()-40, encodedTWithoutSB[4].length()-1});
        tWithJustAnomalies[5] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{});
        tWithJustAnomalies[6] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], new int[]{});
        tWithJustAnomalies[7] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], new int[]{encodedTWithoutSB[7].length()-40, encodedTWithoutSB[7].length()-1});

        boolean trainOnSelf = false;
        for(int index = 0; index < (trainOnSelf?8:7); index++){//train on index, test on index + 1
//            if(index == 0)
                dictionary = lzw.createDictionary(encodedTWithSB[index], new HashMap<String, Double>());
//            if((index > 0) && (!skip))
//                dictionary = lzw.createDictionary(encodedTWithSB[index], quantisedDictionary);
            if(!skip) quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
            avgSize += quantisedDictionary.size();
            skip = false;
            if(discardSmallFreqPlusLength)
                Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, v);
            if(enhancement){
                switch (index){
                    case 7:
                    case 4:{
                        dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + (trainOnSelf?0:1)].substring(0, encodedTWithoutSB[index + (trainOnSelf?0:1)].length()-40));
                        }
                    break;
                    default:
                        dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + (trainOnSelf?0:1)]);
                }
            }
            else
                dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + (trainOnSelf?0:1)]);
            anomalousData = dataStreamPatternRecognizer.
                    removePatternsGotFromQuantizedDictionary(quantisedDictionary.keySet(), false, traceFileIO);
            currStatistics.add( Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + (trainOnSelf?0:1)]) );
        }

        double avgFPR = 0.0, avgTPR = 0.0, avgPrecision = 0.0, avgRecall = 0.0, avgF1mMeasure = 0.0;
        double avgFpoint5Measure = 0.0, avgF2Measure = 0.0;
        for (Statistics statistic : currStatistics) {
            if(statistic.TP == 0) continue;
            avgPrecision += statistic.precision;
            avgRecall += statistic.recall;
            avgFpoint5Measure += statistic.fpoint5measure;
            avgF1mMeasure += statistic.f1measure;
            avgF2Measure += statistic.f2measure;
            avgFPR += statistic.FPR;
            avgTPR += statistic.TP > 0 ? statistic.TPR : 0.0;
        }
        avgFPR /= 2;
        avgTPR /= 2;
        avgFpoint5Measure /=2;
        avgF1mMeasure /= 2;
        avgF2Measure /= 2;
        avgPrecision /= 2;
        avgRecall /= 2;
        avgSize /= 2;
//        System.out.println("Average FPR = " + avgFPR + "\nAverage TPR = " + avgTPR + "\nAverage Size = " + avgSize + "\n");
        System.out.println("Average Precision = " + avgPrecision + "\nAverage Recall = " + avgRecall + "\nAverage F1Measure = " + avgF1mMeasure + "\n");
        commaSeparatedFPRs += avgPrecision + ",";
        commaSeparatedFPRs += avgRecall + ",";
        commaSeparatedFPRs += avgFpoint5Measure + ",";
        commaSeparatedFPRs += avgF1mMeasure + ",";
        commaSeparatedFPRs += avgF2Measure + "\n";

        avgSize = 0.0;
        currStatistics.clear();
        dictionary.clear();
    }

    String commaSeparatedFPRs;

    @Test
    public void testRandom() throws IOException {
        double threshold[] = new double[]{0.0};
        for (double v : threshold) {
            testResultsWithDynamicTrainingOnFolder7(v);
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("results/temporaryCommaSeparatedValues.csv"));
        bufferedWriter.write(commaSeparatedFPRs);
        bufferedWriter.close();
    }

    public void testResultsWithDynamicTrainingOnFolder8(double v) throws IOException {
        boolean discardSmallFreqPlusLength = true;
        String[] tWithJustAnomalies = new String[10];
        String[] encodedTWithSB = new String[10];
        String[] encodedTWithoutSB = new String[10];
        boolean enhancement = true, skip = false;
        LZW lzw = new LZW(0.7);
        traceFileIO = new TraceFileReadAndWrite(1,0);
        ArrayList<Statistics> currStatistics = new ArrayList<Statistics>();
        double avgSize = 0.0, avgFPR, avgTPR, avgPrecision, avgRecall, avgF1Measure, avgFpoint5Measure, avgF2Measure;
        ArrayList<HashMap<String, Double>> ensemble = new ArrayList<HashMap<String, Double>>(3);

        //get all training data with and without Session Breaks
        for(int filenum = 0; filenum <= 9; filenum++){
            traceFile = "8(WithConceptDrift40Anomaly)/T"+filenum;
            traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/" + traceFile);
            encodedTWithSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(true, -1);
            encodedTWithoutSB[filenum] = traceFileIO.retrieveTrainingCommandSequence(false, -1);
        }

        tWithJustAnomalies[0] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[0], new int[]{});
        tWithJustAnomalies[1] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[1], new int[]{});
        tWithJustAnomalies[2] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[2], new int[]{});
        tWithJustAnomalies[3] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[3], new int[]{});
        tWithJustAnomalies[4] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[4], new int[]{encodedTWithoutSB[4].length()-40, encodedTWithoutSB[4].length()-1});
        tWithJustAnomalies[5] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[5], new int[]{});
        tWithJustAnomalies[6] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[6], new int[]{});
        tWithJustAnomalies[7] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[7], new int[]{encodedTWithoutSB[7].length()-40, encodedTWithoutSB[7].length()-1});
        tWithJustAnomalies[8] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[8], new int[]{});
        tWithJustAnomalies[9] = Utility.
                returnActualCommandSequenceWithJustAnomalies(encodedTWithoutSB[9], new int[]{encodedTWithoutSB[9].length()-40, encodedTWithoutSB[9].length()-1});

        boolean trainOnSelf = false;
        double threshold[] = new double[]{0.01, 0.03, 0.05, 0.07, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};

            for(int index = 0; index < (trainOnSelf?10:9); index++){//train on index, test on index + 1
//                if(index == 0)
                    dictionary = lzw.createDictionary(encodedTWithSB[index], new HashMap<String, Double>());
//                if((index > 0) && (!skip))
//                    dictionary = lzw.createDictionary(encodedTWithSB[index], quantisedDictionary);
                if(!skip) quantisedDictionary = new LossyCompression(dictionary).createQuantisedDictionaryFromDictionary();
                skip = false;
                if(discardSmallFreqPlusLength)
                    Utility.discardEntriesWithFreqPlusLengthLessThan(quantisedDictionary, v);
//                System.out.println("\nDICTIONARY SIZE: " + quantisedDictionary.size() + "\n");
                avgSize += quantisedDictionary.size();
                if(enhancement){
                    switch (index){
                        case 4:
                        case 7:
                        case 9:{
                            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + (trainOnSelf?0:1)].substring(0, encodedTWithoutSB[index + (trainOnSelf?0:1)].length()-40));
                        }
                        break;
                        default:
                            dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + (trainOnSelf?0:1)]);
                    }
                }
                else
                    dataStreamPatternRecognizer = new DataStreamPatternRecognizer(encodedTWithoutSB[index + (trainOnSelf?0:1)]);

                ensemble.add(quantisedDictionary);

                Statistics bestStatistic = new Statistics();
                Statistics worstStatistic = new Statistics();

                for (HashMap<String, Double> qD : ensemble) {
                    anomalousData = dataStreamPatternRecognizer.
                            removePatternsGotFromQuantizedDictionary(qD.keySet(), false, traceFileIO);
                    Statistics statistic = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);

                    if((bestStatistic.FPR == 0.0) || (statistic.FPR < bestStatistic.FPR))
                        bestStatistic = statistic;
                    if(statistic.FPR > worstStatistic.FPR)
                        worstStatistic = statistic;
                }
                if(ensemble.size() == 3){
                    for(int i = 0; i <= 2; i++){
                        anomalousData = dataStreamPatternRecognizer.
                                removePatternsGotFromQuantizedDictionary(ensemble.get(i).keySet(), false, traceFileIO);
                        Statistics statistic = Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + 1]);
                        if(worstStatistic == statistic) {
                            ensemble.remove(i);
                            break;
                        }
                    }
                }
                currStatistics.add(bestStatistic);
//                currStatistics.add(Utility.printAndReturnStatisticsFrom(anomalousData, tWithJustAnomalies[index + (trainOnSelf ? 0 : 1)]));
            }

            avgFPR = avgTPR = avgF1Measure = avgPrecision = avgRecall = avgFpoint5Measure = avgF2Measure = 0.0;
            for (Statistics statistic : currStatistics) {
                if(statistic.TP == 0.0) continue;
                avgFpoint5Measure += statistic.fpoint5measure;
                avgF1Measure += statistic.f1measure;
                avgF2Measure += statistic.f2measure;
                avgRecall += statistic.recall;
                avgPrecision += statistic.precision;
                avgFPR += statistic.FPR;
                avgTPR += statistic.TPR;
            }
            avgFPR /= currStatistics.size();
            avgTPR /= 3;
            avgSize /= 3;
            avgF1Measure /= 3;
            avgF2Measure /= 3;
            avgFpoint5Measure /= 3;
            avgPrecision /= 3;
            avgRecall /= 3;
            System.out.println("Average Precision = " + avgPrecision + "\nAverage Recall = " + avgRecall + "\nAverage F1Measure = " + avgF1Measure + "\n");
            commaSeparatedFPRs += avgPrecision + ",";
            commaSeparatedFPRs += avgRecall + ",";
            commaSeparatedFPRs += avgFpoint5Measure + ",";
            commaSeparatedFPRs += avgF1Measure + ",";
            commaSeparatedFPRs += avgF2Measure + "\n";            avgSize = 0.0;
            currStatistics.clear();
            dictionary.clear();
    }

    @Test
    public void test() throws IOException {
        traceFileIO = new TraceFileReadAndWrite(1,0);
        List<String> string1 = traceFileIO.retrieveCommandSequenceAndCreateMapFrom("in/8(WithConceptDrift25Anomaly)/non-4O");

        TraceFileReadAndWrite traceFileIO2 = new TraceFileReadAndWrite(1, 0);
        List<String> strings2 = traceFileIO2.retrieveCommandSequenceAndCreateMapFrom("in/8(WithConceptDrift25Anomaly)/novice-28O");

        TraceFileReadAndWrite traceFileIO3 = new TraceFileReadAndWrite(1, 0);
        List<String> strings3 = traceFileIO3.retrieveCommandSequenceAndCreateMapFrom("in/8(WithConceptDrift25Anomaly)/scientist-36");

        string1.addAll(strings2);
        HashSet<String> anomalies = new HashSet<String>();
        anomalies.addAll(strings3);
        anomalies.removeAll(string1);
        int index = 1;
        for (String anomaly : anomalies) {
            System.out.print("C " + anomaly + "\n" + "D /user/grads\n" + "A NIL\n" + "H NIL\n" + "X NIL\n\n");
            index ++;
            if(index %40 == 0)
                System.out.println("\n---------------");
        }

        //String s = traceFileIO.retrieveTrainingCommandSequence(true, -1);
        //utilityToFindIndicesOfAnomalousCodeInjected(s);
    }

    private void utilityToFindIndicesOfAnomalousCodeInjected(String s) {//without the neglected entries !
        ArrayList<String> strings = traceFileIO.decodeCommandsFromString(s);
        for (int i = 0, stringsSize = strings.size(); i < stringsSize; i++) {
            String string = strings.get(i);
            System.out.print("("+i+")"+string + " ");
        }
    }
}
//dynamic should be more tight wrt matching
//static should use most recent data for training
