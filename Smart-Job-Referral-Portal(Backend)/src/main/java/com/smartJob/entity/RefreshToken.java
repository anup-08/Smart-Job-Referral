package com.smartJob.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token" , unique = true ,nullable = false)
    private String refreshToken;

    @Column(name = "user_name")
    private String userName;

    public RefreshToken(String refreshToken, String userName, Date expireDate) {
        this.refreshToken = refreshToken;
        this.userName = userName;
        this.expireDate = expireDate;
    }

    private Date expireDate;

}
