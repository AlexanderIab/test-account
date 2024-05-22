package com.iablonski.testaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Transaction implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private final Random random = new Random();
    private final TransactionManager transactionManager;
    private final CountDownLatch countDownLatch;


    public Transaction(TransactionManager transactionManager, CountDownLatch countDownLatch) {
        this.transactionManager = transactionManager;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        List<Account> accounts = transactionManager.getAccounts();
        try {
            int indexAccountFrom = random.nextInt(accounts.size());
            int indexAccountTo = 0;
            do {
                indexAccountTo = random.nextInt(accounts.size());

            } while (indexAccountFrom == indexAccountTo);

            Account accountFrom = accounts.get(indexAccountFrom);
            Account accountTo = accounts.get(indexAccountTo);

            int amount = random.nextInt(10000);

            if (transactionManager.transferMoney(accountFrom, accountTo, amount)) {
                logger.info("Successfully transferred {} from {} to {}", amount, accountFrom.getId(), accountTo.getId());
            }
            try {
                Thread.sleep(1000 + random.nextInt(1001));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted", e);
            }
        } finally {
            countDownLatch.countDown();
        }
    }
}
