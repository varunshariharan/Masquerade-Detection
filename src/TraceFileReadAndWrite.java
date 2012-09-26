import java.io.*;
import java.util.*;

public class TraceFileReadAndWrite {
    private List<TraceFileEntry> traceFileEntries;
    private List<String> commandSequence;
    private HashMap<Character, String> characterToCommandHashMap;
    private HashMap<String, Character> commandToCharacterHashMap;
    private float p, q;
    private String encodedCommandSequence;
    public static char sessionBreak = '-';
    private int splitIndex;
    private int counter = '\u4E00';
    private final HashSet<String> commandSet;

    //creates training data for approximately p/(p+q) of trace file, testing data for the rest
    public TraceFileReadAndWrite(int p, int q) {
        this.p = p;
        this.q = q;
        characterToCommandHashMap = new HashMap<Character, String>();
        commandToCharacterHashMap = new HashMap<String, Character>();
        commandSet = new HashSet<String>();
    }

    public List<String> retrieveCommandSequenceAndCreateMapFrom(String inputFile) throws IOException {
        readFrom(inputFile);
        commandSequence = new ArrayList<String>();
        for (TraceFileEntry traceFileEntry : traceFileEntries) {
            if(traceFileEntry.sessionBreak){
                commandSequence.add(String.valueOf(sessionBreak));
                continue;
            }
            String commandWithoutParameters = traceFileEntry.C.split(" ")[0];
            commandSequence.add(commandWithoutParameters);
        }
        mapCommandsToAString();
        return commandSequence;
    }

    public ArrayList<String> decodeCommandsFromString(String encodedData) {
        ArrayList<String> individualDecodedCommandSequence = new ArrayList<String>();
        for(int index = 0; index < encodedData.length(); index++){
            String command = characterToCommandHashMap.get(encodedData.charAt(index));
            individualDecodedCommandSequence.add(command);
        }
        return individualDecodedCommandSequence;
    }


    private List<TraceFileEntry> readFrom(String inputFile) throws IOException {
        traceFileEntries = new ArrayList<TraceFileEntry>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
        String line;

        line = bufferedReader.readLine();
        while (line != null) {

            if((line.length() > 0) && (line.charAt(0) == 'S')){
                TraceFileEntry traceFIleEntry = new TraceFileEntry();
                traceFIleEntry.sessionBreak = true;
                traceFileEntries.add(traceFIleEntry);
            }
            if((line.length() > 0) && (line.charAt(0) == 'C')){
                TraceFileEntry traceFileEntry = new TraceFileEntry();
                try{
                    traceFileEntry.C = line.substring(2);
                    line = bufferedReader.readLine();
                    traceFileEntry.D = line.substring(2);
                    line = bufferedReader.readLine();
                    traceFileEntry.A = line.substring(2);
                    line = bufferedReader.readLine();
                    traceFileEntry.H = line.substring(2);
                    line = bufferedReader.readLine();
                    traceFileEntry.X = line.substring(2);
                }
                catch (Exception e){
//                    System.err.println("bad data");
                }
                if("NIL".equals(traceFileEntry.X))
                    traceFileEntries.add(traceFileEntry);
            }
            line = bufferedReader.readLine();
        }

        bufferedReader.close();
        return traceFileEntries;
    }

