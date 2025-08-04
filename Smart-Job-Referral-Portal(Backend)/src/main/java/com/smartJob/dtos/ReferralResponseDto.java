package com.smartJob.dtos;

import com.smartJob.enums.ReferralStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralResponseDto {
    private Long referralId;
    private String candidateName;
    private String candidateEmail;

    private String jobTitle;
    private String referredByName;
    private String referredByEmail;

    private ReferralStatus status;

    private Date referredAt;

    private String fileName;
}
