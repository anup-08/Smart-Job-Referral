package com.smartJob.dtos;

import com.smartJob.enums.ReferralStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReferralStatusRequest {
    private String newStatus;
}
