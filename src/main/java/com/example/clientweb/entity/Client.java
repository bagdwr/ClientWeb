package com.example.clientweb.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "client")
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fio")
    @NotNull
    private String fio;

    @Column(name = "iin")
    @NotNull
    private String iin;

    @Column(name = "document_number")
    @NotNull
    private String documentNumber;

    @Column(name = "given_by")
    @NotNull
    private String givenBy;

    @Column(name = "given_date")
    @NotNull
    private Date givenDate;

    @Column(name = "expiration_date")
    @NotNull
    private Date expirationDate;

    @Column(name = "created_at")
    private Date created_at = new Date();

    @Column(name = "actual")

    private Boolean actual = true;
}
