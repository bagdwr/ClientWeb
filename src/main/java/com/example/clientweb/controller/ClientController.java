package com.example.clientweb.controller;

import com.example.clientweb.entity.Client;
import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/")
    public String showClientList(Model model) {
        model.addAttribute("clients",clientService.getAllClients());
        return "index";
    }

    @GetMapping(value = "/add-new-client")
    public String showClientForm(Model model) {
        return "client-create";
    }

    @PostMapping(value = "/add-new-client")
    public String addNewClient(@RequestBody Client client) {
        clientService.saveClient(client);
        return "redirect:/index";
    }
}
