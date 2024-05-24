package com.iablonski.testaccount;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        Logger logger = LogManager.getLogger(Main.class);

        Properties prop = new Properties();

        try (FileInputStream input = new FileInputStream("src/main/resources/config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            logger.error("Error reading configuration file. Exception {}", e.getMessage());
            return;
        }

        int INITIAL_BALANCE = Integer.parseInt(prop.getProperty("INITIAL_BALANCE"));
        int ACCOUNTS_NUMBER = Integer.parseInt(prop.getProperty("ACCOUNTS_NUMBER"));
        int THREADS_NUMBER = Integer.parseInt(prop.getProperty("THREADS_NUMBER"));
        int TRANSACTIONS_NUMBER = Integer.parseInt(prop.getProperty("TRANSACTIONS_NUMBER"));
        int MAX_TRANSFER_AMOUNT = Integer.parseInt(prop.getProperty("MAX_TRANSFER_AMOUNT"));

        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < ACCOUNTS_NUMBER; i++) {
            accounts.add(new Account(UUID.randomUUID().toString(), INITIAL_BALANCE));
        }

        TransactionManager transactionManager = new TransactionManager(accounts);
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_NUMBER);
        CountDownLatch countDownLatch = new CountDownLatch(TRANSACTIONS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            executorService.submit(new Transaction(transactionManager, countDownLatch, MAX_TRANSFER_AMOUNT));
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