package com.smartJob.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralRequestDto {

    @NotBlank(message = "Enter the Candidate Name")
    private String candidateName;

    @Pattern(regexp = "^[a-zA-Z0-9]+(\\.?[a-zA-Z0-9]+)*@gmail\\.com$", message = "Must be a valid Gmail address")
    private String candidateEmail;

    @NotNull(message = "Enter the Job Id")
    private long jobId;

}
