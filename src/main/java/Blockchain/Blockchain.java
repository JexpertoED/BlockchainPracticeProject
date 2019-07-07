package main.java.Blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> blocks;

    public List<Block> getBlocks() {
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

    public void addBlock(String data) {
        Block prevBlock = this.blocks.get(this.blocks.size() - 1);
        Block newBlock = Block.generateBlock(data, prevBlock.getHash());
        this.blocks.add(newBlock);
    }

    private Block newGenesisBlock() {
        return Block.generateBlock("Genesis Block", "");
    }


}
