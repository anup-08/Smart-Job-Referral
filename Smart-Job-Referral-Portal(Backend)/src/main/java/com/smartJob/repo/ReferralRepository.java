package com.smartJob.repo;

import com.smartJob.entity.Job;
import com.smartJob.entity.Referral;
import com.smartJob.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferralRepository extends JpaRepository<Referral,Long> {
//    List<Referral> findByReferredBy(User user);

    List<Referral> findByJob(Job job);

    List<Referral> findByCandidateEmail(String email);

    List<Referral> findByReferredBy(User user);
}
