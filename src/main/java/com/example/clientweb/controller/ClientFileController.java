package com.example.clientweb.controller;

import com.example.clientweb.entity.ClientFile;
import com.example.clientweb.pojo.ClientFileInfo;
import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/delete-file-by-id")
    public String deleteFileById(@RequestParam(name = "packageId") Long packageId,
                                 @RequestParam(name = "fileId") Long fileId) {
        clientService.deleteFileById(fileId);
        return "redirect:/detail-package/" + packageId;
    }

    @GetMapping(value = "/download-file/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable(name = "id") Long id) {
        ClientFile file = clientService.getFileById(id);
        if (file == null) {
            return ResponseEntity.notFound()
                    .build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.valueOf(file.getType()))
                .body(file.getData());
    }

    @GetMapping(value = "/edit-file/{id}")
    public String showEditPage(@PathVariable(name = "id") Long id,
                               Model model) {
        ClientFileInfo file = clientService.getFileInfoById(id);
        if (file == null) {
            return "redirect:/";
        }
        model.addAttribute("file", file);
        return "cf-edit";
    }

    @PostMapping(value = "/edit-file")
    public String editFile(@RequestParam(name = "file") MultipartFile file,
                           @RequestParam(name = "fileId") Long fileId,
                           @RequestParam(name = "packageId") Long packageId) throws IOException {

        clientService.updateFile(file,fileId);
        return "redirect:/detail-package/" + packageId;
    }
}
