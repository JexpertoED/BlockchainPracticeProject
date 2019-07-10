package main.java.Blockchain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Block {

    private long timeStamp;
    private ArrayList<Transaction> data;
    private byte[] previousHash;
    private byte[] hash;
    private long nonce;

    public long getTimeStamp() {
        return timeStamp;
    }

    long getNonce() {
        return nonce;
    }

    void setNonce(long nonce) {
        this.nonce = nonce;
    }

    void setHash(byte[] hash) {
        this.hash = hash;
    }

    ArrayList<Transaction> getData() {
        return data;
    }

    byte[] getPreviousHash() {
        return previousHash;
    }

    byte[] getHash() {
        return hash;
    }

    private Block(long timeStamp, ArrayList<Transaction> data, byte[] previousHash, byte[] hash, long nonce) {
        this.timeStamp = timeStamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
        this.nonce = nonce;
    }

    byte[] calculateHash() {
        ByteArrayOutputStream totalData = new ByteArrayOutputStream();
        try {
            totalData.write(this.previousHash);
            totalData.write(Blockchain.hexStringToByteArray(Long.toHexString(this.timeStamp)));
            totalData.write(createMercleRoot(this.data));

            return SHA256.sha256(totalData.toByteArray());
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println("Calculate hash in Block exception");
        }

        return new byte[0];
    }

    static Block generateBlock(ArrayList<Transaction> data, byte[] previousHash) {
        Block newBlock = new Block(System.currentTimeMillis(), data, previousHash, new byte[0], 0);
        newBlock.hash = newBlock.calculateHash();
        ProofOfWork pow = new ProofOfWork(newBlock);
        try {
            pow.findProof();
            newBlock.nonce = pow.block.nonce;
            newBlock.hash = pow.block.hash;
        } catch (InterruptedException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Interrupted");
        }
        return newBlock;
    }


    private static byte[] createMercleRoot(ArrayList<Transaction> transactions) throws NoSuchAlgorithmException {
        if (transactions.size() == 0)
            return Blockchain.hexStringToByteArray(completeShaString(""));
        ArrayList<String> shas = new ArrayList<>(transactions.size());
        for (Transaction transaction : transactions) {
            shas.add(SHA256.sha256(transaction.getTransactionString()));
        }
        int count = transactions.size();
        int offset = 0;
        int newCount;
        boolean last;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        while (count != 1) {
            newCount = 0;
            last = (count & 1) == 1;
            if (last)
                count--;
            for (int i = offset; i < count + offset; i += 2) {
                BigInteger dblSha = new BigInteger(shas.get(i) + shas.get(i + 1), 16);
                shas.add(completeShaString(Blockchain.bytesToHex(digest.digest(digest.digest(dblSha.toByteArray())))));
                newCount++;
            }
            if (last) {
                BigInteger dblSha = new BigInteger(shas.get(count + offset) + shas.get(count + offset), 16);
                shas.add(completeShaString(Blockchain.bytesToHex(digest.digest(digest.digest(dblSha.toByteArray())))));
                newCount++;
                count++;
            }

            offset += count;
            count = newCount;
        }
        return Blockchain.hexStringToByteArray(shas.get(shas.size() - 1));
    }


    private static String completeShaString(String sha) {
        return completeStringWith(sha, 64);
    }

    private static String completeStringWith(String string, int len) {
        int need = len - string.length();
        if (need == 0)
            return string;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < need; i++) {
            builder.append("0");
        }
        return builder.append(string).toString();
    }
}
