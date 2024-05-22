package com.iablonski.testaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int INITIAL_BALANCE = 1000;
    private static final int TRANSACTION_COUNT = 30;
    private static final int THREAD_COUNT = 2;
    private static final int ACCOUNT_COUNT = 4;

    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            accounts.add(new Account(UUID.randomUUID().toString(), INITIAL_BALANCE));
        }

        TransactionManager transactionManager = new TransactionManager(accounts);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(TRANSACTION_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(new Transaction(transactionManager, countDownLatch));
        }
        executorService.shutdown();

        try {
            while (executorService.isTerminated()) {
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
