package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.protobuf.generated.ValueMessage;
import ru.otus.protobuf.generated.ValueRequest;

public class RemoteValueGeneratorServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    private final RealValuesGeneratorService realValuesGeneratorService;

    public RemoteValueGeneratorServiceImpl(RealValuesGeneratorService realValuesGeneratorService) {
        this.realValuesGeneratorService = realValuesGeneratorService;
    }

    @Override
    public void getValues(ValueRequest request, StreamObserver<ValueMessage> responseObserver) {
        realValuesGeneratorService.init(request.getFirstValue(), request.getLastValue());
        try{
            while(!realValuesGeneratorService.isFinished()){
                var value = realValuesGeneratorService.getNext();
                responseObserver.onNext(ValueMessage.newBuilder().setValue(value).build());
                Thread.sleep(2000);
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        responseObserver.onCompleted();
    }
}
