package com.example.clientweb.service.impl;

import com.example.clientweb.entity.Client;
import com.example.clientweb.repository.ClientRepository;
import com.example.clientweb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client saveClient(Client client) {
        if (!client.getIin().matches("^\\d{12}$")) {
            return null;
        }

        if (!client.getDocumentNumber().matches("^\\d{9}$")) {
            return null;
        }

        if (!(isNullOrBlank(client.getFio()) || isNullOrBlank(client.getIin()) ||
                isNullOrBlank(client.getGivenBy()))) {
            return null;
        }
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    private Boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
}
