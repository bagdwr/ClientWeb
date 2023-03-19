package com.example.clientweb.controller;

import com.example.clientweb.entity.Client;
import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/")
    public String showClientList(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "index";
    }

    @GetMapping(value = "/add-new-client")
    public String showClientForm(Model model) {
        return "client-create";
    }

    @GetMapping(value = "/edit-client/{id}")
    public String showEditClientForm(@PathVariable(name = "id") Long id,
                                     Model model) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            return "redirect:/";
        }
        model.addAttribute("client", client);
        return "edit-client";
    }

    @PostMapping(value = "/edit-client")
    public String updateClient(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "fio") String fio,
                               @RequestParam(name = "iin") String iin,
                               @RequestParam(name = "documentNumber") String documentNumber,
                               @RequestParam(name = "givenBy") String givenBy,
                               @RequestParam(name = "givenDate") Date givenDate,
                               @RequestParam(name = "expirationDate") Date expirationDate) {
        Client client = new Client();
        client.setId(id);
        client.setFio(fio);
        client.setIin(iin);
        client.setGivenDate(givenDate);
        client.setGivenBy(givenBy);
        client.setExpirationDate(expirationDate);
        client.setDocumentNumber(documentNumber);
        clientService.updateClient(client);
        return "redirect:/";
    }

    @PostMapping(value = "/add-new-client")
    public String addNewClient(@RequestParam(name = "fio") String fio,
                               @RequestParam(name = "iin") String iin,
                               @RequestParam(name = "documentNumber") String documentNumber,
                               @RequestParam(name = "givenBy") String givenBy,
                               @RequestParam(name = "givenDate") Date givenDate,
                               @RequestParam(name = "expirationDate") Date expirationDate
    ) {
        Client client = new Client();
        client.setFio(fio);
        client.setIin(iin);
        client.setGivenDate(givenDate);
        client.setGivenBy(givenBy);
        client.setExpirationDate(expirationDate);
        client.setDocumentNumber(documentNumber);
        clientService.saveClient(client);
        return "redirect:/";
    }

    @PostMapping(value = "/delete-by-id")
    public String deleteById(@RequestParam(name = "id") Long id) {
        clientService.deleteClient(id);
        return "redirect:/";
    }
}
