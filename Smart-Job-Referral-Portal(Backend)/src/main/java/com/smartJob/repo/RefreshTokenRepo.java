package com.smartJob.repo;

import com.smartJob.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken , Long> {
    void deleteByUserName(String email);

    Optional<RefreshToken> findByRefreshToken(String rToken);
}
