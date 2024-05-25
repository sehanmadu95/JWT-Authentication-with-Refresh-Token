package com.technotic.jwt2.auth.utils;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToekn;
}
