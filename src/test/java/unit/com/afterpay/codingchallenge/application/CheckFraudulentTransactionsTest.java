package unit.com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.application.CheckFraudulentTransactions;
import com.afterpay.codingchallenge.exception.InvalidInputFileException;
import com.afterpay.codingchallenge.exception.InvalidParameterException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckFraudulentTransactionsTest {

    private CheckFraudulentTransactions checkFraudulentTransactions = new CheckFraudulentTransactions();

    private String invalidInputDirectory = "invalid_input";

    private String validInputDirectory = "valid_input";

    @ParameterizedTest
    @ValueSource(strings = {"transaction_list_with_invalid_amount.csv",
            "transaction_list_with_invalid_row.csv",
            "transaction_list_with_invalid_time.csv"})
    public void checkFraudulentTransactions_invalidContentFile_throwsInvalidParameterException(String fileName) {
        InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
                        checkFraudulentTransactions.checkFraudulentTransactions(invalidInputDirectory + "/" + fileName, new BigDecimal("15.00"))
                , "Expected to throw InvalidParameterException, but didn't throw it");
        exception.getMessage();
        assertTrue(exception.getMessage().contains("Exception occurred while parsing transactions file"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"nonExistentFile.csv",
            "valid_input#$%#$abc.csv"})
    public void checkFraudulentTransactions_invalidFile_throwsInvalidParameterException(String fileName) {
        InvalidInputFileException exception = assertThrows(InvalidInputFileException.class, () ->
                        checkFraudulentTransactions.checkFraudulentTransactions(fileName, new BigDecimal("15.00"))
                , "Expected to throw InvalidInputFileException, but didn't throw it");
        exception.getMessage();
        assertTrue(exception.getMessage().contains("Exception occurred while opening input file"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"transaction_list_with_valid_values.csv"})
    public void checkFraudulentTransactions_withValidValues_returnsSuccess(String fileName) {

        Set<String> fraudCardNumberSet = checkFraudulentTransactions.checkFraudulentTransactions(validInputDirectory + "/" + fileName, new BigDecimal("15.00"));
        assertEquals(3, fraudCardNumberSet.size());
        assertTrue(fraudCardNumberSet.contains("10d7ce2f43e35fa57d1bbf8b1e3"));
        assertTrue(fraudCardNumberSet.contains("10d7ce2f43e35fa57d1bbf8b1e4"));
        assertTrue(fraudCardNumberSet.contains("10d7ce2f43e35fa57d1bbf8b1e7"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"transaction_list_valid_values_testing_boundary_conditions.csv"})
    public void checkFraudulentTransactions_withValidValues_boundaryConditions_returnsSuccess(String fileName) {
        Set<String> fraudCardNumberSet = checkFraudulentTransactions.checkFraudulentTransactions(validInputDirectory + "/" + fileName, new BigDecimal("15.00"));
        assertEquals(fraudCardNumberSet.size(), 0);
    }
}

