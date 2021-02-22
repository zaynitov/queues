import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

public class QueuesService {
    private final static Integer QUEUE_1_CUSTOMER_PER_HOUR = 10;
    private final static Integer QUEUE_2_CUSTOMER_PER_HOUR = 13;
    private final static Integer QUEUE_3_CUSTOMER_PER_HOUR = 15;
    private final static Integer QUEUE_4_CUSTOMER_PER_HOUR = 17;
    private final static Integer QUEUE_TOTAL_CAPACITY = 20;
    private List<QueueWaitingTime> queues;

    public QueuesService() {
        queues = new ArrayList<>();
        queues.add(new QueueWaitingTime("firstQueue", new ArrayBlockingQueue<>(QUEUE_TOTAL_CAPACITY), customerPerHourToServeTimeOneCustomer(QUEUE_1_CUSTOMER_PER_HOUR)));
        queues.add(new QueueWaitingTime("secondQueue", new ArrayBlockingQueue<>(QUEUE_TOTAL_CAPACITY), customerPerHourToServeTimeOneCustomer(QUEUE_2_CUSTOMER_PER_HOUR)));
        queues.add(new QueueWaitingTime("thirdQueue", new ArrayBlockingQueue<>(QUEUE_TOTAL_CAPACITY), customerPerHourToServeTimeOneCustomer(QUEUE_3_CUSTOMER_PER_HOUR)));
        queues.add(new QueueWaitingTime("fourthQueue", new ArrayBlockingQueue<>(QUEUE_TOTAL_CAPACITY), customerPerHourToServeTimeOneCustomer(QUEUE_4_CUSTOMER_PER_HOUR)));
    }

    public void serve() {
        queues.forEach(queue -> {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            synchronized (this) {
                                queue.getQueue().take();
                            }
                            Thread.sleep(queue.getWaitingTime());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        });
    }

    public synchronized void insertCustomer() {
        QueueWaitingTime shortestQueue = findShortestQueue(queues);
        try {
            shortestQueue.getQueue().put("New");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        queues.forEach(System.out::println);
        System.out.println();
    }

    public QueueWaitingTime findShortestQueue(List<QueueWaitingTime> queues) {
        Long shortestTime = Long.MAX_VALUE;
        boolean allQueuesAreFull = queues.stream()
                .allMatch(queueWaitingTime -> queueWaitingTime.getQueue().size() == QUEUE_TOTAL_CAPACITY);
        if (allQueuesAreFull) {
            return queues.stream()
                    .min(Comparator.comparing(QueueWaitingTime::getWaitingTime))
                    .orElse(null);
        }
        List<QueueWaitingTime> emptyQueues = queues.stream().filter(queue -> queue.getQueue().size() == 0).collect(Collectors.toList());
        if (emptyQueues.size() > 1) {
            return emptyQueues.stream()
                    .min(Comparator.comparing(QueueWaitingTime::getWaitingTime))
                    .get();
        }
        QueueWaitingTime shortestQueue = null;
        for (QueueWaitingTime queue : queues) {
            long waitTotal = queue.getQueue().size() * queue.getWaitingTime();
            if ((queue.getQueue().size() != QUEUE_TOTAL_CAPACITY) && (waitTotal < shortestTime)) {
                shortestTime = waitTotal;
                shortestQueue = queue;
            }
        }
        return shortestQueue;
    }

    private Long customerPerHourToServeTimeOneCustomer(Integer customerPerHour) {
        return (60 / customerPerHour) * 60 * 1000L;
    }
}
