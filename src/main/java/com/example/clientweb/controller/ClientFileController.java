package com.example.clientweb.controller;

import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClientFileController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/detail-package/{id}")
    public String showFilesPage(@PathVariable(value = "id") Long packageId,
                                Model model) {

        return "cl-index";
    }
}
