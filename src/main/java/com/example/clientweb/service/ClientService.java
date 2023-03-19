package com.example.clientweb.service;

import com.example.clientweb.entity.Client;

import java.util.List;

public interface ClientService {

    Client saveClient(Client client);

    List<Client> getAllClients();

    void deleteClient(Long id);

    Client getClientById(Long id);

    void updateClient(Client client);
}
