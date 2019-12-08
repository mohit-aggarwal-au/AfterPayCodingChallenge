package com.afterpay.codingchallenge.application;

import com.afterpay.codingchallenge.model.TransactionDetail;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DetectFraud {

    public Set<String> detectFraud(List<TransactionDetail> transactionDetails, double threshold) {

        Set<String> fraudSet = new HashSet<>();
        transactionDetails.forEach(detail -> {
            List<TransactionDetail> slidingTimeWindowList = transactionDetails.stream().filter(isValidTimeWindow(detail.getHashedCardNumber(), detail.getTransactionTime())).collect(Collectors.toList());

            //Double totalTransactionMoney = transactionDetails.stream().filter(isValidTimeWindow(detail.getHashedCardNumber(), detail.getTransactionTime())).collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));
            if (isFraudDetected(slidingTimeWindowList, threshold)) {
                fraudSet.add(detail.getHashedCardNumber());
            }
        });
        return fraudSet;
    }

    private static Predicate<TransactionDetail> isValidTimeWindow(String hashedCreditCardNumber, Instant transactionTime) {

        //For a given hashed card number, TransactionToBeChecked has to be less than or equal to the transaction time and more than or equal to transaction time - 24 hours
        return transactionToBeChecked -> transactionToBeChecked.getHashedCardNumber().equals(hashedCreditCardNumber) &&
                (transactionTime.compareTo(transactionToBeChecked.getTransactionTime()) >= 0 && transactionTime.minus(Duration.ofHours(24)).compareTo(transactionToBeChecked.getTransactionTime()) <= 0);
    }

    private boolean isFraudDetected(List<TransactionDetail> transactionDetails, double threshold) {
        Double totalTransactionMoney = transactionDetails.stream().collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));
        return totalTransactionMoney >= threshold;
    }

}
