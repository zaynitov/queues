import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.BlockingQueue;

@Getter
@Setter
@AllArgsConstructor
public class QueueWaitingTime {
    private String name;
    private BlockingQueue<String> queue;
    private Long waitingTime;

    public String toString() {
        return "QueueWaitingTime(name=" + this.getName() + ", queueSize=" + this.getQueue().size() + ", waitingTime=" + this.getWaitingTime() + ")";
    }
}
