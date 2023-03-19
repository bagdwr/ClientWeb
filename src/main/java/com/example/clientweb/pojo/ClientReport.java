package com.example.clientweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientReport {
    public String fio;
    public Integer packageAmount;
    public Integer filesAmount;
}
