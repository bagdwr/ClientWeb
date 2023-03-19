package com.example.clientweb.service;

import com.example.clientweb.entity.Client;
import com.example.clientweb.entity.ClientFile;
import com.example.clientweb.entity.ClientPackage;
import com.example.clientweb.pojo.ClientFileInfo;
import com.example.clientweb.pojo.ClientPackageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    List<ClientFileInfo> getFilesInfo(Long packageId);

    void createFile(Long packageId, MultipartFile file) throws IOException;

    void deleteFileById(Long fileId);

    ClientFile getFileById(Long id);

    ClientFileInfo getFileInfoById(Long id);

    void updateFile(MultipartFile file, Long fileId) throws IOException;
}
