package com.smartJob.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestDto {
    @NotNull(message = "Title is Required")
    private String title;
    @NotNull(message = "Company Name is Required")
    private String companyName;
    @NotNull(message = "Description is Required")
    private String description;
    @NotNull(message = "Department is Required")
    private String department;
    @NotNull(message = "Location is Required")
    private List<String> location = new ArrayList<>();
    @NotNull(message = "Experience can't be Blank")
    private int experienceRequired;
}
