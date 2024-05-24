package com.iablonski.testaccount;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private static final Logger logger = LogManager.getLogger(Account.class);
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

    // Снимает указанную сумму со счета, если достаточно средств
    public boolean withdrawal(int amount) {
        if (money.get() >= amount) {
            money.addAndGet(-amount);
            return true;
        }
        return false;
    }

    // Зачисляет указанную сумму на счет
    public void deposit(int amount) {
        money.addAndGet(amount);
    }
}