package com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.model.TransactionDetail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReadFile {

   public void betaTesting(String fileName, double threshold) {
        try {
            Path path = Paths.get(this.getClass().getClassLoader()
                    .getResource(fileName).toURI());
            List<TransactionDetail> transactionDetails = Files.lines(path).map(mapToObject).collect(Collectors.toList());
            System.out.println(transactionDetails.size());

            DetectFraud detectFraud = new DetectFraud();
            detectFraud.detectFraud(transactionDetails, threshold);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Function<String, TransactionDetail> mapToObject = (line) -> {
        String[] lineContent = line.split(",");
        TransactionDetail detail = new TransactionDetail();
        detail.setHashedCardNumber(lineContent[0].trim());
        Instant instant = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            instant = sdf.parse(lineContent[1]).toInstant();
        } catch (Exception e) {
            e.printStackTrace();
        }
        detail.setTransactionTime(instant);
        detail.setTransactionAmount(Double.parseDouble(lineContent[2].trim()));
        return detail;
    };
}
