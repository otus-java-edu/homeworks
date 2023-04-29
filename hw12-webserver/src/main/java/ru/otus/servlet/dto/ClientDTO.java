package ru.otus.servlet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;

import java.util.List;

@AllArgsConstructor
@Getter
public class ClientDTO {
    private final String name;
    private final String address;
    private final List<String> phones;

    public static ClientDTO fromClient(Client client)
    {
        var address = client.getAddress() == null ? "" : client.getAddress().toString();
        return new ClientDTO(client.getName(), address, client.getPhones().stream().map(p->p.getPhone()).toList());
    }
}
