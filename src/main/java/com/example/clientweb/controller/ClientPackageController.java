package com.example.clientweb.controller;

import com.example.clientweb.entity.Client;
import com.example.clientweb.pojo.ClientPackageInfo;
import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ClientPackageController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/detail/{id}")
    public String showPackageTable(@PathVariable(name = "id") Long clientId,
                                   Model model) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return "redirect:/";
        }

        String header = "Список папок по клиенту - " + client.getFio();

        model.addAttribute("header", header);
        model.addAttribute("clientId", clientId);
        List<ClientPackageInfo> cps = clientService.getClientPackages(clientId);
        if (cps != null && !cps.isEmpty()) {
            model.addAttribute("cps", cps);
        }

        return "cp-index";
    }

    @GetMapping(value = "/add-new-package/{id}")
    public String showCreatePage(@PathVariable(name = "id") Long clientId, Model model) {
        model.addAttribute("clientId", clientId);
        return "cp-create";
    }

    @PostMapping(value = "/add-new-package")
    public String showCreatePage(@RequestParam(name = "clientId") Long clientId,
                                 @RequestParam(name = "name") String name) {

        clientService.createPackage(clientId, name);
        return "redirect:/detail/" + clientId;
    }

    @PostMapping(value = "/delete-package")
    public String deletePackage(@RequestParam(name = "packageId") Long packageId,
                                @RequestParam(name = "clientId") Long clientId) {
        clientService.deletePackageAndFiles(packageId);
        return "redirect:/detail/" + clientId;
    }

    @GetMapping(value = "/edit-package/{id}")
    public String editPackage(@PathVariable(name = "id") Long packageId,
                              Model model) {
        model.addAttribute("pkg", clientService.getPackageById(packageId));
        return "cp-edit";
    }

    @PostMapping(value = "/edit-package")
    public String editPackage(@RequestParam(name = "clientId") Long clientId,
                              @RequestParam(name = "packageId") Long packageId,
                              @RequestParam(name = "name") String name) {
        clientService.updatePackage(packageId,name);
        return "redirect:/detail/" + clientId;
    }
}
