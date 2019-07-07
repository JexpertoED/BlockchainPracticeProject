package main.java.Blockchain;

import javax.swing.*;

public class Block {

    private long timeStamp;
    private String data;
    private String previousHash;
    private String hash;
    private long nonce;

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getData() {
        return data;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    private Block(long timeStamp, String data, String previousHash, String hash, long nonce) {
        this.timeStamp = timeStamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.nonce = nonce;
    }

    public String calculateHash() {                     // Deprecated
        String headers = this.previousHash + this.data + this.timeStamp ;
        return SHA256.sha256(headers);
    }

    public static Block generateBlock(String data, String previousHash) {
        Block newBlock = new Block (System.currentTimeMillis(), data, previousHash, "", 0);
        //newBlock.hash = newBlock.calculateHash();
        ProofOfWork pow = new ProofOfWork(newBlock);
        try {
            pow.findProof();
            newBlock.nonce = pow.block.nonce;
            newBlock.hash = pow.block.hash;
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Interrupted");
        }
        return newBlock;
    }

}
