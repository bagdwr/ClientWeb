package com.example.clientweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientPackageInfo {
    public Long id;
    public Long userId;
    public String name;
    public double size;
    public int filesAmount=0;
    public Date created_at;

    public ClientPackageInfo(Long id, Long userId, String name, Date created_at) {
        this.id=id;
        this.userId=userId;
        this.name=name;
        this.created_at=created_at;
    }
}
