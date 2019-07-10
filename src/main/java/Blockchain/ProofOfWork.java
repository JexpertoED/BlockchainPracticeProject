package main.java.Blockchain;

class ProofOfWork {
    private static final int targetZeroes = 10; //number of zeroes * 4
    private Long nonce = -1L;
    private static final int globalThreads = Runtime.getRuntime().availableProcessors();
    Block block;

    ProofOfWork(Block block) {
        this.block = block;
//        this.target = BigInteger.valueOf(1);
//        this.target.shiftLeft(256 - targetBits);
    }

    private String prepareData(long nonce) {
        return block.getPreviousHash() + block.getData() + block.getTimeStamp() + Integer.toHexString(targetZeroes) + Long.toHexString(nonce);
    }

    private static String prepareData(Block block, long nonce) {
        return block.getPreviousHash() + block.getData() + block.getTimeStamp() + Integer.toHexString(targetZeroes) + Long.toHexString(nonce);
    }


    void findProof() throws InterruptedException {
        startOptimal();
        this.block.setNonce(nonce);
        this.block.setHash(SHA256.sha256(prepareData(nonce)));
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
                    String hash;
                    for (long i = threadNumber; i < Long.MAX_VALUE; i += globalThreads * 5) {
                        String data = prepareData(i);
                        hash = SHA256.sha256(data);
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

    static boolean Validate(Block b, long nonce) {
        String data = prepareData(b,nonce);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < targetZeroes; i++) {
            stringBuilder.append(0);
        }
        return SHA256.sha256(data).startsWith(stringBuilder.toString());
    }
}

