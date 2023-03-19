package com.example.clientweb.service;

import com.example.clientweb.entity.Client;
import com.example.clientweb.entity.ClientPackage;
import com.example.clientweb.pojo.ClientPackageInfo;

import java.util.List;

public interface ClientService {

    Client saveClient(Client client);

    List<Client> getAllClients();

    void deleteClient(Long id);

    Client getClientById(Long id);

    void updateClient(Client client);

    List<ClientPackageInfo> getClientPackages(Long clientId);

    void createPackage(Long clientId, String name);

    void  deleteAllPackageAndFiles(Long clientId);

    void  deletePackageAndFiles(Long packageId);

    ClientPackage getPackageById(Long packageId);

    void updatePackage(Long packageId, String name);
}
