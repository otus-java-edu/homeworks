package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.servlet.dto.ClientDTO;

import java.io.IOException;
import java.util.stream.Collectors;


public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var id = extractIdFromRequest(request);
        String result;
        if (id < 0)
            result = gson.toJson(dbServiceClient.findAll().stream().map(ClientDTO::fromClient).toList());
        else
            result = gson.toJson(dbServiceClient.getClient(id).orElse(null));
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var newClient = gson.fromJson(request.getReader(), ClientDTO.class);
        var client = dbServiceClient.saveClient(new Client(null, newClient.name,
                new Address(null, newClient.address),
                newClient.phones.stream().map(p->new Phone(null, p)).toList()));
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        var result = gson.toJson(client.toString());
        out.print(result);
    }
    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
