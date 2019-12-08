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
            //Total transaction amount is the sum of all the transaction amount that happened over a 24 hour sliding window period for a given credit card of a transaction
//            Double totalTransactionAmount = transactionDetails.stream().filter(isValidTimeWindow(detail.getHashedCardNumber(), detail.getTransactionTime())).
//                    collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));

            List<TransactionDetail> slidingTimeWindowList = transactionDetails.stream().filter(isValidTimeWindow(detail.getHashedCardNumber(), detail.getTransactionTime())).collect(Collectors.toList());
            Double totalTransactionAmount = slidingTimeWindowList.stream().collect(Collectors.summingDouble(TransactionDetail::getTransactionAmount));

            if (totalTransactionAmount > threshold) {
                // There may be duplicate hashed card numbers which were detected as fraudulent over multiple 24 hours sliding window periods, program will return only distinct hashed card values
                // List will also contain card numbers for which a single transaction amount is more than threshold.
                fraudSet.add(detail.getHashedCardNumber());
            }
        });
        return fraudSet;
    }

    private static Predicate<TransactionDetail> isValidTimeWindow(String hashedCreditCardNumber, Instant transactionTime) {

        //For a given hashed card number, TransactionToBeChecked time has to be less than or equal to the transaction time and more than or equal to (transaction time - 24 hours)
        return transactionToBeChecked -> transactionToBeChecked.getHashedCardNumber().equals(hashedCreditCardNumber) &&
                (transactionTime.compareTo(transactionToBeChecked.getTransactionTime()) >= 0 && transactionTime.minus(Duration.ofHours(24)).compareTo(transactionToBeChecked.getTransactionTime()) <= 0);
    }
}
