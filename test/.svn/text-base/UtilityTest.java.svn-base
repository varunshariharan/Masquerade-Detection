import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;

public class UtilityTest {
    @Test
    public void testFindUniqueCommandsInFileBButNotInFileA() throws Exception {
        HashSet<String> uniqueCommandsInFileBButNotInFileA = Utility.findUniqueCommandsInFileBButNotInFileA("novice-1", "scientist-1");
        HashSet<String> expectedUniqueCommands = new HashSet<String>(){{
            add("diff");add("a.out");add("grep");add("f");add("c");add("p");add("echo");add("ruptime");add("make");
            add("hello");add("which");add("jobs");add("reset");add("memo");add("dirs");add("%rl");add("setvterm");
            add("checknews");add("eqn");add("postnews");add("head");add("enscript");add("history");add("fgrep");
            add("%e");add("hostname");add("%r");add("set");add("%4");add("%3");add("%2");add("nroff");add("set_prompt");
            add("from");add("warp.path");add("ftp");add("tbl");add("stty");add("alloc");add("find");add("tail");
            add("temp");add("dbx");add("/bin/rm");add("bind");add("clear");add("cvgetscr");add("whom");add("csh");
            add("readnews");add("tidy");add("pwd");add("rlogin");add("fspell");add("troff");add("popd");add("du");
            add("df");add("yes");add("bc");add("cc");add("as");add("pd");add("ll");add("last");add("spell");
            add("nuke");add("if");add("kermit");add("hog");add("wc");add("setenv");add("tty");add("su");add("login");
            add("ps");add("apq");add("printenv");add("rwho");
        }};
        Assert.assertEquals(expectedUniqueCommands, uniqueCommandsInFileBButNotInFileA);
    }
}
