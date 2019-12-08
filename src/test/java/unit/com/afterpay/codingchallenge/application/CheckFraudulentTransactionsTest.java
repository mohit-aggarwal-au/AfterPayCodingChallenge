package unit.com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.application.CheckFraudulentTransactions;
import com.afterpay.codingchallenge.exception.InvalidParameterException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckFraudulentTransactionsTest {

    private CheckFraudulentTransactions checkFraudulentTransactions = new CheckFraudulentTransactions();

    private String invalidInputDirectory = "invalid_input";

    @ParameterizedTest
    @ValueSource(strings = {"transaction_list_with_invalid_amount.csv",
            "transaction_list_with_invalid_row.csv",
            "transaction_list_with_invalid_time.csv"})
    public void CheckFraudulentTransaction(String fileName) {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                        checkFraudulentTransactions.checkFraudulentTransactions(invalidInputDirectory + "/" + fileName, 15.00)
                , "Expected to throw InvalidParameterException, but didn't throw it");
        exception.getMessage();
        assertTrue(exception.getMessage().contains("Exception occurred while parsing transactions file"));
    }
}

