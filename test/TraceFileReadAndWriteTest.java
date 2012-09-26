import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

public class TraceFileReadAndWriteTest {
    @Test
    public void testDiscardingSessions() {
        String dataFromScientistOne = "-丗乜丫-世乏世丠丛丟-乍乜丫乒世-丞丞乏世丠丐丛万丛乏丠为世乜-乜乏世丠东丠东东丟丞" +
                "-乜为为为万-乜丠乜丢世乓丠乓丢両丐-乜乜乢丁丝严丁両乢丠乜世丠乓両乒乓両世世上丠両丞乜万-乜么么为为么両世万世" +
                "丫乜乜万両-乜丠乏両乒乒乙丟丳世丫丠両世-乜丝世世-乜丫乜丞丞乜丁丁丁両丞-丞乜乜乢乏世丠丛乜-乜-乨丬丮乨-乜乏両" +
                "丠世乏乚丒书万书丢-书万-书乔乔丫乔乔乤乔乏为一万-丞-乜丞丞乜乜乢万丫丁世丫乓丢-丞乜-丕万両乢乜乢-乊乜両-乜乢" +
                "-乜乢丞乜-乜为-乜丐乢丞乏丠丛丟-为为乜世丫乜为世两乏么世乎丆丠丝万丆丼乓乎丆丼万丆万万丆世乄乄丆万-乏世丆么为" +
                "丠丆万丆丆丫-乢乏世上丑丠丆丆丞为世丆久丆一乡举久举丑久-乏为世上世丠丆丆丫久举丆久举丆丆丆丼万丆丆久上丢世丫丫" +
                "丫举丼举丆丫举丆丆久举丆丆久举严丆乀举丆乀举丆-乏丠丆为为举丆乀丆丆乀不乏世乒乄不丆乀为为为为为乀-乨丬丮-之丰丝" +
                "丝両世上乓丫丢世乓丢丢乒乓世乓世专世为为为为一-乜为为乊乊丫一丮一乜万-乜为-乨丬丮-为么丫世丫丛丛丟丠乀一丮世乓" +
                "为-丞丞乛丞-乜为-乜丫为両九丄丄丄万-乜乜乏丠丛-乃乃-乜世丞丞乜为-乜-乏世三三世三三世三严三乏世丿乏丠-丞-丞乛乛" +
                "両丞-乢-丞乒-乜丞为-乜-乜丞丞乏丠为-乢乢丞乜丠丠世乓両乒乓专世乂乖-乜-乛乨世丫为世丞乜万万为-乜丞-丗丞乜乔丝乔" +
                "九乔乔世乌-乜-丞乜中中丫乒乔乔乔乔乔乔中丞丞为为丄--乜世世乒乓丢世世乒乓世丫丢世丢世乓両世乏乒-丠丠乔-乔世丞丧-" +
                "为乢乢万乜丞-乑为为串为为为为为为一世为一両串为-乜丞乜-丞乊乊丞丟-乜乢乔乔丞丠丝丶丝乔乔丞乔丞乔乔丞丶丫丠-乜世乄" +
                "世丫丞乛丞乜-乜乢丞乜乏世世乏丫両世丠-乢中严-乜丞乏世丠-乜乏丠九世乒乣両乔丫不世世乒且不丫为万万-乜丞丞中-为-为为" +
                "为为为为为严-乏丠乜乜万丞万乜丞乛万万両丠丂丟丄万丠-乜乏丠丠丞万丟丛丟丂-乜-乜乢-乏丠九世-世乃乃乃丯乃乃乃乃主-丞" +
                "乜乛丞乏世乓丫不丢乒乒乓不乓世不世丞乜不丠乛万-乃乄-乜乏丑丸-乏丛丟乣不且乏且乣乣乏世且丝不丛丟-世且不世不不乏世丹" +
                "乏乏丹世乏丹乥世世乂世乂世乏乓世乏乏世且且丫丢世世乒乒世乓世乏乁世乓世乁世丢乏世乏世乏世乏乏世乏乏世专乏世世世世乓世" +
                "乏丫乓世乏乁世乏世乓乓乓世世乒乓乏乓乒世乒乓乏世世乏乒乓乒乓世丷丢丷七乓世上乒世乒世乓乒世丢世世丢世乏世世乏世世丢世" +
                "丢世乖世乂乖专-丰乊-乢乢万乒丕万両-丞丞-乜丞乜-乜乢丞-乖乜丞乊乊両丠-丅世丫-世世七両乘両丫両七乏七両丫世丫世丫丫乓丫" +
                "七七世严九乏世七乏丢丢両乒世丫世-乜丞-乜丞-为-为-丰为-丞-乜乏世乏世丠世上世乊丄丄一丠丟丛丛丟丝丛丟九世乒両丛乣世且" +
                "不世乏世乏世丫丹不世上乜丛乜丛丛丟临世丛丟-乜世乓丫丐乢両丠-乜丞丞中乛-乜乏世丠乜丷丷丫丞乜丫万乜丝万万両-乜为丞乛万" +
                "-乖世丨丨且且且丮且且乓丝丝丝丝世且上世乢为乜万-乏丠乜両万万万且万为万丞乛万乜両万両-串为両丐丁丫乒乔乔乔万与万与九丬" +
                "世乊丞万万万丞中乜丝乜乜丝乕丝万乜万乜万为万両乓上世丢世乓世世乓世乒丢世乓世世不丫世丫両专不专世乏世且両丌丌丌丟丝丛丛" +
                "世丫世丫乓乏丞乜丟-为-乕並乜両丘乔乔両丵-乜丞乜-世乔世且万-乕乜乏両丠乨万乃乃万万乃万世万両乃丛丟世上世九乏丝乐万丟-乃" +
                "-乃乃义东乞乃乃丟丛丟临--乜丠乤乤-乜丞乛乔乛丞丞乛乛丞为为为为乛万乛万丠万-丞丞乜丞乜丞丞乜丞万为乜丠乜万-不乏世乏世个" +
                "乄乏丠乓世-且丒且-乆丶丁丘-乕乜为乔丠乔丠乔丠丵-乔丣乧乔丫-";

        String expectedOutput = "-世乏世丠丛丟-丞丞乏世丠丐丛万丛乏丠为世乜-乜乏世丠东丠东东丟丞-乜丠乜丢世乓丠乓丢両丐-" +
                "乜乜乢丁丝严丁両乢丠乜世丠乓両乒乓両世世上丠両丞乜万-乜么么为为么両世万世丫乜乜万両-乜丠乏両乒乒乙丟丳世丫" +
                "丠両世-乜丫乜丞丞乜丁丁丁両丞-丞乜乜乢乏世丠丛乜-乜乏両丠世乏乚丒书万书丢-书乔乔丫乔乔乤乔乏为一万-乜丞丞" +
                "乜乜乢万丫丁世丫乓丢-丕万両乢乜乢-乜丐乢丞乏丠丛丟-为为乜世丫乜为世两乏么世乎丆丠丝万丆丼乓乎丆丼万丆万万丆" +
                "世乄乄丆万-乏世丆么为丠丆万丆丆丫-乢乏世上丑丠丆丆丞为世丆久丆一乡举久举丑久-乏为世上世丠丆丆丫久举丆久举丆" +
                "丆丆丼万丆丆久上丢世丫丫丫举丼举丆丫举丆丆久举丆丆久举严丆乀举丆乀举丆-乏丠丆为为举丆乀丆丆乀不乏世乒乄不丆乀" +
                "为为为为为乀-之丰丝丝両世上乓丫丢世乓丢丢乒乓世乓世专世为为为为一-乜为为乊乊丫一丮一乜万-为么丫世丫丛丛丟丠" +
                "乀一丮世乓为-乜丫为両九丄丄丄万-乜世丞丞乜为-乏世三三世三三世三严三乏世丿乏丠-乜丞丞乏丠为-乢乢丞乜丠丠世乓両" +
                "乒乓专世乂乖-乛乨世丫为世丞乜万万为-丗丞乜乔丝乔九乔乔世乌-丞乜中中丫乒乔乔乔乔乔乔中丞丞为为丄-乜世世乒乓丢世" +
                "世乒乓世丫丢世丢世乓両世乏乒-为乢乢万乜丞-乑为为串为为为为为为一世为一両串为-乜乢乔乔丞丠丝丶丝乔乔丞乔丞乔乔丞" +
                "丶丫丠-乜世乄世丫丞乛丞乜-乜乢丞乜乏世世乏丫両世丠-乜乏丠九世乒乣両乔丫不世世乒且不丫为万万-为为为为为为为严-乏" +
                "丠乜乜万丞万乜丞乛万万両丠丂丟丄万丠-乜乏丠丠丞万丟丛丟丂-世乃乃乃丯乃乃乃乃主-丞乜乛丞乏世乓丫不丢乒乒乓不乓世" +
                "不世丞乜不丠乛万-乏丛丟乣不且乏且乣乣乏世且丝不丛丟-世且不世不不乏世丹乏乏丹世乏丹乥世世乂世乂世乏乓世乏乏世且且" +
                "丫丢世世乒乒世乓世乏乁世乓世乁世丢乏世乏世乏世乏乏世乏乏世专乏世世世世乓世乏丫乓世乏乁世乏世乓乓乓世世乒乓乏乓乒" +
                "世乒乓乏世世乏乒乓乒乓世丷丢丷七乓世上乒世乒世乓乒世丢世世丢世乏世世乏世世丢世丢世乖世乂乖专-乢乢万乒丕万両-乖乜" +
                "丞乊乊両丠-世世七両乘両丫両七乏七両丫世丫世丫丫乓丫七七世严九乏世七乏丢丢両乒世丫世-乜乏世乏世丠世上世乊丄丄一丠" +
                "丟丛丛丟丝丛丟九世乒両丛乣世且不世乏世乏世丫丹不世上乜丛乜丛丛丟临世丛丟-乜世乓丫丐乢両丠-乜乏世丠乜丷丷丫丞乜丫" +
                "万乜丝万万両-乖世丨丨且且且丮且且乓丝丝丝丝世且上世乢为乜万-乏丠乜両万万万且万为万丞乛万乜両万両-串为両丐丁丫乒" +
                "乔乔乔万与万与九丬世乊丞万万万丞中乜丝乜乜丝乕丝万乜万乜万为万両乓上世丢世乓世世乓世乒丢世乓世世不丫世丫両专不专" +
                "世乏世且両丌丌丌丟丝丛丛世丫世丫乓乏丞乜丟-乕並乜両丘乔乔両丵-乕乜乏両丠乨万乃乃万万乃万世万両乃丛丟世上世九乏丝" +
                "乐万丟-乃乃义东乞乃乃丟丛丟临-乜丞乛乔乛丞丞乛乛丞为为为为乛万乛万丠万-丞丞乜丞乜丞丞乜丞万为乜丠乜万-不乏世乏世" +
                "个乄乏丠乓世-乕乜为乔丠乔丠乔丠丵";
        TraceFileReadAndWrite traceFileIO = new TraceFileReadAndWrite(1, 0);
        String actualOutput = traceFileIO.discardSessionsOfSizeLessThan(5, dataFromScientistOne);
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}
