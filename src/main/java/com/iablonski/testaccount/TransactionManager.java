package com.iablonski.testaccount;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionManager {
    private static final Logger logger = LogManager.getLogger(TransactionManager.class);
    private final List<Account> accounts;
    private final Lock lock = new ReentrantLock();
    private int transactionNumber;

    public TransactionManager(List<Account> accounts) {
        this.accounts = accounts;
        this.transactionNumber = 1;
    }

    // Метод для перевода средств между счетами
    public boolean transferMoney(Account transferFrom, Account transferTo, int amount) {
        lock.lock();
        try {
            // Сохранение изначального баланса (до транзакции) для отката изменений
            int initialFromBalance = transferFrom.getMoney().get();
            int initialToBalance = transferTo.getMoney().get();

            if (transferFrom.withdrawal(amount)) {
                try {
                    transferTo.deposit(amount);
                    logger.info("Transaction number: {} starts", transactionNumber);
                    logger.info("Transferred {} from {} to {}", amount, transferFrom.getId(), transferTo.getId());
                    logger.info("Balance of the account {} is {} ", transferFrom.getId(), transferFrom.getMoney().get());
                    logger.info("Balance of the account {} is {} ", transferTo.getId(), transferTo.getMoney().get());
                    logger.info("Transaction number: {} ends", transactionNumber);
                    logger.info("");
                    transactionNumber++;
                    return true;
                } catch (Exception e){
                    // Необязательное условие - если возникает критическая ошибка,
                    // сумма на балансах становится изначальной (до транзакции)
                    logger.error("Fatal error - {} during transaction from {} to {}. Rollback.",
                            e.getMessage(), transferFrom.getId(), transferTo.getId());
                    transferFrom.getMoney().set(initialFromBalance);
                    transferTo.getMoney().set(initialToBalance);
                    return false;
                }
            } else {
                logger.warn("An attempt to transfer amount {} from account {} to account {}. " +
                                "Transaction failed: Insufficient funds in {}. Account balance - {}",
                        amount, transferFrom.getId(), transferTo.getId(), transferFrom.getId(), transferFrom.getMoney().get());
                logger.info("");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}