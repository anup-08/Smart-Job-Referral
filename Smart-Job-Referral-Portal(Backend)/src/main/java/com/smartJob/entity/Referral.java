package com.smartJob.entity;

import com.smartJob.enums.ReferralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "referrals")
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "candidate_email")
    private String candidateEmail;

    @Column(name = "referral_status")
    @Enumerated(EnumType.STRING)
    private ReferralStatus referralStatus;

    @Column(name = "referred_at")
    private Date referredAt;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "employee_id")
    private User referredBy;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY , mappedBy = "referral")
    private Document document;
}
