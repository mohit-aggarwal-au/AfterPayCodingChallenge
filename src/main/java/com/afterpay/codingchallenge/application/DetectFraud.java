package com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.model.TransactionDetail;

import java.util.List;
import java.util.stream.Collectors;

public class DetectFraud {

    public boolean isFraudDetected(List<TransactionDetail> transactionDetails, String hashedCardNumber, double threshold) {
        //Double totalTransactionMoney = transactionDetails.stream().filter(detail -> detail.getHashedCardNumber().equals(hashedCardNumber)).collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));
        Double totalTransactionMoney = transactionDetails.stream().collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));
        return totalTransactionMoney>=threshold;
   }

}
