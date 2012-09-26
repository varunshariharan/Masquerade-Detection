import org.junit.Assert;
import org.junit.Test;

public class EditDistanceTest {
    @Test
    public void testEditDistanceReturnsDistance() {
        EditDistance editDistance = new EditDistance();

        int distance = editDistance.calculateDistance("kitten", "sitting");
        Assert.assertEquals(3, distance);
    }
}
