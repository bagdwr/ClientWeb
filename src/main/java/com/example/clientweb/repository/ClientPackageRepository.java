package com.example.clientweb.repository;

import com.example.clientweb.entity.ClientPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientPackageRepository extends JpaRepository<ClientPackage, Long> {
    List<ClientPackage> findByClientIdAndActualTrue(Long id);

    ClientPackage findByIdAndActualTrue(Long id);
}
