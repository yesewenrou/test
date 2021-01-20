package net.cdsunrise.hy.lyyt.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.ws.Holder;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * RunHelper
 * 运行辅助类，目前只是简单实现，后期可以考虑双端队列实现优先级
 * @author LiuYin
 * @date 2020/1/19 8:05
 */
@Slf4j
@Component
public class RunHelper {

    private static final BlockingQueue<MessagedRunnable> RUN_QUEUE = new LinkedBlockingQueue<>();

    private static final Holder<Boolean> CONSUMER_QUEUE_HOLDER = new Holder<>(Boolean.TRUE);

    /**
     * 最大队列长度
     */
    private static final int MAX_QUEUE_SIZE = 50;

    private final TaskExecutor taskExecutor;

    public RunHelper(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @PostConstruct
    public void init(){
        taskExecutor.execute(() -> {
            log.info("queue task is begin");
            while (CONSUMER_QUEUE_HOLDER.value && !Thread.currentThread().isInterrupted()){
                try {
                    if(Objects.nonNull(getRunQueue())){
                        final MessagedRunnable messagedRunnable = getRunQueue().take();
                        messagedRunnable.run();
                    }
                } catch (Exception e) {
                    log.error("run from queue error, cause",e);
                }
            }
        });
    }


    @PreDestroy
    public void preDestroy(){
        CONSUMER_QUEUE_HOLDER.value = Boolean.FALSE;
        getRunQueue().forEach(r -> {
            if(Objects.nonNull(r)){
                log.warn("pre destroy run [{}]", r.getMessage());
                taskExecutor.execute(r);
            }
        });
    }


    /**
     * 加入一个可运行的方法， 附带消息
     * @param runnable 运行
     * @param message 消息
     */
    public static void add (Runnable runnable, String message){
        Objects.requireNonNull(runnable, "runnable is null");
        Objects.requireNonNull(message, "message is null");

        if (isFull()) {
            printErrorOnFull();
        } else {
            addMessagedRunnable(MessagedRunnable.create(runnable, message));
        }

    }

    private static void addMessagedRunnable(MessagedRunnable messagedRunnable){
        getRunQueue().add(messagedRunnable);
    }

    private static boolean isFull(){
        return getRunQueue().size() > MAX_QUEUE_SIZE;
    }

    private static void printErrorOnFull() {
        StringBuilder stringBuilder = new StringBuilder();
        getRunQueue().forEach(r -> stringBuilder.append(r.getMessage()).append(" "));
        log.error("queue is full, task list is [{}], add method failed", stringBuilder.toString());
    }

    /**
     * 用try catch 来运行
     * @param runnable 运行方法
     * @param runMessage 消息
     */
    private static void tryRun(Runnable runnable, String runMessage){
        if(Objects.isNull(runnable)){
            log.error("runnable is null");
        }
        try {
            final long begin = System.currentTimeMillis();
            log.info("[{}] begin at [{}], [{}]", runMessage, begin, DateUtil.convert(begin));
            runnable.run();
            final long end = System.currentTimeMillis();
            log.info("[{}] end at [{}], [{}], duration [{}] ms ", runMessage, end, DateUtil.convert(end), end - begin);
        }catch (Exception e){
            log.error("{} run error, cause ",runMessage, e);
        }
    }



    /**
     * 包装了信息的运行
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MessagedRunnable implements Runnable{

        /** 运行的方法*/
        private Runnable runnable;
        /** 信息*/
        private String message;

        public static MessagedRunnable create(Runnable runnable, String message){
            return new MessagedRunnable(runnable, message);
        }

        @Override
        public void run() {
            tryRun(runnable, message);
        }
    }

    private static BlockingQueue<MessagedRunnable> getRunQueue(){
        return RUN_QUEUE;
    }


}
