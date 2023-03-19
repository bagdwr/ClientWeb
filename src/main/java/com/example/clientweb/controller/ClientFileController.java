package com.example.clientweb.controller;

import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ClientFileController {
    @Autowired
    private ClientService clientService;

    @GetMapping(value = "/detail-package/{id}")
    public String showFilesPage(@PathVariable(value = "id") Long packageId,
                                Model model) {
        model.addAttribute("files", clientService.getFilesInfo(packageId));
        model.addAttribute("package", clientService.getPackageById(packageId));
        model.addAttribute("files", clientService.getFilesInfo(packageId));
        return "cf-index";
    }

    @GetMapping(value = "/add-new-file/{id}")
    public String showCreateFilePage(@PathVariable(value = "id") Long packageId,
                                     Model model) {
        model.addAttribute("package", clientService.getPackageById(packageId));
        return "cf-create";
    }

    @PostMapping(value = "/add-new-file")
    public String addNewFile(@RequestParam(name = "packageId") Long packageId,
                             @RequestParam(name = "file") MultipartFile file) throws IOException {
        clientService.createFile(packageId, file);
        return "redirect:/detail-package/" + packageId;
    }
}
