package com.example.clientweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    private String fio;

    @Column(name = "document-number")
    private String documentNumber;

    @Column(name = "given-by")
    private String givenBy;

    @Column(name = "given-date")
    private Date giveDate;

    @Column(name = "expiration-date")
    private Date expirationDate;

    @CreatedDate
    private java.util.Date created_at;
}
