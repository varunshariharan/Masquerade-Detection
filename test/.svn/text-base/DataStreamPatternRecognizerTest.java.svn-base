import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DataStreamPatternRecognizerTest {
    @Test
    public void testRecognizePattern() {
        DataStreamPatternRecognizer patternRecognizer = new DataStreamPatternRecognizer("mousexyzmose");
        int[] pattern = patternRecognizer.recognizePattern("mose");
        Assert.assertEquals(Arrays.toString(new int[]{4, 3, 2, 2, 2, 1, 2, 3, 4, 3, 2, 1, 0}), Arrays.toString(pattern));
        String match = patternRecognizer.performBackwardTraversalFromIndex(11);
        Assert.assertEquals("mose", match);
    }

    @Test
    public void testRemoveMatch() {
        DataStreamPatternRecognizer dataStreamPatternRecognizer = new DataStreamPatternRecognizer("mousexyxmouse");
        dataStreamPatternRecognizer.removeMatch(2, 2);
        Assert.assertEquals("m..sexyxmouse", dataStreamPatternRecognizer.getAnomalousData());
        dataStreamPatternRecognizer.removeMatch(8, 5);
        Assert.assertEquals("m..s.....ouse", dataStreamPatternRecognizer.getAnomalousData());
    }

    @Test
    public void testFromActualData() {
        DataStreamPatternRecognizer patternRecognizer = new DataStreamPatternRecognizer("丰乊乢乢万乒丕万両丞丞乜丞乜乜乢丞乖乜丞乊乊両丠丅世丫世世七両乘両丫両七乏七両丫世丫世丫丫乓丫七七世严九乏世七乏丢丢両乒世丫世乜丞乜丞为为丰为丞乜乏世乏世丠世上世乊丄丄一丠丟丛丛丟丝丛丟九世乒両丛乣世且不世乏世乏世丫丹不世上乜丛乜丛丛丟临世丛丟乜世乓丫丐乢両丠乜丞丞中乛乜乏世丠乜丷丷丫丞乜丫万乜丝万万両乜为丞乛万乖世丨丨且且且丮且且乓丝丝丝丝世且上世乢为乜万乏丠乜両万万万且万为万丞乛万乜両万両串为両丐丁丫乒乔乔乔万与万与九丬世乊丞万万万丞中乜丝乜乜丝乕丝万乜万乜万为万両乓上世丢世乓世世乓世乒丢世乓世世不丫世丫両专不专世乏世且両丌丌丌丟丝丛丛世丫世丫乓乏丞乜丟为乕並乜両丘乔乔両丵乜丞乜世乔世且万乕乜乏両丠乨万乃乃万万乃万世万両乃丛丟世上世九乏丝乐万丟乃乃乃义东乞乃乃丟丛丟临乜丠乤乤乜丞乛乔乛丞丞乛乛丞为为为为乛万乛万丠万丞丞乜丞乜丞丞乜丞万为乜丠乜万不乏世乏世个乄乏丠乓世且丒且乆丶丁丘乕乜为乔丠乔丠乔丠丵乔丣乧乔丫乔乗丝乔丠世九九乧乧乔乔乧丩丣乧万丵乔丣乔乔丣丣乔乒丒丒丏乔乤乔丣丣万丠丠为不世乏世乏世乏世乏一为一世丫丞为乨为丞乜丞乜乨丮乨乜丱乜丞乛万両乢丞串丞中乏世乏业个世世世乜乢世乓乓乓丢丠丐乢上丢世乏世世乏中中中中乨中为万乜丠世乓世世乚丫乒乓世乒両乖乏世世世且乏为串一为一両串为世丞丞乜丞乜乏世乏世丌乏世乏世乏世乏世丌丌丠丞丬世丢丢世丫世世世专乖世乏世世世乒丢世世世世乓乓世乓丢世世乒乒乓世乓世世世乏世且不乏世乥世乏両乒世世世世两乏丠丛丟乊中両丞乜不世乛乜中乖为为万丠両乏世乏世乏世世乏両世丙丶両乏世世不乏世世乏世丫乏世世世世乏丞丞乜串乏世丫丫丷丠九乘严丫丽丄丄严丽丄临丄丄串为乜乜丞乜丞串丞世为为串丞乜丠为丈为一万一丮一为一丮为乏世两並丝乨丞丮乨乏世乏世么世丠万乇万下丼万为为为一举一也举下万下万下万乜万下万下万下下万下万下万下万下下一也万举下万下万下下下万下下也一举一习为丝万世下为万丼乜両乢乏与串为中为为为为乜乢乜丞丞乜为乢万为一丮万不世乒乄不丠为世乒世丠乜世串为乏世乏世丠乏为");
        int[] pattern = patternRecognizer.recognizePattern("乏世世乏");
        for (int index = 0, patternLength = pattern.length; index < patternLength; index++) {
            if (pattern[index] == 0)
                Assert.assertEquals("乏世世乏", patternRecognizer.performBackwardTraversalFromIndex(index - 1));
        }
    }

    @Test
    public void test() {
        DataStreamPatternRecognizer patternRecognizer = new DataStreamPatternRecognizer("moumoumouxpspsmou");
        patternRecognizer.recognizePattern("mou");
        System.out.println();
    }
}
