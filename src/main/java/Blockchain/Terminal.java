package main.java.Blockchain;

import javafx.application.Application;
import javafx.stage.Stage;

public class Terminal extends Application {

    //TODO Transactions, Signature, Merkly tree for blocks, sign blocks, nodes (optional), unverified transaction pool
    public static void main(String[] args) throws Exception {
        Blockchain bc = new Blockchain();
        bc.run();
        bc.connectionTest();
        Thread.sleep(100);
        //  UserInterface ui = new UserInterface("stage.fxml","Election");
        Thread t = null;
        Thread finalT = t;
        t = new Thread(() -> {while (true){
            System.out.println("I'm still alive");
            try {
                finalT.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }});
        launch(args);

        for (Block block : bc.getBlocks()) {
            System.out.println("Prev. hash: " + block.getPreviousHash());
            System.out.println("Data: " + block.getData());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Is valid: " + ProofOfWork.Validate(block, block.getNonce()));
            System.out.println();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UserInterface ui = new UserInterface(primaryStage, "Election", "resource/stage.fxml");
        ui.stage.show();
    }
}
