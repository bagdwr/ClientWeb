package com.example.clientweb.repository;

import com.example.clientweb.entity.ClientPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPackageRepository extends JpaRepository<ClientPackage, Long> {
}
