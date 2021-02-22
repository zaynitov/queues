import lombok.SneakyThrows;

public class Main {
    public static void main(String[] args) {
        QueuesService queuesService = new QueuesService();
        queuesService.serve();
        Thread thread1 = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    queuesService.insertCustomer();
                    Thread.sleep(100);
                }
            }
        };
        Thread thread2 = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    queuesService.insertCustomer();
                    Thread.sleep(100);
                }
            }
        };
        thread1.start();
        thread2.start();

    }
}
