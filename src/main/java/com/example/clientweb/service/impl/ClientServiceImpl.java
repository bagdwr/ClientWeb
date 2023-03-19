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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
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

    @Transactional
    @Override
    public void deleteClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return;
        }
        deleteAllPackageAndFiles(client.get().getId());

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

    @Transactional
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
                packageInfo.size = Math.round((packageInfo.size + sizeInMb) * 1000.0) / 1000.0;
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

    @Transactional
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

    @Transactional
    @Override
    public List<ClientFileInfo> getFilesInfo(Long packageId) {
        ClientPackage clientPackage = getPackageById(packageId);
        if (clientPackage == null) {
            return null;
        }

        List<ClientFile> clientFiles = clientFileRepository.findByClientPackageIdAndActualTrue(packageId);
        List<ClientFileInfo> fileInfoList = new ArrayList<>();
        for (ClientFile cf : clientFiles) {
            int sizeInBytes = cf.getData().length;
            double sizeInMb = Math.round(((double) sizeInBytes / 1048576) * 1000.0) / 1000.0;
            ClientFileInfo clientFileInfo = new ClientFileInfo(cf.getId(), cf.getName(), cf.getType(), cf.getClientPackage().getId(), sizeInMb);
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
        clientFile.setName(file.getOriginalFilename());
        clientFile.setType(file.getContentType());
        clientFile.setData(file.getBytes());

        clientFileRepository.save(clientFile);
    }

    @Override
    public void deleteFileById(Long fileId) {
        Optional<ClientFile> clientFile = clientFileRepository.findById(fileId);
        if (clientFile.isEmpty()) {
            return;
        }

        ClientFile file = clientFile.get();
        file.setActual(false);
        clientFileRepository.save(file);
    }

    @Transactional
    @Override
    public ClientFile getFileById(Long id) {
        return clientFileRepository.findClientFileByIdAndActualTrue(id);
    }

    @Transactional
    @Override
    public ClientFileInfo getFileInfoById(Long id) {
        ClientFile clientFile = clientFileRepository.findClientFileByIdAndActualTrue(id);

        if (clientFile == null) {
            return null;
        }
        int sizeInBytes = clientFile.getData().length;
        double sizeInMb = Math.round(((double) sizeInBytes / 1048576) * 1000.0) / 1000.0;

        return new ClientFileInfo(clientFile.getId(), clientFile.getName(), clientFile.getType(),
                clientFile.getClientPackage().getId(), sizeInMb);
    }

    @Transactional
    @Override
    public void updateFile(MultipartFile file, Long fileId) throws IOException {
        ClientFile clientFile = clientFileRepository.findClientFileByIdAndActualTrue(fileId);

        if (clientFile == null) {
            return;
        }

        clientFile.setName(file.getOriginalFilename());
        clientFile.setData(file.getBytes());
        clientFileRepository.save(clientFile);
    }
}
