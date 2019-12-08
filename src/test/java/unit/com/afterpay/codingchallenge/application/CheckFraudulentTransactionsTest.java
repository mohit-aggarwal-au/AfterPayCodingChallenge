package unit.com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.application.CheckFraudulentTransactions;
import org.junit.Test;

public class CheckFraudulentTransactionsTest {

    private CheckFraudulentTransactions checkFraudulentTransactions = new CheckFraudulentTransactions();

    @Test
    public void CheckFraudulentTransaction() {
        checkFraudulentTransactions.checkFraudulentTransactions("sample.txt", 15.00);
    }
}
