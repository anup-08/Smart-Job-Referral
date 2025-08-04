package com.smartJob.security;

import com.smartJob.entity.RefreshToken;
import com.smartJob.exception.NotFound;
import com.smartJob.repo.RefreshTokenRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    @Transactional
    public String generateRefreshToken(String userName){

        refreshTokenRepo.deleteByUserName(userName);

        String rToken = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken(rToken , userName , new Date(System.currentTimeMillis()+5*24*60*60*1000));

        refreshTokenRepo.save(refreshToken);
        return rToken;
    }

    public RefreshToken validateRefreshToken(String rToken){
        RefreshToken refreshToken = refreshTokenRepo.findByRefreshToken(rToken).orElseThrow(()->new NotFound("Re-Login,Missing or Invalid token"));

         if (refreshToken.getExpireDate().before(new Date(System.currentTimeMillis()))){
             throw  new IllegalArgumentException("Refresh Token Expired");
         }
         return refreshToken;
    }
}
