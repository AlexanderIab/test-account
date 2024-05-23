package com.iablonski.testaccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private static final Logger logger = LoggerFactory.getLogger(Account.class);
    private final String id;
    private final AtomicInteger money;

    public Account(String id, int money) {
        this.id = id;
        this.money = new AtomicInteger(money);
        logger.info("Created account {} with balance {}", id, money);
    }

    public String getId() {
        return id;
    }

    public AtomicInteger getMoney() {
        return money;
    }

    public boolean withdrawal(int amount) {
        if (money.get() >= amount) {
            money.addAndGet(-amount);
            return true;
        }
        return false;
    }

    public void deposit(int amount) {
        money.addAndGet(amount);
    }
}