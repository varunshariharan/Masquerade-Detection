import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class LossyCompressionTest {
    @Test
    public void testLossyCompressionGivesQuantisedDictionary() {
        LZW lzw = new LZW();
        LossyCompression lossyCompression = new LossyCompression(lzw.createDictionary("mousemousemousemouselionlionlion", new HashMap<String, Double>()));
        HashMap<String, Integer> quantisedDictionary = new HashMap<String, Integer>(){{
            put("lion",1);put("mo",3);put("lio",2);put("use",1);put("se",3);put("mou",2);
            put("mous",1);put("li",3);put("on",2);put("sem",1);put("onl",1);
        }};
        Assert.assertEquals(quantisedDictionary, lossyCompression.createQuantisedDictionaryFromDictionary());
    }
}
