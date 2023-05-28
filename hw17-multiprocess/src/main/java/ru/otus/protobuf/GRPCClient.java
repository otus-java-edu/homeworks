package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.protobuf.generated.ValueRequest;

import java.util.concurrent.CountDownLatch;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var latch = new CountDownLatch(1);
        var newStub = RemoteDBServiceGrpc.newStub(channel);
        var clientValueGenerator = new ClientValueGenerator(latch);
        newStub.getValues(ValueRequest.newBuilder().setFirstValue(0).setLastValue(30).build(), clientValueGenerator);
        var thread = new Thread(() -> {
            var currentValue = 0;
            try{
                for(var i = 0; i < 50; i++){
                    currentValue += 1 + clientValueGenerator.takeValue();
                    System.out.printf("currentValue: %d%n", currentValue);
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e){}
        });
        thread.start();
        latch.await();

        channel.shutdown();
    }
}
