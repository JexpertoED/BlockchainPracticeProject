package main.java.Blockchain;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

class UserInterface {
    public Stage stage;

    //    UserInterface(String fxml, String title) {
//        Parent root;
//        try {
//            System.out.println("/resource/" + fxml);
//            root = FXMLLoader.load(UserInterface.class.getResource("stage.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle(title);
//            stage.setScene(new Scene(root));
//            this.stage = stage;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    UserInterface(Stage primaryStage, String title, String fxml) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        this.stage = primaryStage;
    }


//    public void addTransactionToPool(Transaction transaction) {
//        unverifiedTransactionPool.add(transaction);
//    }

}
