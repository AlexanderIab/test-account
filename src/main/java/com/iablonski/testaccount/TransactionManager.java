package com.iablonski.testaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);
    private final List<Account> accounts;
    private final Lock lock = new ReentrantLock();

    public TransactionManager(List<Account> accounts) {
        this.accounts = accounts;
    }

    public boolean transferMoney(Account transferFrom, Account transferTo, int amount) {
        lock.lock();
        try {
            if (transferFrom.withdrawal(amount)) {
                transferTo.deposit(amount);
                logger.info("Transferred {} from {} to {}", amount, transferFrom.getId(), transferTo.getId());
                return true;
            } else {
                logger.info("Transferred {} from {} to {}", amount, transferFrom.getId(), transferTo.getId());
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
