package com.technotic.jwt2.auth.utils;

import com.technotic.jwt2.auth.entity.UserRole;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    private String name;
    private String email;
    private String userName;
    private String password;
    private UserRole role;
}
