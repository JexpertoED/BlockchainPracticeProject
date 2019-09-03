package main.java.Blockchain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.jws.soap.SOAPBinding;
import javax.swing.*;

public class Terminal extends Application {

    //TODO Transactions, Signature, Merkly tree for blocks, sign blocks, nodes (optional), unverified transaction pool
    public static void main(String[] args) throws Exception {
        Blockchain bc = new Blockchain();
        bc.run();
        //bc.connectionTest();
        Thread.sleep(100);
        //  UserInterface ui = new UserInterface("stage.fxml","Election");
        Thread t = null;
        //System.out.println(Thread.currentThread());
        t = new Thread(() -> {
            while (true) {
                //System.out.println("I'm still alive");
                try {
                    Thread.sleep(10);
                    System.out.println(Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        //t.start();
        //launch(args);
        UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        SimpleGUI gui = new SimpleGUI(bc);
        gui.setVisible(true);
        int gb = 0;
        byte[] temp = new byte[0];
        while (true){
            if (bc.getBlocks().size() > gb){
                int gbOld = gb;
                gb = bc.getBlocks().size();
                int i = 0;
        for (Block block : bc.getBlocks()) {
            i++;
            if (i > gbOld){
            System.out.println("Prev. hash: " + Blockchain.bytesToHex(block.getPreviousHash()));
            System.out.println("Fst transaction:");
            System.out.println("Hash: " + Blockchain.bytesToHex(block.getData().get(0).getHash()));
            System.out.println("prevHash: " + Blockchain.bytesToHex(block.getData().get(0).getInput().prevHash));
            System.out.println("scriptSig: " + Blockchain.bytesToHex(block.getData().get(0).getInput().scriptSig));
            System.out.println("pubKeyHash: " + Blockchain.bytesToHex(block.getData().get(0).getOutput().pubKeyHash));
            System.out.println();
            System.out.println("Hash: " + Blockchain.bytesToHex(block.getHash()));
            System.out.println("Is valid: " + ProofOfWork.Validate(block, block.getNonce()));
            try {
                System.out.println("Integral: " + Blockchain.bytesToHex(temp).equals(Blockchain.bytesToHex(block.getPreviousHash())));
            } catch (Exception e) {
            }
            temp = block.getHash();
            System.out.println();
        }}}}


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resource/stage.fxml"));
        UserInterface ui = new UserInterface(primaryStage,"Симуляционная сеть",root);
        ui.stage.show();
        Button button = (Button) root.lookup("#okButtorn");
        button.setDisable(true);
        System.out.println("kek");
    }
}
