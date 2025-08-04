package com.smartJob.repo;

import com.smartJob.entity.Job;
import com.smartJob.entity.Referral;
import com.smartJob.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job , Long> {
    Optional<List<Job>> findByTitle(String title);
    List<Job> findByUserId(User user);

}
