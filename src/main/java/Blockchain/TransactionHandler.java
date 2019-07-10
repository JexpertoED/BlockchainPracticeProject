package main.java.Blockchain;

import java.util.HashSet;

public class TransactionHandler {
    private HashSet<Transaction> unverifiedTransactionPool;
    private HashSet<Transaction> verifiedTransactionPool;


    public void addTransactionToPool(Transaction transaction) {
        unverifiedTransactionPool.add(transaction);
    }

}
