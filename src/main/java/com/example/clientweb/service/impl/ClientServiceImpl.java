package com.example.clientweb.service.impl;

import com.example.clientweb.db.DbManager;
import com.example.clientweb.entity.Client;
import com.example.clientweb.entity.ClientFile;
import com.example.clientweb.entity.ClientPackage;
import com.example.clientweb.pojo.ClientFileInfo;
import com.example.clientweb.pojo.ClientPackageInfo;
import com.example.clientweb.repository.ClientFileRepository;
import com.example.clientweb.repository.ClientPackageRepository;
import com.example.clientweb.repository.ClientRepository;
import com.example.clientweb.service.ClientService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientPackageRepository clientPackageRepository;

    @Autowired
    private ClientFileRepository clientFileRepository;

    @Autowired
    private DbManager dbManager;

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

        deleteAllPackageAndFiles(client.get().getId());
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

    @Override
    public List<ClientPackageInfo> getClientPackages(Long clientId) {
        List<ClientPackage> cps = clientPackageRepository.findByClientIdAndActualTrue(clientId);
        if (cps == null) {
            return null;
        }

        List<ClientPackageInfo> packages = new ArrayList<>();
        for (ClientPackage cp : cps) {
            ClientPackageInfo packageInfo = new ClientPackageInfo(cp.getId(), clientId, cp.getName(), cp.getCreated_at());

            List<ClientFile> cfs = clientFileRepository.findByClientPackageIdAndActualTrue(cp.getId());
            packageInfo.filesAmount = cfs.size();

            for (ClientFile cf : cfs) {
                byte[] byteArray = cf.getData();
                if (byteArray == null) {
                    continue;
                }
                int sizeInBytes = byteArray.length;
                double sizeInMb = (double) sizeInBytes / 1048576;
                packageInfo.size = packageInfo.size + sizeInMb;
            }
            packages.add(packageInfo);
        }

        return packages;
    }

    @Override
    public void createPackage(Long clientId, String name) {
        if (isNullOrBlank(name)) {
            return;
        }

        Client client = getClientById(clientId);
        if (client == null) {
            return;
        }

        ClientPackage clientPackage = new ClientPackage();
        clientPackage.setClient(client);
        clientPackage.setName(name);
        clientPackageRepository.save(clientPackage);
    }

    @Override
    public void deleteAllPackageAndFiles(Long clientId) {
        if (clientId == null) {
            return;
        }

        List<ClientPackage> cps = clientPackageRepository.findByClientIdAndActualTrue(clientId);
        for (ClientPackage cp : cps) {
            List<ClientFile> cfs = clientFileRepository.findByClientPackageIdAndActualTrue(cp.getId());
            for (ClientFile cf : cfs) {
                cf.setActual(false);
                clientFileRepository.save(cf);
            }

            cp.setActual(false);
            clientPackageRepository.save(cp);
        }
    }

    @Override
    public void deletePackageAndFiles(Long packageId) {
        if (packageId == null) {
            return;
        }

        ClientPackage cp = clientPackageRepository.findByIdAndActualTrue(packageId);
        List<ClientFile> cfs = clientFileRepository.findByClientPackageIdAndActualTrue(cp.getId());
        for (ClientFile cf : cfs) {
            cf.setActual(false);
            clientFileRepository.save(cf);
        }

        cp.setActual(false);
        clientPackageRepository.save(cp);
    }

    @Override
    public ClientPackage getPackageById(Long packageId) {
        return clientPackageRepository.findByIdAndActualTrue(packageId);
    }

    @Override
    public void updatePackage(Long packageId, String name) {
        ClientPackage clientPackage = getPackageById(packageId);
        if (clientPackage == null) {
            return;
        }

        clientPackage.setName(name);
        clientPackageRepository.save(clientPackage);
    }

    @Override
    public List<ClientFileInfo> getFilesInfo(Long packageId) {
        ClientPackage clientPackage = getPackageById(packageId);
        if (clientPackage == null) {
            return null;
        }

        List<ClientFile> clientFiles = clientFileRepository.findByClientPackageIdAndActualTrue(packageId);
        List<ClientFileInfo> fileInfoList = new ArrayList<>();
        for (ClientFile cf : clientFiles) {
            ClientFileInfo clientFileInfo = new ClientFileInfo(cf.getId(), cf.getName(), cf.getType());
            fileInfoList.add(clientFileInfo);
        }

        return fileInfoList;
    }

    @Override
    public void createFile(Long packageId, MultipartFile file) throws IOException {
        ClientPackage clientPackage = getPackageById(packageId);
        if (clientPackage == null) {
            return;
        }

        ClientFile clientFile = new ClientFile();
        clientFile.setClientPackage(clientPackage);
        clientFile.setName(file.getName());
        clientFile.setType(file.getContentType());
        clientFile.setData(file.getBytes());

        try (Connection connection = dbManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO public.client_file(name, type,data, client_package_id) values (?,?,?,?);")) {
                preparedStatement.setString(1, clientFile.getName());
                preparedStatement.setString(2, clientFile.getType());
                preparedStatement.setBlob(3, BlobProxy.generateProxy(clientFile.getData()));
                preparedStatement.setLong(4, clientFile.getClientPackage().getId());
                preparedStatement.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        clientFileRepository.save(clientFile);
    }
}