    private void mapCommandsToAString() {
        encodedCommandSequence = "";

        //adding session break and matched char to character to map
        if(!characterToCommandHashMap.containsKey(DataStreamPatternRecognizer.matchedChar)){
            characterToCommandHashMap.put(DataStreamPatternRecognizer.matchedChar,
                    String.valueOf(DataStreamPatternRecognizer.matchedChar));
            commandToCharacterHashMap.put(String.valueOf(DataStreamPatternRecognizer.matchedChar),
                    DataStreamPatternRecognizer.matchedChar);
        }
        if(!characterToCommandHashMap.containsKey(sessionBreak)){
            characterToCommandHashMap.put(sessionBreak, String.valueOf(sessionBreak));
            commandToCharacterHashMap.put(String.valueOf(sessionBreak), sessionBreak);
        }

        //get unique commands
        for (String command : commandSequence)
            commandSet.add(command);
        //assign unique character for unique commands
        for (String command : commandSet){
            if(command.equals( String.valueOf(sessionBreak) ))
                continue;
            if(commandToCharacterHashMap.containsKey(command))
                continue;
            characterToCommandHashMap.put((char) counter, command);
            commandToCharacterHashMap.put(command, (char) counter);
            counter++;
        }

        //encode sequence of commands to a string
        for (String command : commandSequence)
            encodedCommandSequence += commandToCharacterHashMap.get(command);

        //find index to split training and testing data
        splitIndex = encodedCommandSequence.length();
//        int trainingLength, leftSearchIndex, rightSearchIndex;
//        trainingLength = (int) ((p/(p+q)) * encodedCommandSequence.length());
//        leftSearchIndex = rightSearchIndex = trainingLength;
//        while ((encodedCommandSequence.charAt(leftSearchIndex) != '-') &&
//                (encodedCommandSequence.charAt(rightSearchIndex) != '-')){
//            leftSearchIndex--;
//            rightSearchIndex++;
//        }
//        splitIndex = encodedCommandSequence.charAt(leftSearchIndex) == '-' ? leftSearchIndex : rightSearchIndex;
    }

    //-ve => no discard
    public String retrieveTrainingCommandSequence(boolean withSessionBreaks, int discardSessionsSmallerThan) {
        String trainingCommandSequence = encodedCommandSequence.substring(0, splitIndex);
        if(discardSessionsSmallerThan >= 0)
            trainingCommandSequence = discardSessionsOfSizeLessThan(discardSessionsSmallerThan, trainingCommandSequence);
        return withSessionBreaks ?
                trainingCommandSequence :
                trainingCommandSequence.replaceAll("-", "");
    }

    public String retrieveTestingCommandSequence() {
        return encodedCommandSequence
                .substring(splitIndex, encodedCommandSequence.length())
                .replaceAll("-", "");
    }

    public String discardSessionsOfSizeLessThan(int sessionSize, String commandSequence) {
        StringBuilder temporaryCommandSequence = new StringBuilder(commandSequence);
        int commandCount = 0, nextSessionBreakIndex = commandSequence.length();
        for(int index = commandSequence.length() - 1; index >= 0; index--){
            if(temporaryCommandSequence.charAt(index) == sessionBreak){
                if(commandCount <= sessionSize)
                    temporaryCommandSequence.replace(index, nextSessionBreakIndex, "");
                nextSessionBreakIndex = index;
                commandCount = 0;
                continue;
            }
            commandCount++;
        }
        return temporaryCommandSequence.toString();
    }

    public void writeDictionaryToFile(HashMap<String, Integer> quantisedDictionary, String traceFileName) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("quantizedDictionary/" + traceFileName));
        for (Map.Entry<String, Integer> stringIntegerEntry : quantisedDictionary.entrySet())
            bufferedWriter.write(
                    stringIntegerEntry.getValue().toString() + " " +
                            decodeCommandsFromString(stringIntegerEntry.getKey()) + "\n");
        bufferedWriter.close();
    }

    public List<String> retrieveCommandSequenceAndCreateMapFrom(ArrayList<TraceFileCommand> traceFileCommands) {
        traceFileEntries = new ArrayList<TraceFileEntry>();
        for (TraceFileCommand traceFileCommand : traceFileCommands) {
            TraceFileEntry traceFileEntry = new TraceFileEntry();
            traceFileEntry.C = traceFileCommand.getC();
            traceFileEntry.D = traceFileCommand.getD();
            traceFileEntry.A = traceFileCommand.getA();
            traceFileEntry.H = traceFileCommand.getH();
            traceFileEntry.X = traceFileCommand.getX();
            traceFileEntries.add(traceFileEntry);
        }

        commandSequence = new ArrayList<String>();
        for (TraceFileEntry traceFileEntry : traceFileEntries) {
            if(traceFileEntry.sessionBreak){
                commandSequence.add(String.valueOf(sessionBreak));
                continue;
            }
            String commandWithoutParameters = traceFileEntry.C.split(" ")[0];
            commandSequence.add(commandWithoutParameters);
        }
        mapCommandsToAString();
        return commandSequence;
    }
}
