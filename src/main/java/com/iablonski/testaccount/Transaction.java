package com.iablonski.testaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class Transaction implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private final TransactionManager transactionManager;
    private final CountDownLatch countDownLatch;

    private static final int MAX_SLEEP_TIME = 2000;
    private static final int MIN_SLEEP_TIME = 1000;
    private final int maxTransferAmount;


    public Transaction(TransactionManager transactionManager, CountDownLatch countDownLatch, int maxTransferAmount) {
        this.transactionManager = transactionManager;
        this.countDownLatch = countDownLatch;
        this.maxTransferAmount  = maxTransferAmount;
    }

    @Override
    public void run() {
        List<Account> accounts = transactionManager.getAccounts();
        while (countDownLatch.getCount() > 0) {
            int indexAccountFrom = ThreadLocalRandom.current().nextInt(accounts.size());
            int indexAccountTo;
            do {
                indexAccountTo = ThreadLocalRandom.current().nextInt(accounts.size());

            } while (indexAccountFrom == indexAccountTo);

            Account accountFrom = accounts.get(indexAccountFrom);
            Account accountTo = accounts.get(indexAccountTo);

            int amount = ThreadLocalRandom.current().nextInt(maxTransferAmount);

            if (transactionManager.transferMoney(accountFrom, accountTo, amount)) {
                countDownLatch.countDown();
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(MIN_SLEEP_TIME, MAX_SLEEP_TIME + 1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted", e);
            }
        }
    }
}