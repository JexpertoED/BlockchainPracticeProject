package main.java.Blockchain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

class Blockchain {
    private static final int blockSize = 4;
    private List<Block> blocks;
    private ArrayList<Transaction> transactionPool = new ArrayList<>();
    private ArrayList<EventListener.AddBlockEvent> handlers = new ArrayList<>();
    private int poolTransactionsCount = 0;

    List<Block> getBlocks() {
        return blocks;
    }


    void run() throws InterruptedException {
        Thread a = null;
        //Thread finalB = a;
        a = new Thread(() -> {
            ArrayList<Transaction> temp = new ArrayList<>();
            while (true) {
                synchronized (transactionPool) {
                    try {
                        if (temp.size() < blockSize && !transactionPool.isEmpty()) {
                                temp.add(transactionPool.get(0));
                            //    System.out.println(temp);
                                transactionPool.remove(0);
                        } else if (!temp.isEmpty()) {
                          // System.out.println(temp + "   1");
                            addBlock(temp);
                            temp = new ArrayList<>();
                        }
                    } catch (IndexOutOfBoundsException ignore) {
                    }
                }
//                try {
//                    finalB.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
        a.start();
        //a.join();



    }

    void connectionTest() throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        
        transactionPool.add(new Transaction("123".getBytes(), "1231245".getBytes(), "5543".getBytes(), true, "addresshash".getBytes()));
        transactionPool.add(new Transaction("12asd3".getBytes(), "1asda231245".getBytes(), "5fgh543".getBytes(), true, "addresshasadsh".getBytes()));
        transactionPool.add(new Transaction("12fsh3".getBytes(), "123sfgh1245".getBytes(), "554dfgh3".getBytes(), true, "addressasdfhgjhash".getBytes()));
        transactionPool.add(new Transaction("12fsh3".getBytes(), "123sfgh1245".getBytes(), "554dfgh3".getBytes(), true, "addressasdfhgjhash".getBytes()));
        transactionPool.add(new Transaction("12123fsh3".getBytes(), "123s456fgh1245".getBytes(), "554456dfgh3".getBytes(), true, "addressasdfhgjhash".getBytes()));
        transactionPool.add(new Transaction("12f12378sh3".getBytes(), "123sfg789h1245".getBytes(), "554d46456fgh3".getBytes(), true, "address789746as456dfhgjhash".getBytes()));
        transactionPool.add(new Transaction("12f4564sh3".getBytes(), "123sfg7123h1245".getBytes(), "554df123456gh3".getBytes(), true, "address457sdfhgjhash".getBytes()));
        for (Transaction transactions : transactionPool) {
            //System.out.println(transactions);
        }
    }
    void addTransactionToPool(Transaction transaction) {
        transactionPool.add(transaction);
        fireEventListeners(transaction);
    }

    void addTransactionToPoolWithCheck(Transaction transaction) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
//        boolean allowCheck = false;
//        for (Block block : blocks){
//            for (Transaction tr : block.getData()){
//              if (Arrays.equals(tr.getHash(), transaction.getInput().prevHash)){
//                  byte[] refTransactionPubKeyHash = tr.getOutput().pubKeyHash;
//                  byte[] pubSig = transaction.getInput().scriptSig;
//                  byte[] pubKey = Arrays.copyOf(pubSig, 91);
//                  if (Arrays.equals(SHA256.sha256(pubKey), refTransactionPubKeyHash)){
//                      System.out.println("!!!!!!!!!!!!PubKeys match!!!!!!!!!!!!");
//                      iKf (transaction.)
//                  }
//              }
//            }
//        }
//        transactionPool.add(transaction);
    }


    Blockchain() {
        List<Block> blocks = new ArrayList<>(1);
        Block genesisBlock = newGenesisBlock();
        blocks.add(genesisBlock);
        this.fireEventListeners(genesisBlock);
        this.blocks = blocks;
    }

    private void addBlock(ArrayList<Transaction> transactions) {
        Block prevBlock = this.blocks.get(this.blocks.size() - 1);
        Block newBlock = Block.generateBlock(transactions, prevBlock.getHash());
        this.blocks.add(newBlock);
        this.fireEventListeners(newBlock);
    }

    private Block newGenesisBlock() {
        ArrayList<Transaction> gen = new ArrayList<>();
        try {
            gen.add(new Transaction(new byte[0], "Genesis block".getBytes(), "Genesis block".getBytes(), true, new byte[0]));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
        return Block.generateBlock(gen, new byte[0]);
    }


    public void addEventListener(EventListener.AddBlockEvent a){
        handlers.add(a);
    }

    public void addEventListener(EventListener.AddTransactionEvent a){

    }

    private void fireEventListeners(Transaction transaction){

    }

    private void fireEventListeners(Block block){
        for(EventListener.AddBlockEvent a : handlers){
            a.run(block);
        }
    }

//////////////////////////////

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    static String bytesToHex(byte[] bytes) {         //converters
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    static byte[] hexStringToByteArray(String s) {
        int length = s.length();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < length; i += 2) {
            BigInteger bigInt;
            try {
                bigInt = new BigInteger(s.substring(i, i + 2), 16);
            } catch (IndexOutOfBoundsException e) {
                bigInt = new BigInteger(s.substring(i, i + 1), 16);
            }
            outputStream.write(bigInt.byteValue());
        }
        return outputStream.toByteArray();
    }


//////////////////////////////
}
