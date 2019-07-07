package main.java.Blockchain;

public class Block {

    private long timeStamp;
    private String data;
    private String previousHash;
    private String hash;

    public long getTimeStamp() {
        return timeStamp;
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

    private Block(long timeStamp, String data, String previousHash, String hash) {
        this.timeStamp = timeStamp;
        this.data = data;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    public String calculateHash() {
        String headers = this.previousHash + this.data + this.timeStamp ;
        return SHA256.getSHA(headers);
    }

    public static Block generateBlock(String data, String previousHash) {
        Block newBlock = new Block (System.currentTimeMillis(), data, previousHash, "");
        newBlock.hash = newBlock.calculateHash();
        return newBlock;
    }

}
