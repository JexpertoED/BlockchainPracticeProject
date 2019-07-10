package main.java.Blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> blocks;

    List<Block> getBlocks() {
        return blocks;
    }

    public Blockchain(List<Block> blocks) {
        this.blocks = blocks;
    }

    Blockchain() {
        List<Block> blocks = new ArrayList<>(1);
        blocks.add(newGenesisBlock());
        this.blocks = blocks;
    }

    void addBlock(String data) {
        Block prevBlock = this.blocks.get(this.blocks.size() - 1);
        Block newBlock = Block.generateBlock(data, prevBlock.getHash());
        this.blocks.add(newBlock);
    }

    private Block newGenesisBlock() {
        return Block.generateBlock("Genesis Block", "");
    }

//////////////////////////////

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {         //converters
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

//////////////////////////////
}
