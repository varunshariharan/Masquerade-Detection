import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Varun S Hariharan
 * Date: 06/01/12
 * Time: 1:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class TraceFileCommand implements Serializable{
    private String C,D,A,H,X;
    private boolean label;

    public String getC(){
        return C;
    }

    public String getD(){
        return D;
    }

    public String getA(){
        return A;
    }

    public String getH(){
        return H;
    }

    public String getX(){
        return X;
    }

    public boolean getLabel(){
        return label;
    }

    public String getEntireEntry(){
        String returnString="C "+C+"\n"+"D "+D+"\n"+"A "+A+"\n"+"H "+H+"\n"+"X "+X+"\n\n";
        return returnString;
    }

    public void setC(String C){
        this.C=C;
    }

    public void setD(String D){
        this.D=D;
    }

    public void setA(String A){
        this.A=A;
    }

    public void setH(String H){
        this.H=H;
    }

    public void setX(String X){
        this.X=X;
    }

    public void setLabel(boolean label){
        this.label = label;
    }

    public void resetValues(){
        C="";
        D="";
        H="";
        A="";
        X="";
    }

    @Override
    public String toString(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(C);
        stringBuffer.append("\n");
        stringBuffer.append(D);
        stringBuffer.append("\n");
        stringBuffer.append(A);
        stringBuffer.append("\n");
        stringBuffer.append(H);
        stringBuffer.append("\n");
        stringBuffer.append(X);
        stringBuffer.append("\n");
        stringBuffer.append(label);
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }
}
