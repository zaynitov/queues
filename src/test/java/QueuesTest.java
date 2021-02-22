import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class QueuesTest {

    private final QueuesService queuesService = new QueuesService();
    private QueueWaitingTime queueWaitingTime1;
    private QueueWaitingTime queueWaitingTime2;
    private QueueWaitingTime queueWaitingTime3;
    private QueueWaitingTime queueWaitingTime4;

    @Before
    public void init() {
        queueWaitingTime1 = new QueueWaitingTime("1", new ArrayBlockingQueue<>(20), 10_000L);
        queueWaitingTime2 = new QueueWaitingTime("2", new ArrayBlockingQueue<>(20), 11_000L);
        queueWaitingTime3 = new QueueWaitingTime("3", new ArrayBlockingQueue<>(20), 24_000L);
        queueWaitingTime4 = new QueueWaitingTime("4", new ArrayBlockingQueue<>(20), 56_000L);
    }

    @Test
    public void queueAllEmpty() {
        List<QueueWaitingTime> queueWaitingTimeList = new ArrayList<>();
        queueWaitingTimeList.add(queueWaitingTime1);
        queueWaitingTimeList.add(queueWaitingTime2);
        queueWaitingTimeList.add(queueWaitingTime3);
        queueWaitingTimeList.add(queueWaitingTime4);
        QueueWaitingTime shortQueue = queuesService.findShortestQueue(queueWaitingTimeList);
        Assert.assertEquals("1", shortQueue.getName());
    }

    @Test
    public void queueDifferent() {
        List<QueueWaitingTime> queueWaitingTimeList = new ArrayList<>();
        queueWaitingTimeList.add(fillQueueWithNElements(queueWaitingTime1, 5));//5*10_000=50_000 to wait
        queueWaitingTimeList.add(fillQueueWithNElements(queueWaitingTime2, 5));//5*11_000=55_000 to wait
        queueWaitingTimeList.add(fillQueueWithNElements(queueWaitingTime3, 2));//2*24_000=48_000 to wait
        queueWaitingTimeList.add(fillQueueWithNElements(queueWaitingTime1, 1));//1*56_000=56_000 to wait
        QueueWaitingTime shortQueue = queuesService.findShortestQueue(queueWaitingTimeList);
        Assert.assertEquals("3", shortQueue.getName());
    }

    @Test
    public void twoIsEmpty() {
        List<QueueWaitingTime> queueWaitingTimeList = new ArrayList<>();
        queueWaitingTimeList.add(fillQueueWithNElements(queueWaitingTime1, 5));
        queueWaitingTimeList.add(fillQueueWithNElements(queueWaitingTime2, 5));
        queueWaitingTimeList.add(queueWaitingTime3);
        queueWaitingTimeList.add(queueWaitingTime4);
        QueueWaitingTime shortQueue = queuesService.findShortestQueue(queueWaitingTimeList);
        Assert.assertEquals("3", shortQueue.getName());
    }

    private QueueWaitingTime fillQueueWithNElements(QueueWaitingTime queueWaitingTime, int n) {
        for (int i = 0; i < n; i++) {
            queueWaitingTime.getQueue().offer("A");
        }
        return queueWaitingTime;
    }
}
