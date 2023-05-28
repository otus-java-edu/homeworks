package ru.otus.protobuf;


import io.grpc.ServerBuilder;
import ru.otus.protobuf.service.RealValueGeneratorServiceImpl;
import ru.otus.protobuf.service.RemoteValueGeneratorServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var dbService = new RealValueGeneratorServiceImpl();
        var remoteDBService = new RemoteValueGeneratorServiceImpl(dbService);

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(remoteDBService).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
