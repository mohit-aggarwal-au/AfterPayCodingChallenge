package unit.com.nab.ms.risk.rating.core.application;

import com.nab.ms.risk.rating.core.application.FxCalculator;
import com.nab.ms.risk.rating.core.exception.InvalidParameterException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorServiceTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void fxCalculator_withValidInput_returnsSuccess() {
    FxCalculator.main(new String[]{"AUD", "100.00", "in", "NOK"});
    assertEquals("AUD 100.00 = NOK 588.97", outContent.toString());
  }

  @ParameterizedTest
  @MethodSource("invalidCurrencyValues")
  public void fxCalculator_withInvalidCurrencyValue_returnsErrorMessage(String[] input, String fromCurrency, String toCurrency) {
    FxCalculator.main(input);
    assertEquals(String.format("Unable to find rate for %s/%s", fromCurrency, toCurrency), outContent.toString());
  }

  private static Stream<Arguments> invalidCurrencyValues() {
    return Stream.of(Arguments.of(new String[]{"AUD", "100.00", "in", "RED"}, "AUD", "RED"),
        Arguments.of(new String[]{"FJD", "100.00", "in", "DKK"}, "FJD", "DKK"),
        Arguments.of(new String[]{"KRW", "100.00", "in", "FJD"}, "KRW", "FJD"),
        Arguments.of(new String[]{"Random_value", "100.00", "in", "unknown_value"}, "RANDOM_VALUE", "UNKNOWN_VALUE")
    );
  }

  @ParameterizedTest
  @MethodSource("incorrectNumberOfParameterValue")
  public void fxCalculator_withIncorrectNumberOfParameterValue_throwsException(String[] input) {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
        FxCalculator.main(input), "Expected to throw InvalidParameterException, but didn't throw it");
    exception.getMessage();
    assertTrue(exception.getMessage().contains("Input parameters are not correct"));
  }

  static Stream<Arguments> incorrectNumberOfParameterValue() {
    return Stream.of(
        Arguments.of((Object) new String[]{"AUD", "100.00", "in", "DKK", "extra_parameter"}),
        Arguments.of((Object) new String[]{"KRW", "100.00", "FJD"})
    );
  }

  @ParameterizedTest
  @MethodSource("invalidParameterValue")
  public void fxCalculator_withInvalidParameterValue_throwsException(String[] input) {
    InvalidParameterException exception = assertThrows(InvalidParameterException.class, () ->
        FxCalculator.main(input), "Expected to throw InvalidParameterException, but didn't throw it");
    exception.getMessage();
    assertTrue(exception.getMessage().contains("Exception occurred while parsing input amount"));
  }

  static Stream<Arguments> invalidParameterValue() {
    return Stream.of(
        Arguments.of((Object) new String[]{null, "100.00", "in", "DKK"}),
        Arguments.of((Object) new String[]{null, null, null, null}),
        Arguments.of((Object) new String[]{"KRW", "Asd.23", "FJD"})
    );
  }

}

package unit.com.nab.ms.risk.rating.core.application;

import com.nab.ms.risk.rating.core.application.CalculatorService;
import com.nab.ms.risk.rating.core.application.FxCalculator;
import com.nab.ms.risk.rating.core.exception.InvalidParameterException;
import com.nab.ms.risk.rating.core.model.Currency;
import com.nab.ms.risk.rating.core.model.InputDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorServiceTest2 {

  private CalculatorService calculatorService = new CalculatorService();

  @ParameterizedTest
  @CsvFileSource(resources = "/sample.csv", numLinesToSkip = 1)
  void calculatorService_wuthVaidValues_returnsSuccess(String fromCurrencyString, String toCurrencyString, String amount, String expected) {
    Currency fromCurrency = Currency.valueOf(fromCurrencyString);
    Currency toCurrency = Currency.valueOf(toCurrencyString);
    BigDecimal amountToBeConverted = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    InputDetails inputDetails  = new InputDetails(fromCurrency, toCurrency, amountToBeConverted);
    assertEquals(expected, calculatorService.convertMoney(inputDetails).toString());

  }

}

package unit.com.nab.ms.risk.rating.core.application;

