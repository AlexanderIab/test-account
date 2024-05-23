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


    public Transaction(TransactionManager transactionManager, CountDownLatch countDownLatch) {
        this.transactionManager = transactionManager;
        this.countDownLatch = countDownLatch;
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

            int amount = ThreadLocalRandom.current().nextInt(10000);

            if (transactionManager.transferMoney(accountFrom, accountTo, amount)) {
                countDownLatch.countDown();
            }
            try {
                Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(1001));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted", e);
            }
        }
    }
}
