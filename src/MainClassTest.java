import org.junit.Assert;
import org.junit.Test;

public class MainClassTest {
    @Test
   public void testGetLocalNumber(){
        MainClass main = new MainClass();
        Assert.assertEquals("Number != 14", 14, main.getLocalNumber());
    }
}
