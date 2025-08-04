package com.smartJob.dtos;

import com.smartJob.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Name filed can't be blank")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9]+(\\.?[a-zA-Z0-9]+)*@gmail\\.com$", message = "Must be a valid Gmail address")
    @NotBlank(message = "Enter a Valid Gmail")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$", message = "Password must be at least 8 characters long and alphanumeric only")
    @NotBlank(message = "Enter Correct Password")
    private String password;

    private Role role;
}
