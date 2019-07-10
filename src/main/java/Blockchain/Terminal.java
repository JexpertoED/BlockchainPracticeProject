package main.java.Blockchain;

public class Terminal {


    //TODO Transactions, Signature, Merkly tree for blocks, sign blocks, nodes (optional), unverified transaction pool
    public static void main(String[] args) throws Exception {
        Blockchain bc = new Blockchain();
        bc.run();
        bc.connectionTest();
        for (Block block : bc.getBlocks()) {
            System.out.println("Prev. hash: " +  block.getPreviousHash());
            System.out.println("Data: " +  block.getData());
            System.out.println("Hash: " +  block.getHash());
            System.out.println("Is valid: " +  ProofOfWork.Validate(block,block.getNonce()));
            System.out.println();
        }
    }
}
