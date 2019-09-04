package main.java.Blockchain;

public class EventListener {

    public interface AddBlockEvent {
        void run(Block bc);
    }

    public interface AddTransactionEvent {
        void run(Transaction transaction);
    }
}

