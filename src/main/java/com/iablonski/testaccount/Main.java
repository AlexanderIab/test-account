package com.iablonski.testaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);

        Properties prop = new Properties();

        try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            logger.error("Error reading configuration file. Exception {}", e.getMessage());
            return;
        }

        int INITIAL_BALANCE = Integer.parseInt(prop.getProperty("INITIAL_BALANCE"));
        int ACCOUNT_NUMBER = Integer.parseInt(prop.getProperty("ACCOUNT_NUMBER"));
        int THREAD_NUMBER = Integer.parseInt(prop.getProperty("THREAD_NUMBER"));
        int TRANSACTION_NUMBER = Integer.parseInt(prop.getProperty("TRANSACTION_NUMBER"));

        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < ACCOUNT_NUMBER; i++) {
            accounts.add(new Account(UUID.randomUUID().toString(), INITIAL_BALANCE));
        }

        TransactionManager transactionManager = new TransactionManager(accounts);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
        CountDownLatch countDownLatch = new CountDownLatch(TRANSACTION_NUMBER);

        for (int i = 0; i < THREAD_NUMBER; i++) {
            executorService.submit(new Transaction(transactionManager, countDownLatch));
        }

        try {
            countDownLatch.await();
            logger.info("Transactions completed.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted.", e);
        } finally {
            executorService.shutdown();
        }
    }
}