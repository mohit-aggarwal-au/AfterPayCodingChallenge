package com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.exception.InvalidInputFileException;
import com.afterpay.codingchallenge.exception.InvalidParameterException;
import com.afterpay.codingchallenge.model.TransactionDetail;
import com.afterpay.codingchallenge.util.Utility;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CheckFraudulentTransactions {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);

    private DetectFraudService detectFraudService = new DetectFraudService();

    public Set<String> checkFraudulentTransactions(String fileName, BigDecimal threshold) {

        try {

            URL resourceURL = this.getClass().getClassLoader().getResource(fileName);
            if (resourceURL == null) {
                throw new InvalidInputFileException("Exception occurred while opening input file");
            }

            Path path = Paths.get(resourceURL.toURI());
            List<TransactionDetail> transactionDetails = Files.lines(path).map(mapToObject).collect(Collectors.toList());
            return detectFraudService.detectFraud(transactionDetails, threshold);
        } catch (IOException | URISyntaxException exception) {
            throw new InvalidInputFileException("Exception occurred while opening input file:" + exception.getMessage());
        }
    }

    private Function<String, TransactionDetail> mapToObject = (line) -> {
        String[] lineContent = line.split(Utility.COMMA);

        if (lineContent.length != 3) {
            throw new InvalidParameterException("Exception occurred while parsing transactions file :Input transaction file format is not correct");
        }
        TransactionDetail detail = new TransactionDetail();
        try {
            detail.setHashedCardNumber(lineContent[0].trim());
            Instant instant = simpleDateFormat.parse(lineContent[1]).toInstant();
            detail.setTransactionTime(instant);
            BigDecimal bigDecimal = new BigDecimal(lineContent[2].trim()).setScale(2, RoundingMode.HALF_UP);
            detail.setTransactionAmount(bigDecimal);
        } catch (ParseException | NumberFormatException exception) {
            throw new InvalidParameterException("Exception occurred while parsing transactions file:" + exception.getMessage());
        }

        return detail;
    };
}
