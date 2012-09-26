import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class LZWTest {
    @Test
    public void testLZWReturnsDictionary() {
        LZW lzw = new LZW();

        HashMap<String, Integer> dictionary = new HashMap<String, Integer>(){{
            put("mo",3);put("ou",2);put("emo",1);put("mou",2);put("use",1);put("us",2);put("se",3);
            put("em",2);put("m",1);put("sem",1);put("ous",1);put("mous",1);
        }};
        Assert.assertEquals(dictionary, lzw.createDictionary("mousemousemousemouse", new HashMap<String, Double>()));
    }

    @Test
    public void testLZWReturnsDictionaryWithWResetSessionBreak() {
        LZW lzw = new LZW();
        HashMap<String, Integer> dictionary = new HashMap<String, Integer>(){{
            put("mo",2);put("emo",1);put("ou",2);put("lio",1);put("use",1);put("l",2);put("us",2);
            put("m",1);put("se",2);put("io",1);put("li",2);put("mou",1);put("on",2);put("em",2);put("ous",1);
        }};
        Assert.assertEquals(dictionary, lzw.createDictionary("mousemousemouse-lion-lion", new HashMap<String, Double>()));
    }
}
