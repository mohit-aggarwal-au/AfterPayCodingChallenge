package com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.exception.InvalidInputFileException;
import com.afterpay.codingchallenge.exception.InvalidParameterException;
import com.afterpay.codingchallenge.model.TransactionDetail;
import com.afterpay.codingchallenge.util.Utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CheckFraudulentTransactions {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.DATE_FORMAT);

    public void checkFraudulentTransactions(String fileName, double threshold) {

        try {
            Path path = Paths.get(this.getClass().getClassLoader()
                    .getResource(fileName).toURI());
            List<TransactionDetail> transactionDetails = Files.lines(path).map(mapToObject).collect(Collectors.toList());
            System.out.println(transactionDetails.size());
            DetectFraudService detectFraudService = new DetectFraudService();
            detectFraudService.detectFraud(transactionDetails, threshold);
        } catch (IOException | URISyntaxException exception) {
            throw new InvalidInputFileException("Exception occurred while opening input file:" + exception.getMessage());
        }
    }

    private Function<String, TransactionDetail> mapToObject = (line) -> {
        String[] lineContent = line.split(Utility.COMMA);

        if (lineContent.length != 3) {
            throw new InvalidParameterException("Input transaction file format is not correct");
        }
        TransactionDetail detail = new TransactionDetail();
        try {
            detail.setHashedCardNumber(lineContent[0].trim());
            Instant instant = simpleDateFormat.parse(lineContent[1]).toInstant();
            detail.setTransactionTime(instant);
            detail.setTransactionAmount(Double.parseDouble(lineContent[2].trim()));
        } catch (ParseException | NumberFormatException exception) {
            throw new InvalidParameterException("Exception occurred while parsing transactions file:" + exception.getMessage());
        }

        return detail;
    };
}
