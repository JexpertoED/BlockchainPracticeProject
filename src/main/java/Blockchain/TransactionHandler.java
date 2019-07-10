package main.java.Blockchain;

import java.util.HashSet;

public class TransactionHandler {
    private HashSet<Transaction> transactionPool;

    public void addTransactionToPool(Transaction transaction) {
        transactionPool.add(transaction);
    }

    public boolean validateTransaction(Transaction transaction) {
        return false;
    }

    public void createSignature(Transaction transaction) {
    }
    public boolean verifySignature(){
        return false;
    }
}
