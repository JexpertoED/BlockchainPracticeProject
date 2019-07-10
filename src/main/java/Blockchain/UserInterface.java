package main.java.Blockchain;

import java.util.ArrayList;
import java.util.HashSet;

class UserInterface {
    private ArrayList<Transaction> transactionPool;

    UserInterface(ArrayList<Transaction> transactionPool) {
        this.transactionPool = transactionPool;
    }

    void addTransactionToPool(Transaction transaction){
        transactionPool.add(transaction);
    }

//    public void addTransactionToPool(Transaction transaction) {
//        unverifiedTransactionPool.add(transaction);
//    }

}
