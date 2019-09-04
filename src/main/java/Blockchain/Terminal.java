package main.java.Blockchain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class Terminal {
    private static byte[] temp = new byte[0];
    //TODO Transactions, Signature, Merkly tree for blocks, sign blocks, nodes (optional), unverified transaction pool
    public static void main(String[] args) throws Exception {
        ArrayList<Transaction> genesis = new ArrayList<>();
        genesis.add(new Transaction(new byte[0],new byte[0],new byte[0],true, SHA256.sha256(Blockchain.hexStringToByteArray("3059301306072a8648ce3d020106082a8648ce3d0301070342000428932955f5f9dee2ffed021165ac06d26c8a6bd688a179053b979e1aa72d74745406f302ee42ad8370f355c2f545cde40827ec65d0682f1ddaef7a3355087edd"))));
        Blockchain bc = new Blockchain(genesis);

        for (Block block : bc.getBlocks()){
            logBlock(block);
        }

        bc.addEventListener(Terminal::logBlock);

        bc.run();
        //bc.connectionTest();
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        SimpleGUI gui = new SimpleGUI(bc);
        gui.setVisible(true);

    }


    public static void logBlock (Block block){
        System.out.println("Prev. hash: " + Blockchain.bytesToHex(block.getPreviousHash()));
        System.out.println();
        System.out.println("Transaction Hash: " + Blockchain.bytesToHex(block.getData().get(0).getHash()));
        System.out.println("prevHash: " + Blockchain.bytesToHex(block.getData().get(0).getInput().prevHash));
        System.out.println("scriptSig: " + Blockchain.bytesToHex(block.getData().get(0).getInput().scriptSig));
        System.out.println("\tpubKey: " + Blockchain.bytesToHex(Transaction.getPubKeyFromScriptSig(block.getData().get(0).getInput().scriptSig)));
        System.out.println("\tSignature: " + Blockchain.bytesToHex(Transaction.getSignatureFromScriptSig(block.getData().get(0).getInput().scriptSig)));
        System.out.println("addressPubKeyHash: " + Blockchain.bytesToHex(block.getData().get(0).getOutput().pubKeyHash));
        System.out.println();
        System.out.println("Hash: " + Blockchain.bytesToHex(block.getHash()));
        try {
            System.out.println("Is valid: " + ProofOfWork.Validate(block, block.getNonce()));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Integral: " + Blockchain.bytesToHex(temp).equals(Blockchain.bytesToHex(block.getPreviousHash())));
        } catch (Exception ignored) {
        }
        temp = block.getHash();
        System.out.println();
    }
}