import com.nab.ms.risk.rating.core.exception.InvalidInputFileException;
import com.nab.ms.risk.rating.core.model.Currency;
import com.nab.ms.risk.rating.core.util.ReadFileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReadFileUtilTest {


  @ParameterizedTest
  @ValueSource(strings = {"nonExistentFile.csv",
      "valid_input#$%#$abc.csv"})
  public void getCrossCurrencyMap_invalidFile_throwsInvalidParameterException(String fileName) {
    InvalidInputFileException exception = assertThrows(InvalidInputFileException.class, () ->
            ReadFileUtil.getCrossCurrencyMap(fileName)
        , "Expected to throw InvalidInputFileException, but didn't throw it");
    exception.getMessage();
    assertTrue(exception.getMessage().contains("Exception occurred while opening input file"));
  }

  @Test
  public void getCrossCurrencyMap_validFile_returnSuccess() {
    Map<String, Currency> crossCurrencyMap = ReadFileUtil.getCrossCurrencyMap("cross_currency_test.csv");
    assertEquals(4, crossCurrencyMap.size());

  }

  @Test
  public void getCurrencyRateMap_validFile_returnSuccess() {
    Map<String, BigDecimal> currencyRateMap = ReadFileUtil.getCurrencyRateMap("currency_rates_test.csv");
    assertEquals(4, currencyRateMap.size());

  }

  @Test
  public void getCurrencyDecimalPoints_validFile_returnSuccess() {
    Map<Currency, Integer>currencyDecimalPointsMap = ReadFileUtil.getCurrencyDecimalPoints("currency_decimal_points_test.csv");
    assertEquals(2, currencyDecimalPointsMap.size());

  }

  @ParameterizedTest
  @ValueSource(strings = {"nonExistentFile.csv",
      "valid_input#$%#$abc.csv"})
  public void getCurrencyRateMap_invalidFile_throwsInvalidParameterException(String fileName) {
    InvalidInputFileException exception = assertThrows(InvalidInputFileException.class, () ->
            ReadFileUtil.getCurrencyRateMap(fileName)
        , "Expected to throw InvalidInputFileException, but didn't throw it");
    exception.getMessage();
    assertTrue(exception.getMessage().contains("Exception occurred while opening input file"));
  }


  @ParameterizedTest
  @ValueSource(strings = {"nonExistentFile.csv",
      "valid_input#$%#$abc.csv"})
  public void getCurrencyDecimalPoints_invalidFile_throwsInvalidParameterException(String fileName) {
    InvalidInputFileException exception = assertThrows(InvalidInputFileException.class, () ->
            ReadFileUtil.getCurrencyDecimalPoints(fileName)
        , "Expected to throw InvalidInputFileException, but didn't throw it");
    exception.getMessage();
    assertTrue(exception.getMessage().contains("Exception occurred while opening input file"));
  }
}

  public static void main(String[] args) {
    InputDetails inputDetails = getInputDetails(args);
    if (inputDetails != null) {
      CalculatorService calculatorService = new CalculatorService();
      BigDecimal convertedAmount = calculatorService.convertMoney(inputDetails);
      System.out.print(inputDetails.getFromCurrency() + " " + inputDetails.getAmountToBeConverted() + " = " + inputDetails.getToCurrency() + " " + convertedAmount);
    }

  }

  private static InputDetails getInputDetails(String[] args) {
    if (args == null || args.length != 4) {
      throw new InvalidParameterException("Input parameters are not correct");
    }

    InputDetails inputDetails = null;
    try {
      Currency fromCurrency = Currency.valueOf(args[0].trim());
      Currency toCurrency = Currency.valueOf(args[3].trim());
      BigDecimal amountToBeConverted = new BigDecimal(args[1].trim()).setScale(2, RoundingMode.HALF_UP);
      inputDetails = new InputDetails(fromCurrency, toCurrency, amountToBeConverted);

    } catch (NumberFormatException exception) {
      System.out.print(String.format("Exception occurred while parsing input values:" + exception.getMessage()));
    } catch (IllegalArgumentException exception) {
      System.out.print(String.format("Unable to find rate for %s/%s", args[0].trim().toUpperCase(), args[3].trim().toUpperCase()));
      return null;
    } catch (Exception exception) {
      throw new InvalidParameterException("Exception occurred while parsing input values:" + exception.getMessage());
    }

    return inputDetails;
  }
}

