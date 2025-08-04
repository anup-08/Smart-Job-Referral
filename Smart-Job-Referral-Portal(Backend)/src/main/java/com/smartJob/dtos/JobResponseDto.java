package com.smartJob.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResponseDto {
    private long id;
    private String title;
    private String companyName;
    private String description;
    private String department;
    private List<String> location = new ArrayList<>();
    private int experienceRequired;
    private Date postedAt;
    private long postedBy;
}
