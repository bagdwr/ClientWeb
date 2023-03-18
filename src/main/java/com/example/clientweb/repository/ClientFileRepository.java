package com.example.clientweb.repository;

import com.example.clientweb.entity.ClientFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientFileRepository extends JpaRepository<ClientFile, Long> {
}
