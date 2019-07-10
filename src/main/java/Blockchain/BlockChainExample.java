package main.java.Blockchain;

public class BlockChainExample {


    //TODO Transactions, Signature, Merkly tree for blocks, sign blocks, nodes (optional), unverified transaction pool
    public static void main(String[] args) {
        Blockchain bc = new Blockchain();
        bc.addBlock("Send 1 BTC to Ivan");
        bc.addBlock("Send 2 more BTC to Ivan");
        for (Block block : bc.getBlocks()) {
            System.out.println("Prev. hash: " +  block.getPreviousHash());
            System.out.println("Data: " +  block.getData());
            System.out.println("Hash: " +  block.getHash());
            System.out.println("Is valid: " +  ProofOfWork.Validate(block,block.getNonce()));
            System.out.println();
        }
    }
}
