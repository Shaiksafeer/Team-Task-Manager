package com.teammanager.dto;

import com.teammanager.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Role role;
}
