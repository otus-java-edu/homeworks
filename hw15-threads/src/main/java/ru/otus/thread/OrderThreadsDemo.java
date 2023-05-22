package ru.otus.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderThreadsDemo {

    private final AtomicInteger syncer = new AtomicInteger();
    private static final Logger logger = LoggerFactory.getLogger(OrderThreadsDemo.class);

    public static void main(String[] args){
        new OrderThreadsDemo().run();
    }


    private Thread createThread(String name, int val, int next) {

        var thread = new Thread(() -> action(val, next));
        thread.setName(name);
        return thread;
    }

    private void run(){
        logger.info("starting");

        var t1 = createThread("First thread", 0, 1);
        var t2 = createThread("Second thread", 1, 0);

        t1.start();
        t2.start();
    }

    private void action(int value, int next) {
        try {
            var i = 1;
            var increment = 1;
            while(Thread.currentThread().isInterrupted()){
                int lockValue = -1;
                if (syncer.compareAndSet(value, lockValue)) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    i+= increment;
                    if (i % 9 == 1)
                        increment *= -1;
                    syncer.set(next);
                }
                else {
                    Thread.sleep(400);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
