package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

@Controller
public class ClientController {

    private final String osData;
    private final String applicationYmlMessage;
    private final DBServiceClient clientService;

    public ClientController(@Value("${app.client-list-page.msg:Тут может находиться ваша реклама}")
                                    String applicationYmlMessage,
                            @Value("OS: #{T(System).getProperty(\"os.name\")}, " +
                                    "JDK: #{T(System).getProperty(\"java.runtime.version\")}")
                                    String osData,
                            DBServiceClient clientService) {
        this.applicationYmlMessage = applicationYmlMessage;
        this.osData = osData;
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        var clients = clientService.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("osData", osData);
        model.addAttribute("applicationYmlMessage", applicationYmlMessage);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client(null, ""));
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return new RedirectView("/", true);
    }

}
