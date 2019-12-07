package com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.model.TransactionDetail;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DetectFraud {

    public List<String> detectFraud(List<TransactionDetail> transactionDetails, double threshold) {

        Map<String, Double> fraudMap = transactionDetails.stream().collect(Collectors.groupingBy(TransactionDetail::getHashedCardNumber, Collectors.summingDouble(TransactionDetail::getTransactionAmount)));
        List<String> fraudList = fraudMap.entrySet().stream().filter(map -> map.getValue() >= threshold).map(map -> map.getKey()).collect(Collectors.toList());
        return fraudList;
    }
    public boolean isFraudDetected(List<TransactionDetail> transactionDetails, String hashedCardNumber, double threshold) {
        //Double totalTransactionMoney = transactionDetails.stream().filter(detail -> detail.getHashedCardNumber().equals(hashedCardNumber)).collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));
        Double totalTransactionMoney = transactionDetails.stream().collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));
        return totalTransactionMoney>=threshold;
   }

}
