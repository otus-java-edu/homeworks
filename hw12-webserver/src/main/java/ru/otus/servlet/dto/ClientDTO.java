package ru.otus.servlet.dto;

import lombok.AllArgsConstructor;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;

import java.util.List;

@AllArgsConstructor
public class ClientDTO {
    public String name;
    public String address;
    public List<String> phones;

    public static ClientDTO fromClient(Client client)
    {
        var address = client.getAddress() == null ? "" : client.getAddress().toString();
        return new ClientDTO(client.getName(), address, client.getPhones().stream().map(p->p.getPhone()).toList());
    }
}
