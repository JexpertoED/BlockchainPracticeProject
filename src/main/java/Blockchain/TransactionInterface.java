package main.java.Blockchain;

public interface TransactionInterface{

    byte[] addArgument(byte[] arg);

    byte[] addArgument(String arg);

    boolean checkSignature();

}
