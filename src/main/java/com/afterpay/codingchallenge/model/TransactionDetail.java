package com.afterpay.codingchallenge.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TransactionDetail {

    private String hashedCardNumber;

    private Instant transactionTime;

    private double transactionAmount;
}
