package main.java.Blockchain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

class ProofOfWork {
    private static final int targetZeroes = 3; //number of zeroes
    private Long nonce = -1L;
    private static final int globalThreads = Runtime.getRuntime().availableProcessors();
    Block block;

    ProofOfWork(Block block) {
        this.block = block;
//        this.target = BigInteger.valueOf(1);
//        this.target.shiftLeft(256 - targetBits);
    }

    private static byte[] prepareData(byte[] tempHash,long nonce) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(tempHash);
        output.write(Blockchain.hexStringToByteArray(Long.toHexString(nonce)));
        return output.toByteArray();
    }

    private static byte[] prepareData(Block block, long nonce) throws IOException {
        byte[] tempHash = block.calculateHash();
        return prepareData(tempHash,nonce);
    }


    void findProof() throws InterruptedException, IOException, NoSuchAlgorithmException {
        startOptimal();
        this.block.setNonce(nonce);
        this.block.setHash(SHA256.sha256(prepareData(this.block, nonce)));
    }


    private void startOptimal() throws InterruptedException {
        Thread a = null;
        StringBuilder comp = new StringBuilder();
        for (int i = 0; i < targetZeroes; i++) {
            comp.append("0");
        }
        String str = comp.toString();
        for (int i = 0; i < globalThreads * 5; i++) {
            int x = i;
            a = new Thread(new Runnable() {
                final long threadNumber = x;

                @Override
                public void run() {
                    String hash = null;
                    byte[] tempHash = block.calculateHash();
                    for (long i = threadNumber; i < Long.MAX_VALUE; i += globalThreads * 5) {
                        byte[] data = new byte[0];
                        try {
                            data = prepareData(tempHash,i);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            hash = Blockchain.bytesToHex(SHA256.sha256(data));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        if (nonce > -1)
                            break;
                        if (hash.startsWith(str)) {
                            synchronized (nonce) {
                                nonce = i;
                            }
                            break;
                        }
                    }
                }
            });
            a.start();
        }
        assert a != null;
        a.join();
    }

    static boolean Validate(Block b, long nonce) throws IOException, NoSuchAlgorithmException {
        byte[] data = prepareData(b,nonce);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < targetZeroes; i++) {
            stringBuilder.append(0);
        }
        return Blockchain.bytesToHex(SHA256.sha256(data)).startsWith(stringBuilder.toString());
    }
}

