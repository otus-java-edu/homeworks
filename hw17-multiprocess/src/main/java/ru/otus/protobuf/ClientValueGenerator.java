package ru.otus.protobuf;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.ValueMessage;

import java.util.concurrent.CountDownLatch;

public class ClientValueGenerator implements StreamObserver<ValueMessage> {
    private final CountDownLatch latch;
    private int lastServerValue;
    public ClientValueGenerator(CountDownLatch latch){
        this.latch = latch;
    }
    @Override
    public void onNext(ValueMessage value) {
        synchronized (this){
            lastServerValue = value.getValue();
        }
        System.out.printf("new value: %d%n", value.getValue());
    }

    public int takeValue(){
        synchronized (this) {
            var result = lastServerValue;
            lastServerValue = 0;
            return result;
        }
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t);
    }

    @Override
    public void onCompleted() {
        latch.countDown();
    }
}
