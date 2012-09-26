import java.io.Serializable;
import java.util.*;

public class CommandList implements Serializable{
    ArrayList<TraceFileCommand> commandList = new ArrayList<TraceFileCommand>();

    public void setCommandList(ArrayList<TraceFileCommand> commands){
        for (TraceFileCommand command : commands) {
            commandList.add(command);
        }
    }

    public ArrayList<TraceFileCommand> getCommandList(){
        return commandList;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        for (TraceFileCommand traceFileCommand : commandList) {
            buffer.append(traceFileCommand.getEntireEntry());
        }
        return buffer.toString();
    }
}
