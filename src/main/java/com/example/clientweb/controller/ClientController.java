package com.example.clientweb.controller;

import com.example.clientweb.entity.Client;
import com.example.clientweb.entity.ClientFile;
import com.example.clientweb.pojo.ClientReport;
import com.example.clientweb.report.ReportExcel;
import com.example.clientweb.service.ClientService;
import com.example.clientweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.Date;
import java.util.List;

@Controller
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ReportService reportService;

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

    @GetMapping(value = "/report")
    public String showReport(Model model) {
        List<ClientReport> clientReports = reportService.getAllReport();
        if (clientReports == null) {
            return "redirect:/";
        }
        Integer packageAmount = clientReports.stream()
                .mapToInt(ClientReport::getPackageAmount)
                .sum();
        Integer fileAmount = clientReports.stream()
                .mapToInt(ClientReport::getFilesAmount)
                .sum();
        model.addAttribute("clientReport", clientReports);
        model.addAttribute("packageAmount", packageAmount);
        model.addAttribute("fileAmount", fileAmount);

        return "report";
    }

    @GetMapping(value = "/download-report")
    public void getReport(HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition",
                "attachment; filename=client_report_" + new java.util.Date().getTime() + ".xlsx");

        OutputStream outputStream = response.getOutputStream();
        reportService.generateReport(new ReportExcel(outputStream));
        outputStream.flush();
    }
}
