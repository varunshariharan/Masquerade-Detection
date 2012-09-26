import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator{

    public ValueComparator(){
    }

    @Override
    public int compare(Object a, Object b) {
        Map.Entry<String, Double> a1 = (Map.Entry<String, Double>) a;
        Map.Entry<String, Double> b1 = (Map.Entry<String, Double>) b;

        //frequency
//        if(a1.getValue() < b1.getValue())
//            return -1;
//        if(a1.getValue().equals(b1.getValue()))
//            return 0;

        //length
//        if(a1.getKey().length() < b1.getKey().length())
//            return -1;
//        if(a1.getKey().length() == b1.getKey().length())
//            return 0;

        //length*freq
        if(a1.getKey().length()*a1.getValue() < b1.getKey().length()*b1.getValue())
            return -1;
        if(a1.getKey().length()*a1.getValue() == b1.getKey().length()*b1.getValue())
            return 0;

        return 1;
    }
}
