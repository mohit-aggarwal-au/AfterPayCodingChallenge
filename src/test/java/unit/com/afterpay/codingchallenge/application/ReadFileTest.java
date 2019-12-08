package unit.com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.application.ReadFile;
import org.junit.Test;

public class ReadFileTest {

    private ReadFile readFile = new ReadFile();

    @Test
    public void testAlpha() {
        readFile.betaTesting("sample2.csv", 15.00);
    }
}
