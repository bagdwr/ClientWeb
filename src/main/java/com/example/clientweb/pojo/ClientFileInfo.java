package com.example.clientweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientFileInfo {
    public Long id;
    public String name;
    public String type;
    public Long packageId;
}
