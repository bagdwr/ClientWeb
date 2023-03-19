package com.example.clientweb.service.impl;

import com.example.clientweb.entity.Client;
import com.example.clientweb.repository.ClientRepository;
import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client saveClient(Client client) {
//        if (!client.getIin().matches("^\\d{12}$")) {
//            return null;
//        }
//
//        if (!client.getDocumentNumber().matches("^\\d{9}$")) {
//            return null;
//        }
//
//        if (!(isNullOrBlank(client.getFio()) || isNullOrBlank(client.getIin()) ||
//                isNullOrBlank(client.getGivenBy()))) {
//            return null;
//        }
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findClientByActualTrue();
    }

    @Override
    public void deleteClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return;
        }
        client.get().setActual(false);
        clientRepository.save(client.get());
    }

    @Override
    public Client getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return null;
        }
        return client.get();
    }

    private Boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }

    @Override
    public void updateClient(Client client) {
//        if (!client.getIin().matches("^\\d{12}$")) {
//            return;
//        }
//
//        if (!client.getDocumentNumber().matches("^\\d{9}$")) {
//            return;
//        }
//
//        if (!(isNullOrBlank(client.getFio()) || isNullOrBlank(client.getIin()) ||
//                isNullOrBlank(client.getGivenBy()))) {
//            return;
//        }

        Optional<Client> clientDb = clientRepository.findById(client.getId());
        if (clientDb.isEmpty()) {
            return;
        }

        Client c = clientDb.get();
        c.setIin(client.getIin());
        c.setFio(client.getFio());
        c.setGivenBy(client.getGivenBy());
        c.setGivenDate(client.getGivenDate());
        c.setExpirationDate(client.getExpirationDate());
        c.setDocumentNumber(client.getDocumentNumber());
        clientRepository.save(c);
    }
}
